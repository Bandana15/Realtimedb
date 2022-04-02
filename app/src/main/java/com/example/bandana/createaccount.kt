package com.example.bandana

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.bandana.databinding.ActivityCreateaccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class createaccount : AppCompatActivity() {
    lateinit var binding:ActivityCreateaccountBinding
    private lateinit var auth: FirebaseAuth
    val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_createaccount)
        auth = Firebase.auth


        binding.button.setOnClickListener {
            if (TextUtils.isEmpty(binding.tf1.text.toString())) {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
            } else if (!Pattern.matches(Patterns.EMAIL_ADDRESS.toString(),binding.tf1.text.toString())) {
                Toast.makeText(this, "enter valid email", Toast.LENGTH_LONG)
                    .show()
            } else if (TextUtils.isEmpty(binding.tf2.text.toString())) {
                Toast.makeText(this, "enter full name", Toast.LENGTH_LONG).show()
            } else if (TextUtils.isEmpty(binding.tf3.text.toString())) {
                Toast.makeText(this, "enter last name", Toast.LENGTH_SHORT).show()
            } else if (binding.tf4.text.toString().length < 10) {
                Toast.makeText(
                    this,
                    "mobile number should not be less than 10 digits",
                    Toast.LENGTH_LONG
                ).show()
            } else if (binding.tf4.text.toString().length < 10) {
                Toast.makeText(
                    this,
                    "mobile number should not be less than 10 digits",
                    Toast.LENGTH_LONG
                ).show()
            } else if (TextUtils.isEmpty(binding.tf5.text.toString())) {
                Toast.makeText(
                    this,
                    "enter password",
                    Toast.LENGTH_LONG
                ).show()

            } else if (binding.tf5.text.toString().length < 6) {
                Toast.makeText(
                    this,
                    "password should be of 6 digit length",
                    Toast.LENGTH_LONG
                ).show()
            } else if (!binding.tf6.text.toString().equals(binding.tf5.text.toString())) {
                Toast.makeText(
                    this,
                    "password and confirm password should match",
                    Toast.LENGTH_LONG
                ).show()
            } else if (binding.rg.checkedRadioButtonId == -1) {
                Toast.makeText(
                    this,
                    "Select Gender",
                    Toast.LENGTH_LONG
                ).show()
            } else if (!binding.checkbox.isChecked) {
                Toast.makeText(
                    this,
                    "Accept terms",
                    Toast.LENGTH_LONG
                ).show()

            } else {
                createAccount(binding.tf1.text.toString(), binding.tf5.text.toString())
            }
        }
        binding.button2.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnprofile.setOnClickListener {
            var intent = Intent(this,profileinformation::class.java)
            startActivity(intent)
        }
        
    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.e(TAG, "${currentUser.displayName}",)

//                        reload();
        }
    }
    private fun updateRealTimeDatabase() {
        val user = auth.currentUser
        var myRef: DatabaseReference = database.getReference(user!!.uid)

        myRef.setValue(user!!.email)
        startActivity(Intent(this, profileinformation::class.java))
        this.finish()
    }

    // [END on_start_check_user]

    fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
//                                updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Account already Exist",
                        Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    //updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }

    fun updateUI(user: FirebaseUser?) {

    }

    fun reload() {

    }

}