package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EditVehicleModelActivity : AppCompatActivity() {

    private lateinit var editTextModel: EditText
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressDialog
    private lateinit var spinnerBrand: Spinner
    private lateinit var brandName: String
    private var uid: String = ""
    private var name: String = ""
    private var brandList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_vehicle_model)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setTitle("Edit Vehicle Model")
        val intent = intent
        uid = intent.getStringExtra("model_uid").toString()
        name = intent.getStringExtra("model_name").toString()
        brandName = intent.getStringExtra("brand_name").toString()
        editTextModel = findViewById(R.id.edit_text_model)
        buttonUpdate = findViewById(R.id.button_update)
        buttonDelete = findViewById(R.id.button_delete)
        spinnerBrand = findViewById(R.id.spinner_brand)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        editTextModel.setText(name)

        loadSpinnerBrand()

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
        builder.setTitle("Delete Vehicle Model")
        builder.setCancelable(true)
        builder.setPositiveButton("Delete") { dialog, _ ->
            progressBar = ProgressDialog(this)
            progressBar.setMessage("Deleting...")
            progressBar.setCancelable(false)
            progressBar.show()

            reference = database.getReference("Model")
            val deleteModel = mapOf<String,String>(
                "model_status" to "Unavailable"
            )
            reference.child(uid).updateChildren(deleteModel).addOnCompleteListener{
                if (it.isSuccessful) {
                    if (progressBar.isShowing) progressBar.dismiss()
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Vehicle Model Deleted!")
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

    private fun loadSpinnerBrand() {
        reference = database.getReference("Brand")
        reference.orderByChild("brand_name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                brandList.clear()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        brandList.add(data.child("brand_name").value.toString())
                    }
                }

                val adapter = ArrayAdapter(
                    this@EditVehicleModelActivity,
                    android.R.layout.simple_list_item_1, brandList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                spinnerBrand.adapter = adapter
                spinnerBrand.setSelection(adapter.getPosition(brandName))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun updateRecord(uid: String) {
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Updating...")
        progressBar.setCancelable(false)
        progressBar.show()

        if (editTextModel.text.toString().trim().isNullOrEmpty()) {
            editTextModel.error = "This input field cannot be blank!"
            editTextModel.requestFocus()
            if (progressBar.isShowing) progressBar.dismiss()
            return
        }

        reference = database.getReference("Model")
        val updateModel = mapOf<String,String>(
            "model_name" to editTextModel.text.toString()!!,
            "brand_name" to spinnerBrand.selectedItem.toString()
        )
        reference.child(uid!!).updateChildren(updateModel).addOnCompleteListener{
            if (it.isSuccessful) {
                if (progressBar.isShowing) progressBar.dismiss()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Vehicle Model Updated!")
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