package com.example.tristanfreeman.kotlinfirebasemessenger

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {


        }

        back_to_register_textview.setOnClickListener {
            finish()
        }
    }

    fun loginUser() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    Log.d("Login", "Successfully logged in user with uid: ${it.result.user.uid}")

                }.addOnFailureListener {
                    Log.d("Login", "Failed to login user: ${it.message}")
                }
    }
}