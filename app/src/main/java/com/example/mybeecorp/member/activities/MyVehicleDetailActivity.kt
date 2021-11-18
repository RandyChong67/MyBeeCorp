package com.example.mybeecorp.member.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MyVehicleDetailActivity : AppCompatActivity() {

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
    private lateinit var textValidDate: TextView
    private lateinit var buttonCancelSubscription: Button
    private lateinit var buttonReset: Button
    private var vehicleUID: String = ""
    private var insuranceBoughtUID: String = ""
    private var companyUID: String = ""
    private lateinit var validDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_vehicle_detail)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.title = "Vehicle Details"

        vehicleUID = intent.getStringExtra("vehicle_uid")!!
        insuranceBoughtUID = intent.getStringExtra("insurance_bought_uid")!!

        textPlateNum = findViewById(R.id.text_car_plate)
        textInsuranceName = findViewById(R.id.text_insurance_name)
        textCompanyName = findViewById(R.id.text_insurance_company)
        textValidDate = findViewById(R.id.text_valid_date)

        textType = findViewById(R.id.spinner_type)
        textBrand = findViewById(R.id.spinner_brand)
        textModel = findViewById(R.id.spinner_model)
        textVariant = findViewById(R.id.spinner_variant)
        textYear = findViewById(R.id.spinner_year)

        buttonCancelSubscription = findViewById(R.id.button_cancel_subscription)
        buttonReset = findViewById(R.id.button_reset)

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
        val uid = FirebaseAuth.getInstance().uid
        reference = database.getReference("Vehicle/$uid")
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
        val uid = FirebaseAuth.getInstance().uid
        reference = database.getReference("Insurance_Bought/$uid/$companyUID")
        val deleteInsurance = mapOf<String,String>(
            "insurance_subscription_status" to "Unsubscribe"
        )
        reference.child(insuranceBoughtUID).updateChildren(deleteInsurance).addOnCompleteListener{
            if (it.isSuccessful){
//                cancelItemCovered()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("This Vehicle's Insurance Subscription has been CANCELLED.")
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

        val uid = FirebaseAuth.getInstance().uid
        reference = database.getReference("Vehicle/$uid")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        if (data.child("vehicle_uid").value.toString() == vehicleUID){
                            textPlateNum.text = data.child("vehicle_plate_num").value.toString()
                            textType.text = data.child("vehicle_type").value.toString()
                            textBrand.text = data.child("vehicle_brand").value.toString()
                            textModel.text = data.child("vehicle_model").value.toString()
                            textVariant.text = data.child("vehicle_variant").value.toString()
                            textYear.text = data.child("vehicle_year").value.toString()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        reference = database.getReference("Insurance_Bought/$uid")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        for (i in data.children){
                            if (i.child("ins_bought_uid").value.toString() == insuranceBoughtUID){
                                textInsuranceName.text = i.child("insurance_name").value.toString()
                                textCompanyName.text = i.child("insurance_company").value.toString()
                                textValidDate.text = "Subscription from: ${i.child("date_of_bought").value.toString()}"
                            }
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

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}