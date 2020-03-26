package com.example.tristanfreeman.kotlinfirebasemessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import com.example.tristanfreeman.kotlinfirebasemessenger.Model.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()


        register_button_register.setOnClickListener {
            registerUser()
        }
            already_have_account_textview.setOnClickListener {
                Log.d("MainActivity", "Try to show login activity")
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        selectphoto_button_register.setOnClickListener {
            val imageIntent = Intent(Intent.ACTION_GET_CONTENT)
            imageIntent.type = "image/*"
            startActivityForResult(Intent.createChooser(imageIntent
            , "Select Photo"), 0)

        }
    }
    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            circle_image_view.setImageBitmap(bitmap)
            selectphoto_button_register.alpha = 0f
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            selectphoto_button_register.background = bitmapDrawable
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
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener (this) { task ->
                    if (task.isSuccessful){
                        val user = mAuth.currentUser
                        Log.d("RegisterActivity", "Registered User: $user")
                        uploadImageToFirebaseStorage()
                    }else{
                        Toast.makeText(this, "Failed to create user: ${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().reference.child("images/$filename")
        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Toast.makeText(this@RegisterActivity, "Successfully uploaded image: ${it.metadata?.path}", Toast.LENGTH_SHORT).show()

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("RegisterActivity", "File Location: $it")

                        saveUserToFirebaseDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d("MainActivity", it.localizedMessage)
                }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val database = Firebase.database.reference
        //val ref = database.getReference("/users/${mAuth.currentUser?.uid}")
        val currentUser = mAuth.currentUser
        val user = User(currentUser?.uid, username_edittext_registration.text.toString(), profileImageUrl)
        database.child("users").child(currentUser!!.uid).setValue(user)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d("RegisterActivity", "Saved user to db")
                    }else{
                        Log.d("RegisterActivity 🎟", it.exception.toString())
                    }
                }


    }
}
