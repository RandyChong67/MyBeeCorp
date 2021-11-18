package com.example.mybeecorp.superadmin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Vehicle
import com.example.mybeecorp.superadmin.adapters.ViewVehicleRecyclerAdapter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.com.customer.classes.Insurance_Bought
import org.w3c.dom.Text

class ViewVehicleActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var vehicleList = mutableListOf<Vehicle>()
    private var insuranceList = mutableListOf<Insurance_Bought>()
    private lateinit var vehicleRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_vehicle)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setTitle("View Vehicle")

        vehicleRecyclerView = findViewById(R.id.recyclerViewVehicle)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        loadVehicleData()

    }

    private fun loadVehicleData() {
        reference = database.getReference("Vehicle")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var count = 0
                    vehicleList.clear()
                    for (data in snapshot.children){
                        for (i in data.children){
                            if (i.child("vehicle_status").value.toString() == "Available"){
                                var vehicle = i.getValue(Vehicle::class.java)
                                vehicleList.add(vehicle as Vehicle)
                                count++
                            }
                        }
                    }
                    val textTotalVehicle: TextView = findViewById(R.id.text_total_vehicle)
                    textTotalVehicle.text = "Total Vehicles: $count"
                    loadInsuranceData()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun loadInsuranceData() {
        reference = database.getReference("Insurance_Bought")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    insuranceList.clear()
                    for (data in snapshot.children){
                        for (i in data.children){
                            for (j in i.children){
                                if (j.child("insurance_subscription_status").value.toString() == "Active"){
                                    var insurance = j.getValue(Insurance_Bought::class.java)
                                    insuranceList.add(insurance as Insurance_Bought)
                                }
                            }
                        }
                    }
                }
                if (insuranceList.size > 0) {
                    val textNoVehicle = findViewById<TextView>(R.id.text_no_vehicle)
                    textNoVehicle.visibility = View.GONE
                    linearLayoutManager = LinearLayoutManager(this@ViewVehicleActivity)
                    vehicleRecyclerView.layoutManager = linearLayoutManager
                    vehicleRecyclerView.adapter =
                        ViewVehicleRecyclerAdapter(vehicleList, insuranceList)
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