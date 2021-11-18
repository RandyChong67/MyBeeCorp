package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EditVehicleBrandActivity : AppCompatActivity() {

    private lateinit var editTextBrand: EditText
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressDialog
    private var uid: String = ""
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_vehicle_brand)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setTitle("Edit Vehicle Brand")
        val intent = intent
        uid = intent.getStringExtra("brand_uid").toString()
        name = intent.getStringExtra("brand_name").toString()
        editTextBrand = findViewById(R.id.edit_text_brand)
        buttonUpdate = findViewById(R.id.button_update)
        buttonDelete = findViewById(R.id.button_delete)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        editTextBrand.setText(name)

        buttonUpdate.setOnClickListener {
            updateRecord(uid)
        }

        buttonDelete.setOnClickListener {
            deleteRecord(uid)
        }
    }

    private fun deleteRecord(uid: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete this record?")
        builder.setTitle("Delete Vehicle Brand")
        builder.setCancelable(true)
        builder.setPositiveButton("Delete") { dialog, _ ->
            progressBar = ProgressDialog(this)
            progressBar.setMessage("Deleting...")
            progressBar.setCancelable(false)
            progressBar.show()

            reference = database.getReference("Brand")
            val deleteBrand = mapOf<String,String>(
                "brand_status" to "Unavailable"
            )
            reference.child(uid).updateChildren(deleteBrand).addOnCompleteListener{
                if (it.isSuccessful) {
                    if (progressBar.isShowing) progressBar.dismiss()
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Vehicle Brand Deleted!")
                    builder.setTitle("Deleted Successfully!")
                    builder.setCancelable(false)
                    builder.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    val alert = builder.create()
                    alert.show()
                }else{
                    if (progressBar.isShowing) progressBar.dismiss()
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Failed to Delete...")
                    builder.setTitle("Failed")
                    builder.setCancelable(false)
                    builder.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val alert = builder.create()
                    alert.show()
                }
            }
        }
        builder.setNegativeButton("Cancel"){ dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun updateRecord( brand_uid: String?,) {
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Updating...")
        progressBar.setCancelable(false)
        progressBar.show()

        if (editTextBrand.text.toString().trim().isNullOrEmpty()) {
            editTextBrand.error = "This input field cannot be blank!"
            editTextBrand.requestFocus()
            if (progressBar.isShowing) progressBar.dismiss()
            return
        }

        reference = database.getReference("Brand")
        val updateBrand = mapOf<String,String>(
            "brand_name" to editTextBrand.text.toString()!!
        )
        reference.child(brand_uid!!).updateChildren(updateBrand).addOnCompleteListener{
            if (it.isSuccessful) {
                if (progressBar.isShowing) progressBar.dismiss()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Vehicle Brand Updated!")
                builder.setTitle("Updated Successfully!")
                builder.setCancelable(false)
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                val alert = builder.create()
                alert.show()
            }else{
                if (progressBar.isShowing) progressBar.dismiss()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Failed to Update...")
                builder.setTitle("Failed")
                builder.setCancelable(false)
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}