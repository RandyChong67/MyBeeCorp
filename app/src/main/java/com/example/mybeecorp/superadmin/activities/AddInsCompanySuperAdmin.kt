package com.example.mybeecorp.superadmin.activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.DbInsCompany
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class AddInsCompanySuperAdmin : AppCompatActivity() {


    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var imageUri: Uri
    private lateinit var company_name: String
    private lateinit var insuranceCompanyLogo: ImageView
    private lateinit var company_status: String
    private lateinit var closeStatus: RadioButton
    private lateinit var availableStatus: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ins_company_super_admin)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Create New Insurance Company")

        val insCompanyName: EditText = findViewById(R.id.insuranceCompanyName_editText)
        insuranceCompanyLogo = findViewById(R.id.companyLogo_imageView)
        val insCompanyStatus: RadioGroup = findViewById(R.id.insCompanyStatus_rgb)
        closeStatus = findViewById(R.id.closed_radioButton)
        availableStatus = findViewById(R.id.AvailableInsurance_radioButton)
        val browseButton: Button = findViewById(R.id.browse_button)
        val resetBtn: Button = findViewById(R.id.back_button)
        val addBtn: Button = findViewById(R.id.add_button)


        browseButton.setOnClickListener {
            browseGallery()
        }
        resetBtn.setOnClickListener {
            insCompanyName.text.clear()
            insCompanyStatus.check(R.id.closed_radioButton)
            insuranceCompanyLogo.setImageBitmap(null)
            insCompanyName.requestFocus()
        }
        addBtn.setOnClickListener {
            company_name = insCompanyName.text.toString().trim()

            if (company_name.isEmpty()) {
                insCompanyName.error = "This field should not be blank."
                return@setOnClickListener
            }
            if(isValid(company_name){
                    Log.i("Information", "Input is Valid")
                })else{
                insCompanyName.error = "Text entered contains special characters and digit is invalid. "
                return@setOnClickListener
            }
            if (insuranceCompanyLogo.drawable == null) {
                Toast.makeText(applicationContext, "Please select an Image.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!closeStatus.isChecked && !availableStatus.isChecked) {
                Toast.makeText(applicationContext, "Please select Insurance Company Status.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            company_status = if (availableStatus.isChecked) "Available" else "Closed"
            uploadImages()
        }
    }

    fun isValid(str: String, function: () -> Unit): Boolean {
        var isValid = false
        val expression = "^[a-z_A-Z ]*$"
        val inputStr: CharSequence = str
        val pattern: Pattern = Pattern.compile(expression)
        val matcher: Matcher = pattern.matcher(inputStr)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }

    private fun uploadImages() {
        val fileName = UUID.randomUUID().toString()
        val storageReference = FirebaseStorage.getInstance().getReference("ImageInsCompanyLogo/$fileName")
        if (imageUri != null) {
            storageReference.putFile(imageUri).addOnSuccessListener {
                //insuranceCompanyLogo.setImageURI(null)
                storageReference.downloadUrl.addOnSuccessListener {
                    saveToFirebaseDatabase(it.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Failed to upload image.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(applicationContext, "Please select an image.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun saveToFirebaseDatabase(Url: String) {
        //database = Firebase.database("https://dbpmt-699fe-default-rtdb.firebaseio.com/")
        database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@AddInsCompanySuperAdmin)
        builder.setTitle("Confirmation Message")
        builder.setMessage("Are you sure to add?")
        builder.setPositiveButton("Yes") { dialog, which ->
            reference = database.getReference("/Insurance_Company")
            val company_uid = reference.push().key?: ""
            val insuranceCompany = DbInsCompany(company_uid, company_name,Url,company_status)
            reference.child(company_uid).setValue(insuranceCompany).addOnSuccessListener {
                //Toast.makeText(applicationContext, "New Insurance Company add successful", Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Message")
                builder.setMessage("New insurance company add successfully!")
                builder.setCancelable(true)
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
            }
            return@setPositiveButton
            clearField()
        }
        builder.setNegativeButton("No") { dialog, which ->
            Log.i("Information", "No new added item.")
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun clearField() {
        val insCompanyName: EditText = findViewById(R.id.insuranceCompanyName_editText)
        val insCompanyStatus: RadioGroup = findViewById(R.id.insCompanyStatus_rgb)

        insCompanyName.text.clear()
        insuranceCompanyLogo.setImageURI(null)
        insCompanyStatus.check(R.id.closed_radioButton)
    }

    //Browse Gallery
    private fun browseGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data!!
            insuranceCompanyLogo.setImageURI(imageUri)
        }
    }

    //Back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

