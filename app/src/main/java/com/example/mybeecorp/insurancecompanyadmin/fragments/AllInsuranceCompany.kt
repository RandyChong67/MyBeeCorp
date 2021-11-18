package com.example.mybeecorp.insurancecompanyadmin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.mybeecorp.insurancecompanyadmin.adapters.RecyclerAdapter
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.DbInsCompany
import my.com.customer.classes.Insurance_Company

class AllInsuranceCompany : Fragment() {

    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    var instance = FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

    private lateinit var totalInsuranceCompany: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_insurance_company, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        totalInsuranceCompany = view.findViewById(R.id.totalInsuranceCompany_TextView)
        val sortButton = view.findViewById<ImageView>(R.id.sortByAlpha_icon)
        loadTotalInsCompany()

        sortButton.setOnClickListener{
            Log.i("Information", "Sort icon had been clicked.")
            val linearLayoutManager = LinearLayoutManager(view.context)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true
        }
        
        var companyDataList = arrayListOf<DbInsCompany>()
        var ref = instance.getReference("Insurance_Company").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    companyDataList.clear()
                    for(item in snapshot.children){
                        var insCompany = item.getValue(DbInsCompany::class.java)
                        companyDataList.add(insCompany!!)
                    }
                    if(companyDataList.size > 0){
                        val InsCompany_recyclerView = view.findViewById<RecyclerView>(R.id.InsCompany_recyclerView)
                        InsCompany_recyclerView.layoutManager = LinearLayoutManager(view.context)

                        InsCompany_recyclerView.adapter = RecyclerAdapter(companyDataList)
                    }else{
                        Log.i("Information","No Insurance Company Record(s)")
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "An error has occurred.")
            }
        })
    }

    private fun loadTotalInsCompany() {
        //database = Firebase.database("https://dbinscompanyadmin-default-rtdb.firebaseio.com/")
        database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Insurance_Company")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var noOfInsCompany = 0
                if(snapshot.exists()){
                    for(item in snapshot.children){
                        noOfInsCompany += 1
                    }
                    totalInsuranceCompany.text = noOfInsCompany.toString()
                }else{
                    Log.i("Information","No record(s).")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "An error occurs.")
            }
        })
    }
}
