package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Variant
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.math.RoundingMode
import java.text.DecimalFormat

class EditVehicleVariantActivity : AppCompatActivity() {

    private lateinit var editTextVariant: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressDialog
    private lateinit var spinnerBrand: Spinner
    private lateinit var spinnerModel: Spinner
    private var modelName: String = ""
    private var brandName: String = ""
    private var uid: String = ""
    private var name: String = ""
    private var price: String = ""
    private var brandList = mutableListOf<String>()
    private var modelList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_vehicle_variant)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setTitle("Edit Vehicle Variant")
        val intent = intent
        uid = intent.getStringExtra("variant_uid").toString()
        name = intent.getStringExtra("variant_name").toString()
        price = intent.getStringExtra("variant_price").toString()
        modelName = intent.getStringExtra("model_name").toString()
        editTextVariant = findViewById(R.id.edit_text_variant)
        editTextPrice = findViewById(R.id.edit_text_price)
        buttonUpdate = findViewById(R.id.button_update)
        buttonDelete = findViewById(R.id.button_delete)
        spinnerBrand = findViewById(R.id.spinner_brand)
        spinnerModel = findViewById(R.id.spinner_model)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        editTextVariant.setText(name)
        editTextPrice.setText(price)

        loadSpinnerBrand()
        loadSpinnerModel()

        spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                reference = database.getReference("Model")
                reference.orderByChild("model_name").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        modelList.clear()
                        if (snapshot.exists()) {
                            for (data in snapshot.children) {
                                if (data.child("brand_name").value.toString() == parent!!.getItemAtPosition(position).toString())
                                modelList.add(data.child("model_name").value.toString())
                            }
                        }

                        val adapter = ArrayAdapter(
                            this@EditVehicleVariantActivity,
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
        builder.setTitle("Delete Vehicle Variant")
        builder.setCancelable(true)
        builder.setPositiveButton("Delete") { dialog, _ ->
            progressBar = ProgressDialog(this)
            progressBar.setMessage("Deleting...")
            progressBar.setCancelable(false)
            progressBar.show()

            reference = database.getReference("Variant")
            val deleteVariant = mapOf<String,String>(
                "variant_status" to "Unavailable"
            )
            reference.child(uid).updateChildren(deleteVariant).addOnCompleteListener{
                if (it.isSuccessful) {
                    if (progressBar.isShowing) progressBar.dismiss()
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Vehicle Variant Deleted!")
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

    private fun updateRecord(uid: String) {
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Updating...")
        progressBar.setCancelable(false)
        progressBar.show()

        if (editTextVariant.text.toString().trim().isNullOrEmpty()) {
            editTextVariant.error = "This input field cannot be blank!"
            editTextVariant.requestFocus()
            if (progressBar.isShowing) progressBar.dismiss()
            return
        }

        if (editTextPrice.text.toString().trim().isNullOrEmpty()) {
            editTextPrice.error = "This input field cannot be blank!"
            editTextPrice.requestFocus()
            if (progressBar.isShowing) progressBar.dismiss()
            return
        }

        if (editTextPrice.text.toString().toDouble() < 0) {
            editTextPrice.error = "Price cannot less than RM0 !"
            editTextPrice.requestFocus()
            if (progressBar.isShowing) progressBar.dismiss()
            return
        }

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        val price = df.format(editTextPrice.text.toString().toDouble()).toDouble()

        reference = database.getReference("Variant")

        val updateVariant = Variant(
            uid,
            editTextVariant.text.toString(),
            price,
            "Available",
            spinnerModel.selectedItem.toString()
        )

        reference.child(uid!!).setValue(updateVariant).addOnCompleteListener {
            if (it.isSuccessful) {
                if (progressBar.isShowing) progressBar.dismiss()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Vehicle Variant Updated!")
                builder.setTitle("Updated Successfully!")
                builder.setCancelable(false)
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                val alert = builder.create()
                alert.show()
            } else {
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

    private fun loadSpinnerModel() {
        reference = database.getReference("Model")
        reference.orderByChild("model_name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                modelList.clear()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        if (data.child("model_name").value.toString() == modelName){
                            brandName = data.child("brand_name").value.toString()
                        }
                        modelList.add(data.child("model_name").value.toString())
                    }
                }

                val adapter = ArrayAdapter(
                    this@EditVehicleVariantActivity,
                    android.R.layout.simple_list_item_1, modelList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                spinnerModel.adapter = adapter
                spinnerModel.setSelection(adapter.getPosition(modelName))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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
                    this@EditVehicleVariantActivity,
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}