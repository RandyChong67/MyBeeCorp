package com.example.mybeecorp.member.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.member.adapters.MemberInsuranceListAdapter
import com.example.yangloosuperadmin.classes.MemberInsuranceList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MemberReferralList : AppCompatActivity() {

    private lateinit var insuranceListRef : DatabaseReference
    private lateinit var insuranceListRecyclerView: RecyclerView
    private lateinit var insuranceListArrayList : ArrayList<MemberInsuranceList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_referral_list)
        this.supportActionBar?.title = "My Referral"
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        insuranceListRecyclerView = findViewById(R.id.insuranceBought_recycle)
        insuranceListRecyclerView.layoutManager = LinearLayoutManager(this)
        insuranceListRecyclerView.setHasFixedSize(true)

        insuranceListArrayList = arrayListOf<MemberInsuranceList>()

        val insSearchET = findViewById<EditText>(R.id.insList_Search)
        var insSearch = insSearchET.text.toString()

        getAvailableCompany(insSearch)
        //getAllInsuranceListData(insSearch)

        insSearchET.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val insSearch = insSearchET.text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val insSearch = insSearchET.text.toString()
                getAvailableCompany(insSearch)
            }

            override fun afterTextChanged(s: Editable?) {
                val insSearch = insSearchET.text.toString()
            }

        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getAvailableCompany(insSearch:String){
        var companyListRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Insurance_Company")
        var companyList = ArrayList<String>()

        companyListRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                companyList.clear()
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        if(i.child("company_status").value.toString() == "Available"){
                            companyList.add(i.child("company_name").value.toString())
                        }
                    }
                    getAllInsuranceListData(insSearch, companyList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun getAllInsuranceListData(insSearch:String, companyList:ArrayList<String>){
        /*insuranceListRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Motor_Insurance")*/
        insuranceListRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Motor_Insurance")

        val companySet = HashSet<String>(companyList)

        if(insSearch == "") {
            insuranceListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    insuranceListArrayList.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            for(j in companySet){
                                if(i.child("insurance_status").value.toString() == "Available" && i.child("insurance_company").value.toString() == j) {
                                    var insuranceUID = i.child("insurance_uid").value.toString()
                                    var insuranceName = i.child("insurance_name").value.toString()
                                    var insuranceCompany = i.child("insurance_company").value.toString()
                                    var insurance = MemberInsuranceList(
                                        insuranceUID,
                                        insuranceName,
                                        insuranceCompany,
                                        0
                                    )
                                    insuranceListArrayList.add(insurance)
                                }
                            }
                        }
                    }
                    getReferralCount(insuranceListArrayList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
        else{
            insuranceListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    insuranceListArrayList.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            for(j in companySet){
                                var searchIns = i.child("insurance_name").value.toString()
                                if(i.child("insurance_status").value.toString() == "Available" && searchIns.contains(insSearch, ignoreCase = true) && i.child("insurance_company").value.toString() == j) {
                                    var insuranceUID = i.child("insurance_uid").value.toString()
                                    var insuranceName = i.child("insurance_name").value.toString()
                                    var insuranceCompany = i.child("insurance_company").value.toString()
                                    var insurance = MemberInsuranceList(
                                        insuranceUID,
                                        insuranceName,
                                        insuranceCompany,
                                        0
                                    )
                                    insuranceListArrayList.add(insurance)
                                }
                            }
                        }
                    }
                    getReferralCount(insuranceListArrayList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
    }

    private fun getReferralCount(insuranceListArrayList:ArrayList<MemberInsuranceList>){
        /*val referralCountRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Referral")*/
        val referralCountRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Referral")

        val userID = FirebaseAuth.getInstance().uid
        //val userID = "usr10007"

        referralCountRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val insuranceList = insuranceListArrayList
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        for(j in insuranceList){
                            if(i.child("insurance_recommended").value.toString() == j.insurance_uid.toString() && i.child("inviter_uid").value.toString() == userID){
                                j.referralCount+=1
                            }
                        }
                    }
                    insuranceListRecyclerView.adapter = MemberInsuranceListAdapter(insuranceList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}