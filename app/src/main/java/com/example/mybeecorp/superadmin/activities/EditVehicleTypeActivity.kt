package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Type
import com.example.mybeecorp.classes.Variant
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EditVehicleTypeActivity : AppCompatActivity() {

    private lateinit var editTextType: EditText
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressDialog
    private var uid: String = ""
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_vehicle_type)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setTitle("Edit Vehicle Type")
        val intent = intent
        uid = intent.getStringExtra("type_uid").toString()
        name = intent.getStringExtra("type_name").toString()
        editTextType = findViewById(R.id.edit_text_type)
        buttonUpdate = findViewById(R.id.button_update)
        buttonDelete = findViewById(R.id.button_delete)

        editTextType.setText(name)

        buttonUpdate.setOnClickListener {
            updateRecord(editTextType.text.toString(), uid)
        }

        buttonDelete.setOnClickListener {
            deleteRecord(uid)
        }
    }

    private fun deleteRecord(uid: String) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete this record?")
        builder.setTitle("Delete Vehicle Type")
        builder.setCancelable(true)
        builder.setPositiveButton("Delete") { dialog, _ ->
            progressBar = ProgressDialog(this)
            progressBar.setMessage("Deleting...")
            progressBar.setCancelable(false)
            progressBar.show()

            database =
                Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            reference = database.getReference("Type")
            val deleteType = mapOf<String,String>(
                "type_status" to "Unavailable"
            )
            reference.child(uid).updateChildren(deleteType).addOnCompleteListener{
                if (it.isSuccessful) {
                    if (progressBar.isShowing) progressBar.dismiss()
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Vehicle Type Deleted!")
                    builder.setTitle("Success!")
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

    private fun updateRecord(typeEdit: String?, type_uid: String?,) {
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Updating...")
        progressBar.setCancelable(false)
        progressBar.show()

        if (typeEdit?.trim().isNullOrEmpty()) {
            editTextType.error = "This input field cannot be blank!"
            editTextType.requestFocus()
            if (progressBar.isShowing) progressBar.dismiss()
            return
        }
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Type")
        val updateType = mapOf<String,String>(
            "type_name" to typeEdit!!
        )
        reference.child(type_uid!!).updateChildren(updateType).addOnCompleteListener{
            if (it.isSuccessful) {
                if (progressBar.isShowing) progressBar.dismiss()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Vehicle Type Updated!")
                builder.setTitle("Success!")
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