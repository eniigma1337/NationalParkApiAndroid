package com.example.advancedandroidproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.DatePicker.OnDateChangedListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.advancedandroidproject.databinding.FragmentEditItineraryBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class EditItineraryFragment : Fragment(R.layout.fragment_edit_itinerary) {
    private val TAG="FRAGMENT EDIT"
    private var selectedDate: Date? = null
    private val args:EditItineraryFragmentArgs by navArgs()
    private val db = FirebaseFirestore.getInstance()

    //binding variables
    private var _binding: FragmentEditItineraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditItineraryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "3- fragment onViewCreated() running")
        var t: Timestamp? = null

        binding.button3.setOnClickListener {
            deleteDocumentById()
        }

        val documentRef = db.collection("itinerary").document(args.id)
        documentRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val data = document.data

                    // Save the fields to variables
                    val name = data!!["name"] as String
                    val address = data!!["address"] as String
                    val notes = data!!["notes"] as String

                    val timestamp = data!!["date"] as Timestamp

                    selectedDate = timestamp!!.toDate()
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    binding.dateTextView.text = dateFormat.format(selectedDate!!)

                    val calendar = Calendar.getInstance()
                    calendar.time = timestamp?.toDate()

                    // Set the DatePicker to the date from the Calendar
                    binding.datePicker.init(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ) { _, _, _, _ -> }

                    binding.parkName.text = name
                    binding.parkAddress.text = address
                    binding.editNotesText.setText(notes)

                    binding.datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
                        calendar.set(Calendar.MILLISECOND, 0)

                        t = Timestamp(calendar.time)

                    }

                }
                else{
                    Log.d(TAG, "cant find document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error occurred while retrieving documents")
            }

        binding.button2.setOnClickListener(){
            var updates =hashMapOf<String, Any>()
            updates = if (t != null) {
                hashMapOf<String, Any>(
                    "notes" to binding.editNotesText.text.toString(),
                    "date" to t!!
                )
            } else {
                hashMapOf<String, Any>(
                    "notes" to binding.editNotesText.text.toString()
                )
            }
            documentRef.update(updates)
                .addOnSuccessListener {
                    Log.d(TAG, "updated the document")
                    Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, "cant find document")
                }

        }



    }
    private fun deleteDocumentById() {
        val docRef = db.collection("itinerary").document(args.id)
        // delete document
        docRef.delete()
            .addOnSuccessListener {
                // Handle success
                println("Document $args.id successfully deleted!")
                val action = EditItineraryFragmentDirections.actionEditItineraryFragmentToItineraryFragment()
                findNavController().navigate(action)
            }
            .addOnFailureListener { e ->
                // Handle failure
                println("Error deleting document $args.id: $e")
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "4- fragment onDestroy() running")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}