package com.example.mybeecorp.superadmin.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Brand
import com.example.mybeecorp.superadmin.activities.AddVehicleBrand
import com.example.mybeecorp.superadmin.adapters.VehicleBrandRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class VehicleBrandFragment : Fragment() {

    private var brandPositionList = mutableListOf<String>()
    private var brandList = mutableListOf<Brand>()
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var fabCreate: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_brand, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        fabCreate = view.findViewById(R.id.fab_add)
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        readDatabase(view)
        fabCreate.setOnClickListener {
            val intent = Intent(view.context, AddVehicleBrand::class.java)
            view.context.startActivity(intent)
        }
    }

    private fun readDatabase(view: View) {
        reference = database.getReference("Brand")
        val rv_recyclerView  = view.findViewById<RecyclerView>(R.id.recyclerView_vehicleBrand)
        reference.orderByChild("brand_name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                brandList.clear()
                brandPositionList.clear()
                var i = 1
                for(data in snapshot.children){
                    if (data.child("brand_status").value == "Available"){
                        var model = data.getValue(Brand::class.java)
                        brandList.add(model as Brand)
                        brandPositionList.add("${i++}.")
                    }
                }

                val totalRecylceData = view.findViewById<TextView>(R.id.text_totalBrand)
                totalRecylceData.text = "Total Brand of Vehicle: ${i-1}"

                if(brandList.size > 0){
                    val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
                    progressBar.isVisible = false

                    rv_recyclerView.layoutManager = LinearLayoutManager(view.context)
                    rv_recyclerView.adapter = VehicleBrandRecyclerAdapter(brandPositionList, brandList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }
        })
    }

}