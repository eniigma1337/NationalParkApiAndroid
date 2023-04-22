package com.example.advancedandroidproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.advancedandroidproject.databinding.FragmentParkDetailsBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.util.Calendar
import java.util.Date


class ParkDetailsFragment : Fragment(R.layout.fragment_park_details) {
    private val TAG="FRAGMENT"
    //binding variables
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentParkDetailsBinding? = null
    private val binding get() = _binding!!
    private val args:ParkDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentParkDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()

        var name = ""
        var address = ""

        Log.d(TAG, "3- fragment onViewCreated() running")


//        binding.button.setOnClickListener {
//            val action = ParkDetailsFragmentDirections.actionParkDetailsFragmentToItineraryFragment()
//            findNavController().navigate(action)
//        }

            //make url clickable
            val textView = binding.url
            val text = args.selectedPark.url
            textView.text = text
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    // define the URL
                    val url = args.selectedPark.url

                    // intent to open in browser
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
            val spannableString = SpannableString(text)
            spannableString.setSpan(clickableSpan, text.indexOf(args.selectedPark.url), text.indexOf(args.selectedPark.url) + args.selectedPark.url.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            textView.text = spannableString
            textView.movementMethod = LinkMovementMethod.getInstance()



            name = args.selectedPark.fullName
            val image = args.selectedPark.image
            val description = args.selectedPark.description
            address = args.selectedPark.address
        binding.description.movementMethod = ScrollingMovementMethod()
            binding.fullname.text = name
            binding.description.text = description
            binding.address.text = address
            Glide.with(requireContext())
                .load(image)
                .into(binding.imageView)

        binding.addbutton.setOnClickListener(){
                var currentDate = LocalDate.now()
                var notes = ""

                val today = Date() // get the current date and time
                val calendar = Calendar.getInstance()
                calendar.time = today
                // set the hour,minute,second,millisecond to 0
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val date = calendar.time
                val timestamp = Timestamp(date)

                val itinerary: MutableMap <String, Any> = HashMap()
                itinerary["name"] = name
                itinerary["date"] = timestamp
                itinerary["address"] = address
                itinerary["notes"] = "Notes..."

                db.collection("itinerary")
                    .whereEqualTo("name", name)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            // if no itinerary with the same name exists, add to Firestore
                            db.collection("itinerary")
                                .add(itinerary)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Added document with ID ${it.id}")
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Error adding document", e)
                                }
                        } else {
                            // If an itinerary already exists, log an error message
                            Log.w(TAG, "An itinerary with the same name already exists")
                            Toast.makeText(context, "Already exists in your Itinerary", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error checking for existing itinerary", e)
                    }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}