package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Type
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class AddVehicleType : AppCompatActivity() {

    private lateinit var buttonAdd: Button
    private lateinit var editTextType: EditText
    private lateinit var buttonCancel: Button
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vehicle_type)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Add New Vehicle Type")

        buttonAdd = findViewById(R.id.button_add)
        buttonCancel = findViewById(R.id.button_cancel)
        editTextType = findViewById(R.id.edit_text_type)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        buttonAdd.setOnClickListener {
            if (editTextType.text.toString().trim().isNullOrEmpty()){
                editTextType.error = "This field cannot be blank!"
                editTextType.requestFocus()
                return@setOnClickListener
            }

            createType()
        }

        buttonCancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun createType() {
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Adding...")
        progressBar.setCancelable(false)
        progressBar.show()

        reference = database.getReference("Type")
        val uid = reference.push().key?: ""
        val status: String = "Available"
        val typeClass = Type(uid, editTextType.text.toString(), status)

        reference.child(uid).setValue(typeClass).addOnCompleteListener{
            if (it.isSuccessful){
                if (progressBar.isShowing) progressBar.dismiss()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("New type added!")
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