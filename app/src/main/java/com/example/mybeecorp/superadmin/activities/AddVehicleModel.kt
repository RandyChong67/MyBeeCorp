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
import com.example.mybeecorp.classes.Brand
import com.example.mybeecorp.classes.Model
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddVehicleModel : AppCompatActivity() {

    private var brandList = mutableListOf<String>()
    private lateinit var buttonAdd: Button
    private lateinit var editTextModel: EditText
    private lateinit var buttonCancel: Button
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressDialog
    private lateinit var spinnerBrand: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle_model)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Add New Vehicle Model")

        buttonAdd = findViewById(R.id.button_add)
        buttonCancel = findViewById(R.id.button_cancel)
        spinnerBrand = findViewById(R.id.spinner_brand)
        editTextModel = findViewById(R.id.edit_text_model)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        loadSpinnerBrand()

        buttonAdd.setOnClickListener {
            if (editTextModel.text.toString().trim().isNullOrEmpty()) {
                editTextModel.error = "This field cannot be blank!"
                editTextModel.requestFocus()
                return@setOnClickListener
            }
            createModel()
        }

        buttonCancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun createModel() {
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Adding...")
        progressBar.setCancelable(false)
        progressBar.show()

        reference = database.getReference("Model")
        val uid = reference.push().key ?: ""
        val status = "Available"
        val modelClass = Model(uid, editTextModel.text.toString(), status, spinnerBrand.selectedItem.toString())

        reference.child(uid).setValue(modelClass).addOnCompleteListener {
            if (it.isSuccessful) {
                if (progressBar.isShowing) progressBar.dismiss()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("New model added!")
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

    private fun loadSpinnerBrand() {
        reference = database.getReference("Brand")
        reference.orderByValue()
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                brandList.clear()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        brandList.add(data.child("brand_name").value.toString())
                    }
                }
                val adapter = ArrayAdapter(
                    this@AddVehicleModel,
                    android.R.layout.simple_list_item_1, brandList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                spinnerBrand.adapter = adapter

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    //Back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}