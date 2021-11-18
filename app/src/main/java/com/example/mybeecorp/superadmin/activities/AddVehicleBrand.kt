package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Brand
import com.example.mybeecorp.classes.Type
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddVehicleBrand : AppCompatActivity() {

    private lateinit var buttonAdd: Button
    private lateinit var editTextBrand: EditText
    private lateinit var buttonCancel: Button
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_brand)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Add New Vehicle Brand")

        buttonAdd = findViewById(R.id.button_add)
        buttonCancel = findViewById(R.id.button_cancel)
        editTextBrand = findViewById(R.id.edit_text_brand)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        buttonAdd.setOnClickListener {

            if (editTextBrand.text.toString().trim().isNullOrEmpty()) {
                editTextBrand.error = "This field cannot be blank!"
                editTextBrand.requestFocus()
                return@setOnClickListener
            }

            createBrand()
        }

        buttonCancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun createBrand() {
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Adding...")
        progressBar.setCancelable(false)
        progressBar.show()

        reference = database.getReference("Brand")
        val uid = reference.push().key ?: ""
        val status: String = "Available"
        val typeClass = Brand(uid, editTextBrand.text.toString(), status)

        reference.child(uid).setValue(typeClass).addOnCompleteListener {
            if (it.isSuccessful) {
                if (progressBar.isShowing) progressBar.dismiss()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("New brand added!")
                builder.setTitle("Added Successfully!")
                builder.setCancelable(false)
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                val alert = builder.create()
                alert.show()
            }
        }
    }

    //Back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
