package com.example.mybeecorp.superadmin.activities

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mybeecorp.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.mybeecorp.classes.DbInsurance
import com.example.mybeecorp.classes.DbItemCovered
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class AddInsuranceSuperAdmin : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var insurance_name:String
    private lateinit var insurance_class:String
    private lateinit var insurance_status: String
    private lateinit var unavailableStatus: RadioButton
    private lateinit var availableStatus: RadioButton
    private lateinit var insurance_company: String
    private lateinit var insCompanySpinner: Spinner
    private lateinit var item_name:String
    private lateinit var item_price:String
    private lateinit var item_status:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_insurance_super_admin)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("Create New Insurance")

        insCompanySpinner = findViewById(R.id.insuranceCompanyName_spinner)
        val insuranceClass = resources.getStringArray(R.array.InsuranceClass)
        val insClassDetail:TextView = findViewById(R.id.insClassDetail_TextView)
        unavailableStatus = findViewById(R.id.unavailableInsStatus_radioButton)
        availableStatus = findViewById(R.id.AvailableInsStatus_radioButton)
        val resetBtn: Button = findViewById(R.id.back_button)
        val addBtn: Button = findViewById(R.id.add_button)

        //unavailableStatus.isChecked = true
        loadCompanySpinner()

        // Spinner - Insurance Class
        val spinnerClass = findViewById<Spinner>(R.id.insuranceClass_spinner)
        if (spinnerClass != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, insuranceClass)
            spinnerClass.adapter = adapter
        }
        spinnerClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected( //当spinner的item selected的时候
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!!.getItemAtPosition(position).toString() == "Class A"){
                    Log.i("Information","Class A had been selected.")
                    insClassDetail.text = "Class A - Cover Claim of Own Vehicle and Third Party Vehicle"
                }
                else{
                    Log.i("Information","Class C had been selected.")
                    insClassDetail.text = "Class C - Only Covered Third Party Vehicle"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { //当没有item selected的时候
                Log.i("Information","Nothing had been selected.")
            }

        }

        resetBtn.setOnClickListener{
            clearField()
        }
        addBtn.setOnClickListener{
            createMotorInsurance()
        }
    }

    private fun createMotorInsurance() {
        val insuranceName: EditText = findViewById(R.id.insuranceName_editText)
        val spinnerClass = findViewById<Spinner>(R.id.insuranceClass_spinner)
        val insClassDetail:TextView = findViewById(R.id.insClassDetail_TextView)
        val itemCovered1: CheckBox = findViewById(R.id.itemCovered1_checkBox)
        val itemCovered2: CheckBox = findViewById(R.id.itemCovered2_checkBox)
        val itemCovered3: CheckBox = findViewById(R.id.itemCovered3_checkBox)
        val itemCovered4: CheckBox = findViewById(R.id.itemCovered4_checkBox)
        val itemCovered5: CheckBox = findViewById(R.id.itemCovered5_checkBox)
        val itemCovered6: CheckBox = findViewById(R.id.itemCovered6_checkBox)
        val itemCovered7: CheckBox = findViewById(R.id.itemCovered7_checkBox)
        val itemCovered8: CheckBox = findViewById(R.id.itemCovered8_checkBox)
        insurance_name = insuranceName.text.toString().trim()
        insurance_company = insCompanySpinner.selectedItem.toString()
        insurance_class = spinnerClass.selectedItem.toString()

        if (insurance_name.isEmpty()) {
            insuranceName.error = "This field should not be blank."
            return
        }
        if(isValid(insurance_name){
                Log.i("Information", "Input is Valid")
            })else{
            insuranceName.error = "Text entered contains special characters and digit is invalid. "
            return
        }

        if (!unavailableStatus.isChecked && !availableStatus.isChecked) {
            Toast.makeText(applicationContext, "Please select Insurance Status.", Toast.LENGTH_LONG).show()
            return
        }
        insurance_status = if (availableStatus.isChecked) "Available" else "Unavailable"
        if(!itemCovered1.isChecked && !itemCovered2.isChecked && !itemCovered3.isChecked && !itemCovered4.isChecked
            && !itemCovered5.isChecked && !itemCovered6.isChecked&& !itemCovered7.isChecked&& !itemCovered8.isChecked){
            Toast.makeText(applicationContext, "Please select item covered(s).", Toast.LENGTH_LONG).show()
            return
        }
        saveToFirebaseDatabase()
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

    // Create Motor Insurance
    private fun saveToFirebaseDatabase() {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@AddInsuranceSuperAdmin)
        builder.setTitle("Confirmation Message")
        builder.setMessage("Are you sure to add?")

        builder.setPositiveButton("Yes") { dialog, which ->
            //database = Firebase.database("https://dbpmt-699fe-default-rtdb.firebaseio.com/")
            database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            reference = database.getReference("/Motor_Insurance")
            val insurance_uid = reference.push().key?: ""
            val motorInsurance = DbInsurance(insurance_uid, insurance_name,insurance_company,insurance_class,insurance_status)
            reference.child(insurance_uid).setValue(motorInsurance).addOnSuccessListener {
                createItemCovered(insurance_uid)
            }
        }
        builder.setNegativeButton("No") { dialog, which ->
            Log.i("Information", "No new added item.")
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    // Create Motor Insurance - Item Covered
    private fun createItemCovered(uid:String) {
        val itemCovered1: CheckBox = findViewById((R.id.itemCovered1_checkBox))
        val itemCovered2: CheckBox = findViewById((R.id.itemCovered2_checkBox))
        val itemCovered3: CheckBox = findViewById((R.id.itemCovered3_checkBox))
        val itemCovered4: CheckBox = findViewById((R.id.itemCovered4_checkBox))
        val itemCovered5: CheckBox = findViewById((R.id.itemCovered5_checkBox))
        val itemCovered6: CheckBox = findViewById((R.id.itemCovered6_checkBox))
        val itemCovered7: CheckBox = findViewById((R.id.itemCovered7_checkBox))
        val itemCovered8: CheckBox = findViewById((R.id.itemCovered8_checkBox))

        if(itemCovered1.isChecked) {
            item_name = itemCovered1.getText().toString()
            item_price = "1900.00"
            item_status= "Available"
            saveToItemCoveredFirebase(uid)
        }
        if(itemCovered2.isChecked){
            item_name = itemCovered2.getText().toString()
            item_price = "2000.00"
            item_status= "Available"
            saveToItemCoveredFirebase(uid)
        }
        if(itemCovered3.isChecked){
            item_name = itemCovered3.getText().toString()
            item_price = "1500.00"
            item_status= "Available"
            saveToItemCoveredFirebase(uid)
        }
        if(itemCovered4.isChecked){
            item_name = itemCovered4.getText().toString()
            item_price = "1000.00"
            item_status= "Available"
            saveToItemCoveredFirebase(uid)
        }
        if(itemCovered5.isChecked){
            item_name = itemCovered5.getText().toString()
            item_price = "1200.00"
            item_status= "Available"
            saveToItemCoveredFirebase(uid)
        }
        if(itemCovered6.isChecked){
            item_name = itemCovered6.getText().toString()
            item_price = "900.00"
            item_status= "Available"
            saveToItemCoveredFirebase(uid)
        }
        if(itemCovered7.isChecked){
            item_name = itemCovered7.getText().toString()
            item_price = "3000.00"
            item_status= "Available"
            saveToItemCoveredFirebase(uid)
        }
        if(itemCovered8.isChecked){
            item_name = itemCovered8.getText().toString()
            item_price = "399.00"
            item_status= "Available"
            saveToItemCoveredFirebase(uid)
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Message")
        builder.setMessage("New insurance add successfully!")
        builder.setCancelable(true)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
        clearField()
        return
    }

    private fun saveToItemCoveredFirebase(uid:String) {
        reference = database.getReference("/Item_Covered").child(uid)
        val item_uid = reference.push().key?: ""
        val itemCovered = DbItemCovered(item_uid,item_name,item_price, item_status)
        reference.child(item_uid).setValue(itemCovered).addOnSuccessListener {
            //Toast.makeText(applicationContext, "New Insurance add successful", Toast.LENGTH_SHORT).show()
            Log.i("Info","Add successfully.")
        }
    }

    private fun clearField() {
        val insuranceName: EditText = findViewById(R.id.insuranceName_editText)
        insCompanySpinner = findViewById(R.id.insuranceCompanyName_spinner)
        val insuranceStatus: RadioGroup = findViewById(R.id.insuranceStatus_radioGroup)
        unavailableStatus = findViewById(R.id.unavailableInsStatus_radioButton)
        availableStatus = findViewById(R.id.AvailableInsStatus_radioButton)
        val itemCovered1: CheckBox = findViewById((R.id.itemCovered1_checkBox))
        val itemCovered2: CheckBox = findViewById((R.id.itemCovered2_checkBox))
        val itemCovered3: CheckBox = findViewById((R.id.itemCovered3_checkBox))
        val itemCovered4: CheckBox = findViewById((R.id.itemCovered4_checkBox))
        val itemCovered5: CheckBox = findViewById((R.id.itemCovered5_checkBox))
        val itemCovered6: CheckBox = findViewById((R.id.itemCovered6_checkBox))
        val itemCovered7: CheckBox = findViewById((R.id.itemCovered7_checkBox))
        val itemCovered8: CheckBox = findViewById((R.id.itemCovered8_checkBox))
        val spinnerClass = findViewById<Spinner>(R.id.insuranceClass_spinner)
        val insClassDetail:TextView = findViewById(R.id.insClassDetail_TextView)

        if(insuranceName.text.isEmpty() && !unavailableStatus.isChecked && !availableStatus.isChecked &&  !itemCovered1.isChecked
            && !itemCovered2.isChecked && !itemCovered3.isChecked && !itemCovered4.isChecked && !itemCovered5.isChecked
            && !itemCovered6.isChecked && !itemCovered7.isChecked && !itemCovered8.isChecked){
            insCompanySpinner.setSelection(0)
            spinnerClass.setSelection(0)
            Toast.makeText(applicationContext, "The fields already cleared.", Toast.LENGTH_LONG).show()
        }
        else{
            insuranceName.text.clear()
            insuranceStatus.check(R.id.unavailableInsStatus_radioButton)
            insCompanySpinner.setSelection(0)
            spinnerClass.setSelection(0)
            itemCovered1.isChecked = false
            itemCovered2.isChecked = false
            itemCovered3.isChecked = false
            itemCovered4.isChecked = false
            itemCovered5.isChecked = false
            itemCovered6.isChecked = false
            itemCovered7.isChecked = false
            itemCovered8.isChecked = false
            insuranceName.requestFocus()
        }
    }

    //Get data for Insurance Company from Firebase
    private fun loadCompanySpinner() {
        val company = ArrayList<String>()
        //val companyRef = Firebase.database("https://dbpmt-699fe-default-rtdb.firebaseio.com/")
        val companyRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Insurance_Company")

        companyRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        company.add(i.child("company_name").value.toString())
                    }
                    val companyAdapter = ArrayAdapter<String>(this@AddInsuranceSuperAdmin, android.R.layout.simple_spinner_item, company)
                    companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                    insCompanySpinner.adapter = companyAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG).show()
            }
        })
    }

    //Back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}






