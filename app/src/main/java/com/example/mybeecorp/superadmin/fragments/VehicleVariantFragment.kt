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
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Variant
import com.example.mybeecorp.superadmin.activities.AddVehicleModel
import com.example.mybeecorp.superadmin.activities.AddVehicleVariant
import com.example.mybeecorp.superadmin.adapters.VehicleVariantRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round

class VehicleVariantFragment : Fragment() {

    private var variantPositionList = mutableListOf<String>()
    private var variantList = mutableListOf<Variant>()
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var fabCreate: FloatingActionButton
    private lateinit var buttonFilter: Button
    private var filterModelList = mutableListOf<String>()
    private var filterBrandList = mutableListOf<String>()
    private lateinit var spinnerBrandFAB: Spinner
    private lateinit var spinnerModelFAB: Spinner
    private lateinit var filterSpinnerBrand: Spinner
    private lateinit var filterSpinnerModel: Spinner
    private lateinit var customLayoutInflater: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_variant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        buttonFilter = view.findViewById(R.id.button_filter)
        fabCreate = view.findViewById(R.id.fab_add)
        filterSpinnerBrand = view.findViewById(R.id.spinner_filter_brand)
        filterSpinnerModel = view.findViewById(R.id.spinner_filter_model)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        readDatabase(view)
        loadFilterSpinnerBrand(view, filterSpinnerBrand)

        filterSpinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                filterModelList.clear()
            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                reference = database.getReference("Model")
                reference.orderByChild("model_name")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            filterModelList.clear()
                            if (snapshot.exists()) {
                                for (data in snapshot.children) {
                                    if (data.child("brand_name").value.toString() == parent!!.getItemAtPosition(
                                            position
                                        ).toString()
                                    )
                                        filterModelList.add(data.child("model_name").value.toString())
                                }
                            }

                            val adapter = ArrayAdapter(
                                this@VehicleVariantFragment.requireContext(),
                                android.R.layout.simple_list_item_1, filterModelList
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                            filterSpinnerModel.adapter = adapter
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
            }
        }

        fabCreate.setOnClickListener {
            val intent = Intent(view.context, AddVehicleVariant::class.java)
            view.context.startActivity(intent)
        }

        buttonFilter.setOnClickListener {
            filterData(view)
        }


    }


    private fun loadFilterSpinnerBrand(view: View, filterSpinnerBrand: Spinner) {
        reference = database.getReference("Brand")
        reference.orderByChild("brand_name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                filterBrandList.clear()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        filterBrandList.add(data.child("brand_name").value.toString())
                    }
                }
                val adapter = ArrayAdapter(
                    view.context,
                    android.R.layout.simple_list_item_1, filterBrandList
                )

                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                filterSpinnerBrand.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    private fun filterData(view: View) {
        reference = database.getReference("Variant")
        reference.orderByChild("variant_name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                variantList.clear()
                variantPositionList.clear()
                var i = 1
                for (data in snapshot.children) {
                    if (filterSpinnerModel.selectedItemPosition != -1){
                        if (data.child("variant_status").value.toString() == "Available" &&
                            data.child("model_name").value.toString() == filterSpinnerModel.selectedItem.toString()
                        ) {
                            var model = data.getValue(Variant::class.java)
                            variantList.add(model as Variant)
                            variantPositionList.add("${i++}.")
                        }
                    }
                }
                val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
                progressBar.isVisible = false

                if (variantList.size > 0) {
                    val totalRecylceData = view.findViewById<TextView>(R.id.text_totalVariant)
                    totalRecylceData.text = "Total Variant of Vehicle: ${i - 1}"
                    val rv_recyclerView =
                        view.findViewById<RecyclerView>(R.id.recyclerView_vehicleVariant)
                    rv_recyclerView.layoutManager = LinearLayoutManager(view.context)
                    rv_recyclerView.adapter =
                        VehicleVariantRecyclerAdapter(variantPositionList, variantList)
                }else{
                    variantList.clear()
                    variantPositionList.clear()
                    val totalRecylceData = view.findViewById<TextView>(R.id.text_totalVariant)
                    totalRecylceData.text = "Total Variant of Vehicle: ${i - 1}"
                    val rv_recyclerView =
                        view.findViewById<RecyclerView>(R.id.recyclerView_vehicleVariant)
                    rv_recyclerView.layoutManager = LinearLayoutManager(view.context)
                    rv_recyclerView.adapter =
                        VehicleVariantRecyclerAdapter(variantPositionList, variantList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })
    }

    private fun readDatabase(view: View) {
        reference = database.getReference("Variant")
        reference.orderByChild("model_name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                variantList.clear()
                variantPositionList.clear()
                var i = 1
                for (data in snapshot.children) {
                    if (data.child("variant_status").value.toString() == "Available") {
                        var model = data.getValue(Variant::class.java)
                        variantList.add(model as Variant)
                        variantPositionList.add("${i++}.")
                    }
                }
                val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
                progressBar.isVisible = false

                if (variantList.size > 0) {
                    val totalRecylceData = view.findViewById<TextView>(R.id.text_totalVariant)
                    totalRecylceData.text = "Total Variant of Vehicle: ${i - 1}"
                    val rv_recyclerView =
                        view.findViewById<RecyclerView>(R.id.recyclerView_vehicleVariant)
                    rv_recyclerView.layoutManager = LinearLayoutManager(view.context)
                    rv_recyclerView.adapter =
                        VehicleVariantRecyclerAdapter(variantPositionList, variantList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })

    }

}