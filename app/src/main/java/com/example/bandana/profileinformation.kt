package com.example.bandana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.bandana.databinding.ActivityCreateaccountBinding
import com.example.bandana.databinding.ActivityProfileinformationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class profileinformation : AppCompatActivity() {
    lateinit var binding: ActivityProfileinformationBinding
    private lateinit var auth: FirebaseAuth
    val database = Firebase.database
    lateinit var currentUser: FirebaseUser
    lateinit var myRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileinformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        if(auth.currentUser != null)
        {
            currentUser = auth.currentUser!!
            myRef = database.getReference(currentUser!!.uid)
        }else{
            //back to main screen
        }



        binding.btnupdate.setOnClickListener {
            if (binding.etname.text.toString().isNullOrBlank()) {
                Toast.makeText(this, "Enter name to update", Toast.LENGTH_LONG).show()
            }
            myRef.setValue(binding.etname.text.toString())
        }
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name: String? = snapshot.getValue(String::class.java)
                name?.let {
                    binding.etname.setText(it)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.logout) {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
            return true
        }


        return super.onOptionsItemSelected(item)

    }
}


