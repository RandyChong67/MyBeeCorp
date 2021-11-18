package com.example.mybeecorp.member.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.mybeecorp.R
import my.com.customer.classes.Item_Covered

class SelectItemCovered : AppCompatActivity() {

    private lateinit var insuranceCompanyNameText: TextView
    private lateinit var insuranceNameText: TextView
    private lateinit var insuranceClassText: TextView
    private lateinit var checkBoxItemCovered1: CheckBox
    private lateinit var checkBoxItemCovered2: CheckBox
    private lateinit var checkBoxItemCovered3: CheckBox
    private lateinit var checkBoxItemCovered4: CheckBox
    private lateinit var checkBoxItemCovered5: CheckBox
    private lateinit var checkBoxItemCovered6: CheckBox
    private lateinit var checkBoxItemCovered7: CheckBox
    private lateinit var checkBoxItemCovered8: CheckBox
    private lateinit var buttonBack: Button
    private lateinit var buttonConfirm: Button
    private var insuranceName: String = ""
    private var insuranceUID: String = ""
    private var insuranceClass: String = ""
    private var insuranceCompanyName: String = ""
    private var insuranceCompanyUID: String = ""
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var progressBar: ProgressBar
    private var itemCovered1: String = "No"
    private var itemCovered2: String = "No"
    private var itemCovered3: String = "No"
    private var itemCovered4: String = "No"
    private var itemCovered5: String = "No"
    private var itemCovered6: String = "No"
    private var itemCovered7: String = "No"
    private var itemCovered8: String = "No"
    private var itemCoveredList = mutableListOf<Item_Covered>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_item_covered)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Provide Insurance Information")

        insuranceCompanyNameText = findViewById(R.id.insuranceCompanyName_TextView)
        insuranceNameText = findViewById(R.id.insuranceName_TextView)
        insuranceClassText = findViewById(R.id.insuranceClass_textView)
        checkBoxItemCovered1 = findViewById(R.id.itemCovered1_checkBox)
        checkBoxItemCovered2 = findViewById(R.id.itemCovered2_checkBox)
        checkBoxItemCovered3 = findViewById(R.id.itemCovered3_checkBox)
        checkBoxItemCovered4 = findViewById(R.id.itemCovered4_checkBox)
        checkBoxItemCovered5 = findViewById(R.id.itemCovered5_checkBox)
        checkBoxItemCovered6 = findViewById(R.id.itemCovered6_checkBox)
        checkBoxItemCovered7 = findViewById(R.id.itemCovered7_checkBox)
        checkBoxItemCovered8 = findViewById(R.id.itemCovered8_checkBox)
        buttonConfirm = findViewById(R.id.ConfirmAndProceed_button)
        buttonBack = findViewById(R.id.back_button)
        progressBar = findViewById(R.id.progressBar)

        insuranceName = intent.getStringExtra("insurance_name")!!
        insuranceUID = intent.getStringExtra("insurance_uid")!!
        insuranceClass = intent.getStringExtra("insurance_class")!!
        insuranceCompanyName = intent.getStringExtra("company_name")!!
        insuranceCompanyUID = intent.getStringExtra("company_uid")!!

        insuranceNameText.setText(insuranceName)
        insuranceCompanyNameText.setText(insuranceCompanyName)
        insuranceClassText.text =
            if (insuranceClass == "Class A") "Cover Claim of Own Vehicle and Third Party Vehicle"
            else "Only Cover Third Party Vehicle"

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")


        loadItemCovered()

        buttonConfirm.setOnClickListener {
            proceedNextPage()
        }
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadItemCovered() {
        reference = database.getReference("Item_Covered/$insuranceUID")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    itemCoveredList.clear()
                    progressBar.visibility = View.GONE
                    var i = 0
                    for (data in snapshot.children) {
                        if (data.child("item_status").value.toString() == "Available") {
                            var model = data.getValue(Item_Covered::class.java)!!
                            itemCoveredList.add(model)
                            if (checkBoxItemCovered1.visibility == View.GONE) checkBoxItemCovered1.visibility =
                                if (itemCoveredList[i].item_name == checkBoxItemCovered1.text.toString()) View.VISIBLE else View.GONE
                            if (checkBoxItemCovered2.visibility == View.GONE) checkBoxItemCovered2.visibility =
                                if (itemCoveredList[i].item_name == checkBoxItemCovered2.text.toString()) View.VISIBLE else View.GONE
                            if (checkBoxItemCovered3.visibility == View.GONE) checkBoxItemCovered3.visibility =
                                if (itemCoveredList[i].item_name == checkBoxItemCovered3.text.toString()) View.VISIBLE else View.GONE
                            if (checkBoxItemCovered4.visibility == View.GONE) checkBoxItemCovered4.visibility =
                                if (itemCoveredList[i].item_name == checkBoxItemCovered4.text.toString()) View.VISIBLE else View.GONE
                            if (checkBoxItemCovered5.visibility == View.GONE) checkBoxItemCovered5.visibility =
                                if (itemCoveredList[i].item_name == checkBoxItemCovered5.text.toString()) View.VISIBLE else View.GONE
                            if (checkBoxItemCovered6.visibility == View.GONE) checkBoxItemCovered6.visibility =
                                if (itemCoveredList[i].item_name == checkBoxItemCovered6.text.toString()) View.VISIBLE else View.GONE
                            if (checkBoxItemCovered7.visibility == View.GONE) checkBoxItemCovered7.visibility =
                                if (itemCoveredList[i].item_name == checkBoxItemCovered7.text.toString()) View.VISIBLE else View.GONE
                            if (checkBoxItemCovered8.visibility == View.GONE) checkBoxItemCovered8.visibility =
                                if (itemCoveredList[i].item_name == checkBoxItemCovered8.text.toString()) View.VISIBLE else View.GONE
                            i++
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun proceedNextPage() {
        var itemCount = itemCoveredList.size

        if (checkBoxItemCovered1.isChecked && checkBoxItemCovered1.visibility == View.VISIBLE){
            itemCovered1 = checkBoxItemCovered1.text.toString()
            itemCount--
        }else{
            itemCovered1 = "No"
        }
        if (checkBoxItemCovered2.isChecked && checkBoxItemCovered1.visibility == View.VISIBLE){
            itemCovered2 = checkBoxItemCovered2.text.toString()
            itemCount--
        }else{
            itemCovered2 = "No"
        }
        if (checkBoxItemCovered3.isChecked && checkBoxItemCovered3.visibility == View.VISIBLE){
            itemCovered3 = checkBoxItemCovered3.text.toString()
            itemCount--
        }else{
            itemCovered3 = "No"
        }
        if (checkBoxItemCovered4.isChecked && checkBoxItemCovered4.visibility == View.VISIBLE){
            itemCovered4 = checkBoxItemCovered4.text.toString()
            itemCount--
        }else{
            itemCovered4 = "No"
        }
        if (checkBoxItemCovered5.isChecked && checkBoxItemCovered5.visibility == View.VISIBLE){
            itemCovered5 = checkBoxItemCovered5.text.toString()
            itemCount--
        }else{
            itemCovered5 = "No"
        }
        if (checkBoxItemCovered6.isChecked && checkBoxItemCovered6.visibility == View.VISIBLE){
            itemCovered6 = checkBoxItemCovered6.text.toString()
            itemCount--
        }else{
            itemCovered6 = "No"
        }
        if (checkBoxItemCovered7.isChecked && checkBoxItemCovered7.visibility == View.VISIBLE){
            itemCovered7 = checkBoxItemCovered7.text.toString()
            itemCount--
        }else{
            itemCovered7 = "No"
        }
        if (checkBoxItemCovered8.isChecked && checkBoxItemCovered8.visibility == View.VISIBLE){
            itemCovered8 = checkBoxItemCovered8.text.toString()
            itemCount--
        }else{
            itemCovered8 = "No"
        }

        if (itemCount == itemCoveredList.size) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Warning")
            builder.setMessage("Please choose at least ONE of the Items Covered!")
            builder.setCancelable(true)
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()
            return
        }

        val intent = Intent(this, FillInVehicleDetails::class.java)
        intent.putExtra("company_name", insuranceCompanyName)
        intent.putExtra("company_uid", insuranceCompanyUID)
        intent.putExtra("insurance_name", insuranceName)
        intent.putExtra("insurance_uid", insuranceUID)
        intent.putExtra("insurance_class", insuranceClass)
        intent.putExtra("item_covered_1", itemCovered1)
        intent.putExtra("item_covered_2", itemCovered2)
        intent.putExtra("item_covered_3", itemCovered3)
        intent.putExtra("item_covered_4", itemCovered4)
        intent.putExtra("item_covered_5", itemCovered5)
        intent.putExtra("item_covered_6", itemCovered6)
        intent.putExtra("item_covered_7", itemCovered7)
        intent.putExtra("item_covered_8", itemCovered8)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}