package com.example.bandana

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.bandana.databinding.ActivityGallaryBinding

class gallary : AppCompatActivity() {
    lateinit var binding: ActivityGallaryBinding
    private val IMAGE_CHOOSE = 1000
    private val PERMISSION_CODE = 1001
    val REQUEST_CODE = 100
    private val pickImage = 100
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallary)

        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            when(requestCode){
                PERMISSION_CODE -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        chooseImageGallery()
                    }else{
                        Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnimage.setOnClickListener {
        chooseImageGallery()
        }



}
//    fun chooseImageGallery(){
//        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        startActivityForResult(gallery, pickImage)
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && requestCode == pickImage) {
//            imageUri = data?.data
//            binding.iv.setImageURI(imageUri)
//        }
//    }
        private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)

    }





         fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
                binding.iv.setImageURI(data?.data) // handle chosen image
            }
        }

        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            when(requestCode){
                PERMISSION_CODE -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        chooseImageGallery()
                    }else{
                        Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        binding.btnimage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else{
                    chooseImageGallery()

                }
            }else{
                chooseImageGallery()
            }
        }
// companion object

    }
    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)

    }

    }

