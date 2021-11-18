package com.example.mybeecorp.insurancecompanyadmin.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.DbItemCovered
import java.util.regex.Matcher
import java.util.regex.Pattern

class EditInsuranceCA : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    var instance = FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

    private lateinit var insurance_name:String
    private lateinit var insurance_class:String
    private lateinit var insurance_status: String

    private lateinit var unavailableStatus: RadioButton
    private lateinit var availableStatus: RadioButton

    private var insurance_uid:String? = null
    private lateinit var userCompany: String
    private var itemId:String? = null
    private lateinit var item_name:String
    private lateinit var item_price:String
    private lateinit var item_status:String

    private var insuranceCovered = mutableMapOf<String, DbItemCovered>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_insurance_ca)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("Edit Insurance Information")

        insurance_uid = intent.getStringExtra("insuranceId")
        val name = intent.getStringExtra("insuranceName")
        val insClass = intent.getStringExtra("insuranceClass")
        val insStatus = intent.getStringExtra("insuranceStatus")

        val insuranceName = findViewById<EditText>(R.id.insuranceName_editText)
        val insuranceClass = resources.getStringArray(R.array.InsuranceClass)
        val spinnerClass = findViewById<Spinner>(R.id.insuranceClass_spinner)
        val insClassDetail: TextView = findViewById(R.id.insClassDetail_TextView)
        unavailableStatus = findViewById(R.id.unavailableInsurance_radioButton)
        availableStatus = findViewById(R.id.AvailableInsurance_radioButton)

        var checkboxList = mutableListOf<CheckBox>()

        val itemCovered1: CheckBox = findViewById(R.id.itemCovered1_checkBox)
        checkboxList.add(itemCovered1)
        val itemCovered2: CheckBox = findViewById(R.id.itemCovered2_checkBox)
        checkboxList.add(itemCovered2)
        val itemCovered3: CheckBox = findViewById(R.id.itemCovered3_checkBox)
        checkboxList.add(itemCovered3)
        val itemCovered4: CheckBox = findViewById(R.id.itemCovered4_checkBox)
        checkboxList.add(itemCovered4)
        val itemCovered5: CheckBox = findViewById(R.id.itemCovered5_checkBox)
        checkboxList.add(itemCovered5)
        val itemCovered6: CheckBox = findViewById(R.id.itemCovered6_checkBox)
        checkboxList.add(itemCovered6)
        val itemCovered7: CheckBox = findViewById(R.id.itemCovered7_checkBox)
        checkboxList.add(itemCovered7)
        val itemCovered8: CheckBox = findViewById(R.id.itemCovered8_checkBox)
        checkboxList.add(itemCovered8)

        setCheckboxEventListener(checkboxList)
        val backBtn: Button = findViewById(R.id.back_button)
        val saveBtn: Button = findViewById(R.id.save_button)

        if (spinnerClass != null) {
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, insuranceClass)
            spinnerClass.adapter = adapter
        }

        insuranceName.setText(name)
        if (insClass == "Class A") {
            spinnerClass.setSelection(0)
            insClassDetail.text = "Class A - Cover Claim of Own Vehicle and Third Party Vehicle"
        } else {
            spinnerClass.setSelection(1)
            insClassDetail.text = "Class C - Only Covered Third Party Vehicle"
        }
        spinnerClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                insClassDetail.text = if(parent!!.getItemAtPosition(position).toString() == "Class A") "Class A - Cover Claim of Own Vehicle and Third Party Vehicle"
                else "Class C - Only Covered Third Party Vehicle"
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.i("Information","Nothing had been selected.")
            }
        }
        if (insStatus == "Available") {
            availableStatus.isChecked = true
        } else {
            unavailableStatus.isChecked = true
        }

        // Back Button
        backBtn.setOnClickListener {
            onBackPressed()
        }
        //Save Button
        saveBtn.setOnClickListener {
            insurance_name = insuranceName.text.toString().trim()
            insurance_class = spinnerClass.selectedItem.toString()

            if (insurance_name.isEmpty()) {
                insuranceName.error = "This field should not be blank."
                return@setOnClickListener
            }
            if(isValid(insurance_name){
                    Log.i("Information", "Input is Valid")
                })else{
                insuranceName.error = "Text entered contains special characters and digit is invalid. "
                return@setOnClickListener
            }
            insurance_status = if (availableStatus.isChecked) "Available" else "Unavailable"
            if (!itemCovered1.isChecked && !itemCovered2.isChecked && !itemCovered3.isChecked && !itemCovered4.isChecked
                && !itemCovered5.isChecked && !itemCovered6.isChecked && !itemCovered7.isChecked && !itemCovered8.isChecked
            ) {
                Toast.makeText(applicationContext, "Please select item covered(s).", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            saveToFirebaseDatabase()
        }

        //Get Data Item Covered from firebase
        var ref = instance.getReference("Item_Covered").child(insurance_uid!!).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(item in snapshot.children){
                        var itemCovered = item.getValue(DbItemCovered::class.java)
                        if(itemCovered !=null){
                            insuranceCovered.put(itemCovered.item_name,itemCovered)
                            updateCheckbox(checkboxList,itemCovered.item_name)
                        }
                    }
                }else{
                    Toast.makeText(applicationContext,"You might add some item covered accordingly.",Toast.LENGTH_SHORT).show()
                    //Toast.makeText(applicationContext,"No record(s).",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "An error has occurred.")
            }
        })
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

    private fun saveToFirebaseDatabase() {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@EditInsuranceCA)
        builder.setTitle("Confirmation Message")
        builder.setMessage("Are you sure to edit?")

        builder.setPositiveButton("Yes") { dialog, which ->
            val user_uid = FirebaseAuth.getInstance().uid        // -------------- Hard Code user_id
            database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            reference = database.getReference("User")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        if(data.child("user_uid").value.toString() == user_uid){
                            userCompany = data.child("ins_company_name").value.toString()
                            reference = database.getReference("Motor_Insurance")
                            var data = mutableMapOf<String,Any>("insurance_name" to insurance_name, "insurance_company" to userCompany,
                                "insurance_class" to insurance_class,"insurance_status" to insurance_status)
                            reference.child(insurance_uid!!).updateChildren(data).addOnSuccessListener {
                                saveToItemCoveredFirebase()
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("Cancel", error.toString())
                }
            })
        }
        builder.setNegativeButton("No") { dialog, which ->
            Log.i("Information", "No item(s) had been edited.")
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun saveToItemCoveredFirebase() {
        database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Item_Covered")

        var dataList = mutableMapOf<String,Any>()
        for(key in insuranceCovered.keys){
            var insuranceCovered = insuranceCovered[key]
            if(insuranceCovered!!.item_uid.isEmpty()){
                var itemUID = reference.push().key ?: ""
                var data = mutableMapOf<String,Any>(
                    "item_uid" to itemUID,
                    "item_name" to insuranceCovered!!.item_name,
                    "item_price" to insuranceCovered!!.item_price,
                    "item_status" to insuranceCovered!!.item_status
                )
                dataList[itemUID] = data
            }
            else{
                var data = mutableMapOf<String,Any>(
                    "item_uid" to insuranceCovered!!.item_uid,
                    "item_name" to insuranceCovered!!.item_name,
                    "item_price" to insuranceCovered!!.item_price,
                    "item_status" to insuranceCovered!!.item_status
                )
                dataList[insuranceCovered!!.item_uid] = data
            }
        }
        Log.i("Test","Update Size ${dataList.size}")
        reference.child(insurance_uid!!).setValue(dataList).addOnSuccessListener {
            //Toast.makeText(applicationContext, "Edit Insurance information successfully.", Toast.LENGTH_SHORT).show()
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Message")
            builder.setMessage("Edit Insurance Information successfully!")
            builder.setCancelable(true)
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()
        }
        return
    }

    private fun setCheckboxEventListener(checkboxList: MutableList<CheckBox>){
        for(checkbox in checkboxList){
            checkbox.setOnClickListener {
                var isChecked = checkbox.isChecked
                if(!isChecked){
                    if(insuranceCovered.containsKey(checkbox.tag as String)){
                        insuranceCovered.remove(checkbox.tag)
                    }
                    else{
                        Log.e("Error","Unable to find the insurance")
                    }
                }
                else{
                    var itemName = checkbox.tag as String
                    insuranceCovered[itemName] = createItemCovered(itemName)
                }
            }
        }
    }

    private fun createItemCovered(insuranceName: String): DbItemCovered {
        var itemName = insuranceName
        when(insuranceName){
            "All Drivers" -> {
                var price = "1900.00"
                var status= "Available"
                return DbItemCovered("",itemName,price,status)
            }
            "Bodily Injury" -> {
                var price = "2000.00"
                var status= "Available"
                return DbItemCovered("",itemName,price,status)
            }
            "Car Accessories" -> {
                var price = "1500.00"
                var status= "Available"
                return DbItemCovered("",itemName,price,status)
            }
            "Legal Costs" -> {
                var price = "1000.00"
                var status= "Available"
                return DbItemCovered("",itemName,price,status)
            }
            "Loss or Damage of Own Vehicle" -> {
                var price = "1200.00"
                var status= "Available"
                return DbItemCovered("",itemName,price,status)
            }
            "Loss or Damage of Property" -> {
                var price = "900.00"
                var status= "Available"
                return DbItemCovered("",itemName,price,status)
            }
            "Personal Accidental Death" -> {
                var price = "3000.00"
                var status= "Available"
                return DbItemCovered("",itemName,price,status)
            }
            "Wind Screen Cover" -> {
                var price = "399.00"
                var status= "Available"
                return DbItemCovered("",itemName,price,status)
            }
            else -> {
                var price = "999.99"
                var status = "Available"
                return DbItemCovered("",itemName,price,status)
            }
        }
    }

    private fun updateCheckbox(checkboxList:MutableList<CheckBox>,itemName:String){
        for(checkbox in checkboxList){
            if(itemName == checkbox.tag as String){
                checkbox.isChecked = true
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
