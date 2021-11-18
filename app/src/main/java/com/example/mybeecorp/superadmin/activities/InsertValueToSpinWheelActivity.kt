package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Spin_Wheel
import com.example.mybeecorp.classes.Test
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class InsertValueToSpinWheelActivity : AppCompatActivity() {

    private lateinit var ImageUri: Uri
    private lateinit var imageView: ImageView
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_value_to_spin_wheel)
        imageView = findViewById(R.id.image_view_test)

        val buttonSelectImage = findViewById<Button>(R.id.button_select_image)
        buttonSelectImage.setOnClickListener {
            selectImage()
        }

        val buttonUploadImage = findViewById<Button>(R.id.button_upload_image)
        buttonUploadImage.setOnClickListener {
            uploadImage()
        }

        val prizeName = findViewById<EditText>(R.id.edit_text_prizes)
        val buttonInsert = findViewById<Button>(R.id.button_submit)
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        buttonInsert.setOnClickListener {
            if (prizeName.text.toString().isNotEmpty()) {
                reference = database.getReference("Spin_Wheel")
                val uid = reference.push().key?: ""
                val prize = Spin_Wheel(uid, prizeName.text.toString())
                reference.child(uid!!).setValue(prize).addOnCompleteListener {
                    Toast.makeText(
                        applicationContext,
                        "Prize insert successful",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    private fun uploadImage() {
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Uploading Image...")
        progressBar.setCancelable(false)
        progressBar.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        if (ImageUri != null){
            storageReference.putFile(ImageUri).addOnSuccessListener {
                imageView.setImageURI(null)
                if (progressBar.isShowing) progressBar.dismiss()
                storageReference.downloadUrl.addOnSuccessListener {
                    saveToFirebaseDatabase("${it.toString()}")
                }
            }.addOnFailureListener{
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT)
                if (progressBar.isShowing) progressBar.dismiss()
            }
        }else{
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT)
        }
    }

    private fun saveToFirebaseDatabase(url: String) {
        reference = database.getReference("Test")
        val uid = reference.push().key?: ""
        val test = Test(uid, "Test name", url)
        reference.child(uid).setValue(test).addOnSuccessListener {
            Toast.makeText(this, "Added image to Realtime DB", Toast.LENGTH_SHORT)
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            ImageUri = data?.data!!
            imageView.setImageURI(ImageUri)
        }
    }
}