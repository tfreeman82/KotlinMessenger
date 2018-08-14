package com.example.tristanfreeman.kotlinfirebasemessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

import com.google.firebase.auth.FirebaseUser

import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.R.attr.password
import android.support.v4.app.FragmentActivity
import android.R.attr.password




class MainActivity : AppCompatActivity() {
    //private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //mAuth = FirebaseAuth.getInstance()


        register_button_register.setOnClickListener {
            registerUser()
        }
            already_have_account_textview.setOnClickListener {
                Log.d("MainActivity", "Try to show login activity")
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

    }

    fun registerUser() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_registration.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/password", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("MainActivity", "Email is " + email)
        Log.d("MainActivity", "Password: $password")

        //Firebase authentication for user
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    Log.d("Main", "Successfully created user with uid: ${it.result.user.uid}")

                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                    Log.d("Main", "Failed to create user: ${it.message}")
                }
    }
}
