package com.example.mybeecorp.superadmin.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Type
import com.example.mybeecorp.superadmin.activities.AddVehicleType
import com.example.mybeecorp.superadmin.adapters.VehicleTypeRecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class VehicleTypeFragment : Fragment() {

    private var typePositionList = mutableListOf<String>()
    private var typeList = mutableListOf<Type>()
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var fabCreate: FloatingActionButton
    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)


        fabCreate = view.findViewById(R.id.fab_add)
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        readDatabase(view)
        fabCreate.setOnClickListener {

            val intent = Intent(view.context, AddVehicleType::class.java)
            view.context.startActivity(intent)
        }

    }

    private fun readDatabase(view: View) {
        reference = database.getReference("Type")
        val rv_recyclerView  = view.findViewById<RecyclerView>(R.id.recyclerView_vehicleType)
        reference.orderByChild("type_name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                typeList.clear()
                typePositionList.clear()
                var i = 1
                for(data in snapshot.children){
                    if (data.child("type_status").value == "Available"){
                        var model = data.getValue(Type::class.java)
                        typeList.add(model as Type)
                        typePositionList.add("${i++}.")
                    }
                }
                val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
                val totalRecylceData = view.findViewById<TextView>(R.id.text_totalType)
                totalRecylceData.text = "Total Type of Vehicle: ${i-1}"
                if(typeList.size > 0){

                    progressBar.isVisible = false

                    rv_recyclerView.layoutManager = LinearLayoutManager(view.context)
                    rv_recyclerView.adapter = VehicleTypeRecyclerAdapter(typePositionList,typeList)
                }else{
                    progressBar.isVisible = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })
    }
}