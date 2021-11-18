package com.example.mybeecorp.member.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.member.adapters.CompanyListRecyclerAdapter
import com.example.mybeecorp.member.adapters.MyInsuranceListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.com.customer.classes.Insurance_Bought
import my.com.customer.classes.Insurance_Company
import org.w3c.dom.Text

class MyInsurance_MainPage : AppCompatActivity() {

    private lateinit var buttonBuyInsurance: Button
    private lateinit var progressBar: ProgressDialog
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_insurance_main_page)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.title = "My Insurance"

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        progressBar = ProgressDialog(this)
        progressBar.setMessage("Loading...")
        progressBar.setCancelable(false)
        progressBar.show()

        buttonBuyInsurance = findViewById(R.id.BuyMoreInsurance_button)
        buttonBuyInsurance.setOnClickListener {
            val intent = Intent(this, InsuranceCompany_MainPage::class.java)
            startActivity(intent)
        }

        loadInsurance()
    }

    private fun loadInsurance() {

        val uid = FirebaseAuth.getInstance().uid
        reference = database.getReference("Insurance_Bought/$uid")
        var recyclerView = findViewById<RecyclerView>(R.id.recycler_view_my_insurance)
        var dataList = arrayListOf<Insurance_Bought>()
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { //Snapshot - return 回来的东西
                if (snapshot.exists()) {
                    dataList.clear()
                    for (item in snapshot.children) {
                        for (data in item.children){
                            if (data.child("insurance_subscription_status").value.toString() == "Active"){
                                var insuranceBought = data.getValue(Insurance_Bought::class.java)
                                dataList.add(insuranceBought!!)
                            }
                        }
                    }
                    if (dataList.size > 0) {
                        val textNoInsurance = findViewById<TextView>(R.id.text_no_insurance)
                        textNoInsurance.visibility = View.GONE
                        if (progressBar.isShowing) progressBar.dismiss()
                        recyclerView.layoutManager = LinearLayoutManager(this@MyInsurance_MainPage)
                        recyclerView.adapter = MyInsuranceListAdapter(dataList)
                    } else {
                        if (progressBar.isShowing) progressBar.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "No Insurance Bought. Go to buy Insurance now.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    if (progressBar.isShowing) progressBar.dismiss()
                    Toast.makeText(applicationContext, "No record.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (progressBar.isShowing) progressBar.dismiss()
                onBackPressed()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}