package com.example.mybeecorp.superadmin.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Model
import com.example.mybeecorp.superadmin.activities.AddVehicleModel
import com.example.mybeecorp.superadmin.activities.AddVehicleType
import com.example.mybeecorp.superadmin.adapters.VehicleModelRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class VehicleModelFragment : Fragment() {

    private var modelPositionList = mutableListOf<String>()
    private var modelList = mutableListOf<Model>()
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var fabCreate: FloatingActionButton
    private lateinit var buttonFilter: Button
    private lateinit var progressBar: ProgressDialog
    private var brandList = mutableListOf<String>()
    private var filterBrandList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_model, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonFilter = view.findViewById(R.id.button_filter)
        fabCreate = view.findViewById(R.id.fab_add)
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        readDatabase(view)
        fabCreate.setOnClickListener {
            val intent = Intent(view.context, AddVehicleModel::class.java)
            view.context.startActivity(intent)
        }
        buttonFilter.setOnClickListener {
            filterData(view)
        }
    }

    private fun filterData(view: View) {

        val spinnerBrand = view.findViewById<Spinner>(R.id.spinner_filter_brand)
        reference = database.getReference("Model")
        val rv_recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_vehicleModel)
        reference.orderByChild("model_name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                modelList.clear()
                modelPositionList.clear()
                var i = 1
                for (data in snapshot.children) {
                    if (data.child("brand_name").value == spinnerBrand.selectedItem.toString()){
                        var model = data.getValue(Model::class.java)
                        modelList.add(model as Model)
                        modelPositionList.add("${i++}.")
                    }

                }
                val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
                progressBar.isVisible = false

                val totalRecylceData = view.findViewById<TextView>(R.id.text_totalModel)
                totalRecylceData.text = "Total Model of Vehicle: ${i-1}"

                if (modelList.size > 0) {
                    rv_recyclerView.layoutManager = LinearLayoutManager(view.context)
                    rv_recyclerView.adapter = VehicleModelRecyclerAdapter(modelPositionList, modelList)
                }else{
                    modelList.clear()
                    modelPositionList.clear()
                    rv_recyclerView.layoutManager = LinearLayoutManager(view.context)
                    rv_recyclerView.adapter = VehicleModelRecyclerAdapter(modelPositionList, modelList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })
    }

    private fun readDatabase(view: View) {
        reference = database.getReference("Model")
        val rv_recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_vehicleModel)
        reference.orderByChild("brand_name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                modelList.clear()
                modelPositionList.clear()
                var i = 1
                for (data in snapshot.children) {
                    if(data.child("model_status").value.toString() == "Available"){
                        var model = data.getValue(Model::class.java)
                        modelList.add(model as Model)
                        modelPositionList.add("${i++}.")
                    }
                }
                val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
                progressBar.isVisible = false

                val totalRecylceData = view.findViewById<TextView>(R.id.text_totalModel)
                totalRecylceData.text = "Total Model of Vehicle: ${i-1}"

                if (modelList.size > 0) {
                    rv_recyclerView.layoutManager = LinearLayoutManager(view.context)
                    rv_recyclerView.adapter = VehicleModelRecyclerAdapter(modelPositionList, modelList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })

        reference = database.getReference("Brand")
        reference.orderByChild("brand_name").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                filterBrandList.clear()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        filterBrandList.add(data.child("brand_name").value.toString())
                    }
                }
                val spinner = view.findViewById<Spinner>(R.id.spinner_filter_brand)
                val adapter = ArrayAdapter(
                    view.context,
                    android.R.layout.simple_list_item_1, filterBrandList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                spinner.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}