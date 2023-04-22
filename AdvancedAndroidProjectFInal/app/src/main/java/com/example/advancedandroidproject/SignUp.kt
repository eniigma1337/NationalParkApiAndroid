package com.example.advancedandroidproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.advancedandroidproject.databinding.FragmentSignInBinding
import com.example.advancedandroidproject.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth


class SignUp : Fragment() {

    lateinit var binding: FragmentSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var action : NavDirections



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)



        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttsignin2.setOnClickListener {


            action = SignUpDirections.actionSignUpToSignIn()
            findNavController().navigate(action)



        }
        binding.buttsignup2.setOnClickListener {
            val email = binding.etemail2.text.toString()
            val pass = binding.etpass2.text.toString()
            val confirmPass = binding.etconfirmpass.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            action = SignUpDirections.actionSignUpToSignIn()
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }








    }



}