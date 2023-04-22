package com.example.advancedandroidproject

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.advancedandroidproject.databinding.FragmentSignInBinding
import com.example.advancedandroidproject.models.ParkAsArg
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignIn : Fragment() {


    lateinit var binding : FragmentSignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var action : NavDirections









    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        firebaseAuth = FirebaseAuth.getInstance()


        binding.buttsignin.setOnClickListener {
            val email = binding.etemail.text.toString()
            val pass = binding.etpass.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)

                        action = SignInDirections.actionSignInToFindParkFragment()
                        findNavController().navigate(action)

                    }
                }
            }
        }




        binding.buttsignup.setOnClickListener {

            action = SignInDirections.actionSignInToSignUp()
            findNavController().navigate(action)

        }




    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            action = SignInDirections.actionSignInToSignUp()
            findNavController().navigate(action)
        }
    }


}