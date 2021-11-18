package com.example.mybeecorp.insurancecompanyadmin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Vehicle
import com.example.mybeecorp.insurancecompanyadmin.adapters.ViewMemberVehicleRecyclerAdapter
import com.example.mybeecorp.superadmin.adapters.ViewVehicleRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.com.customer.classes.Insurance_Bought

class ViewMemberVehicle : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var vehicleList = mutableListOf<Vehicle>()
    private var insuranceList = mutableListOf<Insurance_Bought>()
    private lateinit var vehicleRecyclerView: RecyclerView
    private lateinit var textCompanyName: TextView
    private lateinit var textCompanyUID: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_member_vehicle)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.title = "Member Vehicle"

        vehicleRecyclerView = findViewById(R.id.recyclerViewVehicle)
        textCompanyName = findViewById(R.id.text_company_name)
        textCompanyUID = findViewById(R.id.text_company_uid)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        loadCompanyName()

    }

    private fun loadCompanyUID() {
        reference = database.getReference("Insurance_Company")
        reference.orderByChild("company_name").equalTo(textCompanyName.text.toString()).limitToFirst(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (data in snapshot.children){
                            if (data.child("company_name").value.toString() == textCompanyName.text.toString()){
                                textCompanyUID.text = data.child("company_uid").value.toString()
                            }
                        }
                    }

                    loadInsuranceData()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun loadCompanyName() {
        val uid = FirebaseAuth.getInstance().uid
        reference = database.getReference("User")
        reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (data in snapshot.children){
                            if (data.child("user_uid").value.toString() == uid){
                                textCompanyName.text = data.child("ins_company_name").value.toString()
                            }
                        }
                    }
                    loadCompanyUID()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



//
    }

    private fun loadVehicleData() {
        reference = database.getReference("Vehicle")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var count = 0
                    vehicleList.clear()
                    for (data in snapshot.children) {
                        for (i in data.children) {
                            if (count < insuranceList.size){
                                if (i.child("vehicle_uid").value.toString() == insuranceList[count].vehicle_uid){
                                    if (i.child("vehicle_status").value.toString() == "Available") {
                                        var vehicle = i.getValue(Vehicle::class.java)
                                        vehicleList.add(vehicle as Vehicle)
                                        count++
                                    }
                                }
                            }
                        }
                    }
//                    Log.d("Test1", "Hello")
//                    Log.d("Test2","$count")
                    val textTotalVehicle: TextView = findViewById(R.id.text_total_vehicle)
                    textTotalVehicle.text = "Total Vehicles: $count"
                    if (insuranceList.size > 0) {
                        val textNoVehicle = findViewById<TextView>(R.id.text_no_vehicle)
                        textNoVehicle.visibility = View.GONE
                        linearLayoutManager = LinearLayoutManager(this@ViewMemberVehicle)
                        vehicleRecyclerView.layoutManager = linearLayoutManager
                        vehicleRecyclerView.adapter =
                            ViewMemberVehicleRecyclerAdapter(vehicleList, insuranceList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun loadInsuranceData() {

        reference = database.getReference("Insurance_Bought")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    insuranceList.clear()
                    for (data in snapshot.children) {
                        for (i in data.children) {
                            if (i.key.toString() == textCompanyUID.text.toString()){
                                for (j in i.children){
                                    if (j.child("insurance_subscription_status").value.toString() == "Active") {
                                        if (j.child("insurance_company").value.toString() == textCompanyName.text.toString()){
                                            var insurance = j.getValue(Insurance_Bought::class.java)
                                            insuranceList.add(insurance as Insurance_Bought)
                                        }
                                    }
                                }

                            }
                        }
                    }
//
                }

                loadVehicleData()
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

