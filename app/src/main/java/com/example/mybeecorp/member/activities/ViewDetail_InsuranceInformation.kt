package com.example.mybeecorp.member.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.member.adapters.MyInsuranceItemCoveredListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.com.customer.classes.Insurance_Bought
import my.com.customer.classes.Insurance_Bought_Item_Covered

class ViewDetail_InsuranceInformation : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var textInsuranceUID: TextView
    private lateinit var textInsuranceName: TextView
    private lateinit var textCompanyName: TextView
    private lateinit var textPlateNumber: TextView
    private lateinit var textInsuranceStatus: TextView
    private lateinit var textYearlyPayment: TextView
    private lateinit var textDateOfBought: TextView
    private lateinit var textInsuranceClass: TextView
    private lateinit var recyclerViewItemCovered: RecyclerView
    private var insuranceList = mutableListOf<Insurance_Bought>()
    private var itemCoveredList = mutableListOf<Insurance_Bought_Item_Covered>()
    private var insuranceBoughtUID = ""
    private var insuranceName = ""
    private var companyName = ""
    private var plateNum = ""
    private var vehicleUID = ""
    private var insuranceStatus = ""
    private var yearlyPayment = ""
    private var dateOfBought = ""
    private var insuranceClass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_detail_insurance_information)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.title = "Details Insurance Information"

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        insuranceBoughtUID = intent.getStringExtra("insurance_bought_uid")!!
        textInsuranceUID = findViewById(R.id.insuranceID_TextView)
        textInsuranceName = findViewById(R.id.insuranceName_TextView)
        textCompanyName = findViewById(R.id.insCompanyName_TextView)
        textPlateNumber = findViewById(R.id.vehiclePlateNum_TextView)
        textInsuranceStatus = findViewById(R.id.insSubscriptionStatus_TextView)
        textYearlyPayment = findViewById(R.id.yearlyPayment_TextView)
        textDateOfBought = findViewById(R.id.dateOfInsuranceBought_TextView)
        textInsuranceClass = findViewById(R.id.text_insurance_class)
        recyclerViewItemCovered = findViewById(R.id.recycler_view_item_covered)

        loadInsuranceData()
        loadItemCoveredData()

    }

    private fun loadItemCoveredData() {
        reference = database.getReference("Insurance_Bought_Item_Covered/$insuranceBoughtUID")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                itemCoveredList.clear()
                for (data in snapshot.children){
                    if (snapshot.exists()){
                        var itemCovered = data.getValue(Insurance_Bought_Item_Covered::class.java)
                        itemCoveredList.add(itemCovered!!)
                    }
                }
                if (itemCoveredList.size > 0) {
                    recyclerViewItemCovered.layoutManager = LinearLayoutManager(this@ViewDetail_InsuranceInformation)
                    recyclerViewItemCovered.adapter = MyInsuranceItemCoveredListAdapter(itemCoveredList)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "No Insurance Bought. Go to buy Insurance now.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Log","An error occurs.")
            }
        })
    }

    private fun loadInsuranceData() {
        val userUID = FirebaseAuth.getInstance().uid?:""
        reference = database.getReference("Insurance_Bought/$userUID")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        for (item in data.children){
                            if (item.child("ins_bought_uid").value.toString() == insuranceBoughtUID){
                                textInsuranceUID.text = item.child("ins_bought_uid").value.toString()
                                textInsuranceName.text = item.child("insurance_name").value.toString()
                                vehicleUID = item.child("vehicle_uid").value.toString()
                                textYearlyPayment.text = String.format("RM %.2f",item.child("yearly_payment").value.toString().toLong().toDouble())
                                textDateOfBought.text = item.child("date_of_bought").value.toString()
                                textInsuranceStatus.text = item.child("insurance_subscription_status").value.toString()

                                insuranceName = item.child("insurance_name").value.toString()
                            }
                        }
                    }
                }
                loadVehiclePlateNum(vehicleUID)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Log","An error occurs.")
            }
        })

        reference = database.getReference("Motor_Insurance")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    if (snapshot.exists()){
                        if (data.child("insurance_name").value.toString() == insuranceName){
                            textCompanyName.text = data.child("insurance_company").value.toString()
                            companyName = data.child("insurance_company").value.toString()
                            textInsuranceClass.text = data.child("insurance_class").value.toString()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Log","An error occurs.")
            }

        })
    }

    private fun loadVehiclePlateNum(vehicleUID: String) {
        val uid = FirebaseAuth.getInstance().uid
        reference = database.getReference("Vehicle/$uid")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    if (snapshot.exists()){
                        if (data.child("vehicle_uid").value.toString() == vehicleUID){
                            textPlateNumber.text = data.child("vehicle_plate_num").value.toString()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Log","An error occurs.")
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}