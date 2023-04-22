import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.advancedandroidproject.ItineraryFragmentDirections
import com.example.advancedandroidproject.ItineraryItem
import com.example.advancedandroidproject.R
import java.text.SimpleDateFormat
import java.util.Locale


class ItineraryAdapter(private val navController: NavController, private val itineraryList: List<ItineraryItem>) :
    RecyclerView.Adapter<ItineraryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itinerary_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itinerary = itineraryList[position]
        holder.nameTextView.text = itinerary.name
        holder.addressTextView.text = itinerary.address
        val selectedDate = itinerary.date.toDate()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.dateTextView.text = dateFormat.format(selectedDate)

        holder.itemView.setOnClickListener {
            val action = ItineraryFragmentDirections.actionItineraryFragmentToEditItineraryFragment(itinerary.id)
            navController.navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return itineraryList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val addressTextView: TextView = view.findViewById(R.id.addressTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
    }
}