package com.example.mybeecorp.insurancecompanyadmin.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.example.mybeecorp.superadmin.fragments.userListAdapter
import com.example.yangloosuperadmin.classes.UserList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class InsuranceCompanyAdminMemberList : AppCompatActivity() {
    private lateinit var userListRecyclerView: RecyclerView
    private lateinit var userListArrayList : ArrayList<UserList>
    var insuranceList = ArrayList<String>()
    var memberList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_company_admin_member_list)
        this.supportActionBar?.title = "Member List"
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userID = FirebaseAuth.getInstance().uid
        //val userID = "usr10005"

        val memberSearchET = findViewById<EditText>(R.id.memberList_Search)
        val memberSearch = memberSearchET.text.toString()

        userListRecyclerView = findViewById(R.id.userList_recycle)
        userListRecyclerView.layoutManager = LinearLayoutManager(this)
        userListRecyclerView.setHasFixedSize(true)

        userListArrayList = arrayListOf<UserList>()
        getFirebaseCompanyName(userID!!, memberSearch)

        memberSearchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val memberSearch = memberSearchET.text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val memberSearch = memberSearchET.text.toString()
                getFirebaseCompanyName(userID, memberSearch)

            }

            override fun afterTextChanged(s: Editable?) {
                val memberSearch = memberSearchET.text.toString()
            }

        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun getFirebaseCompanyName(userID:String, memberSearch:String){
        /*val companyRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")*/
        var companyRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        companyRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        if (i.child("user_uid").value == userID) {
                            if(i.child("ins_company_name").value.toString() == "" || i.child("ins_company_name").value.toString() == "No Company"){
                                Toast.makeText(applicationContext, "Please register a company!!", Toast.LENGTH_LONG)
                                    .show()
                                return
                            }else{
                                val companyName = i.child("ins_company_name").value.toString()
                                getFirebaseCompany(memberSearch, companyName)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun getFirebaseCompany(memberSearch:String, companyName:String){
        var companyUidRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Insurance_Company")

        companyUidRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        if(i.child("company_name").value.toString() == companyName){
                            val companyUid = i.child("company_uid").value.toString()
                            getFirebaseMemberList(memberSearch, companyUid)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }

        })
    }

    private fun getFirebaseMemberList(memberSearch:String, companyUid:String){
        val insuranceBoughtRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Insurance_Bought")

        insuranceBoughtRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        for(j in i.children){
                            if(j.key.toString() == companyUid){
                                for(k in j.children) {
                                    memberList.add(k.child("user_uid").value.toString())
                                }
                            }
                        }
                    }
                    getMemberListData(memberSearch, memberList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    /*private fun getFirebaseInsurance(memberSearch:String, companyName:String){
        /*val insuranceRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Motor_Insurance")*/
        val insuranceRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Motor_Insurance")

        insuranceRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        if (i.child("insurance_company").value.toString() == companyName) {
                            insuranceList.add(i.child("insurance_uid").value.toString())
                        }
                    }
                    getFirebaseMember(memberSearch, insuranceList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }*/

    /*private fun getFirebaseMember(memberSearch: String, insuranceList:ArrayList<String>){
        /*val insuranceBoughtRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Insurance_Bought")*/
        val insuranceBoughtRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Insurance_Bought")

        insuranceBoughtRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        for(j in insuranceList){
                            if(i.child("insurance_uid").value.toString() == j){
                                memberList.add(i.child("user_id").value.toString())
                            }
                        }
                    }
                    getMemberListData(memberSearch, memberList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }*/

    private fun getMemberListData(memberSearch:String, memberList:ArrayList<String>){
        /*val memberListRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")*/
        val memberListRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        val memberSet = HashSet<String>(memberList)

        if(memberSearch == "") {
            memberListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userListArrayList.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            for(j in memberSet){
                                if(i.child("user_uid").value.toString() == j && i.child("user_role").value.toString() == "Member"){
                                    val loadMemberList = i.getValue(UserList::class.java)
                                    userListArrayList.add(loadMemberList!!)
                                }
                            }
                        }

                        userListRecyclerView.adapter = userListAdapter(userListArrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
        else{
            memberListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userListArrayList.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            for(j in memberSet){
                                val searchName = i.child("user_full_name").value.toString()
                                if(i.child("user_uid").value.toString() == j && i.child("user_role").value.toString() == "Member" && searchName.contains(memberSearch, ignoreCase = true)){
                                    var loadMemberList = i.getValue(UserList::class.java)
                                    userListArrayList.add(loadMemberList!!)
                                }
                            }
                        }
                        userListRecyclerView.adapter = userListAdapter(userListArrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
    }
}