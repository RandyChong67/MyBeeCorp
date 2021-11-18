package com.example.mybeecorp.superadmin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class VehicleDetailActivity : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var textPlateNum: TextView
    private lateinit var textMemberName: TextView
    private lateinit var textInsuranceName: TextView
    private lateinit var textCompanyName: TextView
    private lateinit var textType: TextView
    private lateinit var textBrand: TextView
    private lateinit var textModel: TextView
    private lateinit var textVariant: TextView
    private lateinit var textYear: TextView
    private lateinit var buttonCancelSubscription: Button
    private lateinit var buttonReset: Button
    private var memberName: String = ""
    private var insuranceName: String = ""
    private var vehicleUID: String = ""
    private var memberUID: String = ""
    private var companyUID: String = ""
    private var insuranceBoughtUID: String = ""
    private var itemCoveredUID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_detail)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.title = "Vehicle Details"

        memberName = intent.getStringExtra("member_name")!!
        insuranceName = intent.getStringExtra("insurance_name")!!
        vehicleUID = intent.getStringExtra("vehicle_uid")!!
        insuranceBoughtUID = intent.getStringExtra("insurance_bought_uid")!!

        textMemberName = findViewById(R.id.text_member)
        textPlateNum = findViewById(R.id.text_car_plate)
        textInsuranceName = findViewById(R.id.text_insurance_name)
        textCompanyName = findViewById(R.id.text_insurance_company)

        textType = findViewById(R.id.spinner_type)
        textBrand = findViewById(R.id.spinner_brand)
        textModel = findViewById(R.id.spinner_model)
        textVariant = findViewById(R.id.spinner_variant)
        textYear = findViewById(R.id.spinner_year)

        buttonCancelSubscription = findViewById(R.id.button_cancel_subscription)
        buttonReset = findViewById(R.id.button_reset)

        textMemberName.text = memberName
        textInsuranceName.text = insuranceName

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        loadData()

        buttonCancelSubscription.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to cancel this Vehicle's Insurance Subscription?")
            builder.setTitle("Warning")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes") { dialog, _ ->
                dialog.dismiss()
                cancelSubscription()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()
        }
    }

    private fun cancelSubscription() {
        reference = database.getReference("Vehicle/$memberUID")
        val deleteVehicle = mapOf<String,String>(
            "vehicle_status" to "Unavailable"
        )
        reference.child(vehicleUID).updateChildren(deleteVehicle).addOnCompleteListener{
            if (it.isSuccessful){
                cancelInsuranceBought()
            }else{
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Failed to delete vehicle")
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

    private fun cancelInsuranceBought() {
        reference = database.getReference("Insurance_Bought/$memberUID/$companyUID")
        val deleteInsurance = mapOf<String,String>(
            "insurance_subscription_status" to "Unsubscribe"
        )
        reference.child(insuranceBoughtUID).updateChildren(deleteInsurance).addOnCompleteListener{
            if (it.isSuccessful){
//                cancelItemCovered()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("This vehicle's insurance subscription has been cancelled.")
                builder.setTitle("Cancel Successfully")
                builder.setCancelable(false)
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                val alert = builder.create()
                alert.show()
            }else{
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Failed to delete insurance bought")
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

//    private fun cancelItemCovered() {
//        reference = database.getReference("Insurance_Bought_Item_Covered/$memberUID")
//        val deleteInsurance = mapOf<String,String>(
//            "insurance_subscription_status" to "Unsubscribe"
//        )
//        reference.child(insuranceBoughtUID).updateChildren(deleteInsurance).addOnCompleteListener{
//            if (it.isSuccessful){
//                cancelItemCovered()
//            }else{
//                val builder = AlertDialog.Builder(this)
//                builder.setMessage("Failed to delete insurance bought")
//                builder.setTitle("Failed")
//                builder.setCancelable(false)
//                builder.setPositiveButton("OK") { dialog, _ ->
//                    dialog.dismiss()
//                }
//                val alert = builder.create()
//                alert.show()
//            }
//        }
//    }

    private fun loadData() {

        reference = database.getReference("Vehicle")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        for (i in data.children){
                            if (i.child("vehicle_uid").value.toString() == vehicleUID){
                                textPlateNum.text = i.child("vehicle_plate_num").value.toString()
                                textType.text = i.child("vehicle_type").value.toString()
                                textBrand.text = i.child("vehicle_brand").value.toString()
                                textModel.text = i.child("vehicle_model").value.toString()
                                textVariant.text = i.child("vehicle_variant").value.toString()
                                textYear.text = i.child("vehicle_year").value.toString()
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        reference = database.getReference("Motor_Insurance")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        if (data.child("insurance_name").value.toString() == insuranceName){
                            textCompanyName.text = data.child("insurance_company").value.toString()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        reference = database.getReference("User")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        if (data.child("user_full_name").value.toString() == memberName){
                            memberUID = data.child("user_uid").value.toString()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        reference = database.getReference("Insurance_Company")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        if (data.child("company_name").value.toString() == textCompanyName.text.toString()){
                            companyUID = data.child("company_uid").value.toString()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

//        reference = database.getReference("Insurance_Bought_Item_Covered/$insuranceBoughtUID")
//        reference.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    for (data in snapshot.children){
//                        if (data.child("user_full_name").value.toString() == memberName){
//                            memberUID = data.child("user_uid").value.toString()
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}