package com.example.bandana

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bandana.databinding.ActivityPhonecallsBinding
import android.provider.ContactsContract
import android.util.Log
import android.widget.AdapterView
import android.widget.SimpleCursorAdapter
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader

class phonecalls : AppCompatActivity(){
    lateinit var binding:ActivityPhonecallsBinding
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var adapter:Adapter
    var recyclerArray: ArrayList<Phone> =ArrayList()


    private val FROM_COLUMNS: Array<String> = arrayOf(
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)) {
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        } else {
            ContactsContract.Contacts.DISPLAY_NAME
        }
    )


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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phonecalls)
        adapter = Adapter(recyclerArray)
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rv.layoutManager = linearLayoutManager
        binding.rv.adapter = adapter
        binding.btnphone.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getcontacts()
                    // You can use the API that requires the permission.
                }

                else -> {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_CONTACTS
                    )
                }
            }
        }

    }
     fun getcontacts(){
         this?.let {
            val cursor =  this.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            while(cursor?.moveToNext()==true){
                var name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phoneno = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                Log.e(TAG, "name $name,phoneno $phoneno", )
                recyclerArray.add(Phone(name,phoneno))
                adapter.notifyDataSetChanged()
            }
        } ?: throw IllegalStateException()
    }
    }
