package com.example.advancedandroidproject

import ItineraryAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.advancedandroidproject.databinding.FragmentItineraryBinding
import com.google.firebase.firestore.FirebaseFirestore

class ItineraryFragment : Fragment(R.layout.fragment_itinerary) {
    private val TAG="FRAGMENT"
    private val db = FirebaseFirestore.getInstance()
    //binding variables
    private var _binding: FragmentItineraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentItineraryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "3- fragment onViewCreated() running")


// Get a reference to the document to retrieve
        val documentRef = db.collection("itinerary")
        val itemList = mutableListOf<ItineraryItem>()
        documentRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    // Retrieve the three fields and store them into a variable
                    val id = document.id
                    val name = document.getString("name")
                    val address = document.getString("address")
                    val date = document.getTimestamp("date")

                    // Do something with the fields
                    val itineraryToAdd = ItineraryItem(id, name!!,address!!, date!!)
                    itemList.add(itineraryToAdd)
                }
                val recyclerView: RecyclerView = binding.recyclerView
                val navController = Navigation.findNavController(requireActivity(), R.id.container1)
                recyclerView.adapter = ItineraryAdapter(navController, itemList)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error occurred while retrieving documents")
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