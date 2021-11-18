package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Variant
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.math.RoundingMode
import java.text.DecimalFormat

class AddVehicleVariant : AppCompatActivity() {

    private lateinit var buttonAdd: Button
    private lateinit var editTextVariant: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var buttonCancel: Button
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var fabCreate: FloatingActionButton
    private lateinit var buttonFilter: Button
    private lateinit var progressBar: ProgressDialog
    private var modelList = mutableListOf<String>()
    private var brandList = mutableListOf<String>()
    private lateinit var spinnerBrand: Spinner
    private lateinit var spinnerModel: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle_variant)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Add New Vehicle Model")

        buttonAdd = findViewById(R.id.button_add)
        buttonCancel = findViewById(R.id.button_cancel)
        spinnerBrand = findViewById(R.id.spinner_brand)
        spinnerModel = findViewById(R.id.spinner_model)
        editTextVariant = findViewById(R.id.edit_text_variant)
        editTextPrice = findViewById(R.id.edit_text_price)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        loadSpinnerBrand()

        spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                reference = database.getReference("Model")
                reference.orderByChild("model_name")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            modelList.clear()
                            if (snapshot.exists()) {
                                for (data in snapshot.children) {
                                    if (data.child("brand_name").value.toString() == parent!!.getItemAtPosition(
                                            position
                                        ).toString()
                                    )
                                        modelList.add(data.child("model_name").value.toString())
                                }
                            }

                            val adapter = ArrayAdapter(
                                this@AddVehicleVariant,
                                android.R.layout.simple_list_item_1, modelList
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                            spinnerModel.adapter = adapter
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        buttonAdd.setOnClickListener {
            if (editTextVariant.text.toString().trim().isNullOrEmpty()) {
                editTextVariant.error = "This field cannot be blank!"
                editTextVariant.requestFocus()
                return@setOnClickListener
            }
            if (editTextPrice.text.toString().trim().isNullOrEmpty()) {
                editTextPrice.error = "This field cannot be blank!"
                editTextPrice.requestFocus()
                return@setOnClickListener
            }
            if (editTextPrice.text.toString().toDouble() < 1) {
                editTextPrice.error = "Invalid Price!"
                editTextPrice.requestFocus()
                return@setOnClickListener
            }
            if (spinnerModel.selectedItemPosition == -1){
                Toast.makeText(this,"Model cannot be blank!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            createVariant()
        }

        buttonCancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun createVariant() {
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Adding...")
        progressBar.setCancelable(false)
        progressBar.show()

        val price = "%.2f".format(editTextPrice.text.toString().toDouble()).toDouble()

        reference = database.getReference("Variant")
        val uid = reference.push().key ?: ""
        val status = "Available"
        val modelClass = Variant(uid, editTextVariant.text.toString(), price, status, spinnerModel.selectedItem.toString())

        reference.child(uid).setValue(modelClass).addOnCompleteListener {
            if (it.isSuccessful) {
                if (progressBar.isShowing) progressBar.dismiss()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("New variant added!")
                builder.setTitle("Added Successfully!")
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
                builder.setMessage("Failed to add Variant")
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

    private fun loadSpinnerBrand() {
        reference = database.getReference("Brand")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                brandList.clear()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        brandList.add(data.child("brand_name").value.toString())
                    }
                }
                val adapter = ArrayAdapter(
                    this@AddVehicleVariant,
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