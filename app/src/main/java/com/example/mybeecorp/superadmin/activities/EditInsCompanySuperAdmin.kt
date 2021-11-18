package com.example.mybeecorp.superadmin.activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.example.mybeecorp.classes.DbInsCompany
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class EditInsCompanySuperAdmin : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    var instance = FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

    private var imageUri:Uri? = null
    private lateinit var InsuranceCompanyLogo: ImageView
    private lateinit var company_name: String
    private lateinit var company_status: String
    private lateinit var closeStatus: RadioButton
    private lateinit var availableStatus: RadioButton
    private var company_uid:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ins_company_super_admin)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Edit Insurance Company Information")

        company_uid = intent.getStringExtra("companyId")
        val picture = intent.getStringExtra("companyPic")
        val name = intent.getStringExtra("companyName")
        val status = intent.getStringExtra("companyStatus")

        InsuranceCompanyLogo = findViewById(R.id.insuranceCompanyLogo_imageView)
        val browseIcon: ImageView = findViewById(R.id.browse_Icon)
        val companyName: EditText = findViewById(R.id.insuranceCompanyName_EditText)
        closeStatus = findViewById(R.id.closed_radioButton)
        availableStatus= findViewById(R.id.AvailableInsurance_radioButton)
        val cancelBtn: Button = findViewById(R.id.back_button)
        val saveBtn: Button = findViewById(R.id.save_button)

        companyName.setText(name)
        Glide.with(InsuranceCompanyLogo.context).load(picture).into(InsuranceCompanyLogo)
        if( status== "Available"){
            availableStatus.isChecked = true
        }else{
            closeStatus.isChecked = true
        }

        browseIcon.setOnClickListener {
            browseGallery()
        }
        cancelBtn.setOnClickListener{
            onBackPressed()
        }
        saveBtn.setOnClickListener{
            company_name = companyName.text.toString().trim()

            if (company_name.isEmpty()) {
                companyName.error = "This field should not be blank."
                return@setOnClickListener
            }
            if(isValid(company_name){
                    Log.i("Information", "Input is Valid")
                })else{
                companyName.error = "Text entered contains special characters and digit is invalid. "
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
            storageReference.putFile(imageUri!!).addOnSuccessListener {
                //InsuranceCompanyLogo.setImageURI(null)
                storageReference.downloadUrl.addOnSuccessListener {
                    updateToFirebaseDatabase("${it.toString()}")
                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Failed to upload image.", Toast.LENGTH_SHORT).show()
            }
        } else {
            updateToFirebaseDatabase("")
        }
    }

    private fun updateToFirebaseDatabase(Url: String) {
        //database = Firebase.database("https://dbpmt-699fe-default-rtdb.firebaseio.com/")
        database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@EditInsCompanySuperAdmin)
        builder.setTitle("Confirmation Message")
        builder.setMessage("Are you sure to edit?")
        builder.setPositiveButton("Yes") { dialog, which ->
            if (company_uid !=null){
                reference = database.getReference("/Insurance_Company")
                if(Url.isEmpty()){
                    var data = mutableMapOf<String,Any>("company_name" to company_name, "company_status" to company_status)
                    reference.child(company_uid!!).updateChildren(data).addOnSuccessListener {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Message")
                        builder.setMessage("Edit Insurance Company Information successfully!")
                        builder.setCancelable(true)
                        builder.setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        val alert = builder.create()
                        alert.show()
                        return@addOnSuccessListener
                        //Toast.makeText(applicationContext, "Edit Insurance Company information successfully.", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    val insuranceCompany = DbInsCompany(company_uid!!, company_name,Url,company_status)
                    reference.child(company_uid!!).setValue(insuranceCompany).addOnSuccessListener {
                        Toast.makeText(applicationContext, "Edit Insurance Company information successfully.", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(applicationContext, "Error occur.", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("No") { dialog, which ->
            Log.i("Information", "No item had been edited.")
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
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
            imageUri= data?.data!!
            InsuranceCompanyLogo.setImageURI(imageUri)
        }
    }


    //Back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}