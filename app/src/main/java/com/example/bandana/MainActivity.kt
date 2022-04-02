package com.example.bandana

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.example.bandana.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val database = Firebase.database
    lateinit var myRef : DatabaseReference
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        binding.btnpermissions.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                }

                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            }
        }
        binding.btngallary.setOnClickListener {
            var intent = Intent(this, gallary::class.java)
            startActivity(intent)
        }

        binding.etname.doOnTextChanged { text, start, count, after ->
            if (TextUtils.isEmpty(text)) {
                binding.etname.error = "Enter user name"
            } else {
                binding.etname.error = null
            }
        }
        binding.etpassword.doOnTextChanged { text, start, count, after ->
            if (TextUtils.isEmpty(text)) {
                binding.etpassword.error = "Enter Password"
            } else {
                binding.etpassword.error = null
            }
        }

        binding.btn.setOnClickListener {
            if (TextUtils.isEmpty(binding.etname.text.toString())) {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
            } else if (!Pattern.matches(
                    Patterns.EMAIL_ADDRESS.toString(),
                    binding.etname.text.toString()
                )
            ) {
                Toast.makeText(this, "Enter valid EMAIL", Toast.LENGTH_SHORT)
                    .show()
            } else if (TextUtils.isEmpty(binding.etpassword.text.toString())) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
            } else if (binding.etpassword.text.toString().length < 6) {
                Toast.makeText(
                    this,
                    "Password should be of atleast 6 characters",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                signIn(binding.etname.text.toString(),binding.etpassword.text.toString())
                //createAccount(binding.etname.text.toString(), binding.etpassword.text.toString())

            }
        }
        binding.btngoogle.setOnClickListener {
        signIn()
        }
        binding.btnprofile.setOnClickListener {
            var intent = Intent(this,profileinformation::class.java)
            startActivity(intent)
        }
        binding.btnaccount.setOnClickListener {
            var intent = Intent(this, createaccount::class.java)
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
    // [END on_start_check_user]

    fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    startActivity(Intent(this@MainActivity, profileinformation::class.java))

//                                updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                    //updateUI(null)
                }
            }
        // [END create_user_with_email]
    }
    private fun updateRealTimeDatabase() {
        val user = auth.currentUser
        var myRef: DatabaseReference = database.getReference(user!!.uid)

        myRef.setValue(user!!.email)
        startActivity(Intent(this@MainActivity, profileinformation::class.java))
        this.finish()
    }

    fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    startActivity(Intent(this@MainActivity, profileinformation::class.java))
                    // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "Account does not exist", task.exception)
                    Toast.makeText(
                        baseContext, "Account does not exist",
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //updateUI(null)
                }
            }
    }
    // [END auth_with_google]

    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    //private fun updateUI(user: FirebaseUser?) {

    //}

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

}
