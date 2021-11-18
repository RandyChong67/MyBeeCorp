package com.example.mybeecorp.superadmin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.superadmin.fragments.userRoleListAdapter
import com.example.yangloosuperadmin.classes.UserRole
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SuperAdminUserRoleList : AppCompatActivity() {
    private lateinit var userRoleRef : DatabaseReference
    private lateinit var userRoleRecyclerView: RecyclerView
    private lateinit var userRoleArrayList : ArrayList<UserRole>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_users_super_admin)
        this.supportActionBar?.title = "User"
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userRoleRecyclerView = findViewById(R.id.userRoleList_recycle)
        userRoleRecyclerView.layoutManager = LinearLayoutManager(this)
        userRoleRecyclerView.setHasFixedSize(true)

        userRoleArrayList = arrayListOf<UserRole>()
        getUserRoleData()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun getUserRoleData(){
        /*userRoleRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")*/
        userRoleRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        userRoleRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userRoleArrayList.clear()
                var superAdminCount : Int=0
                var insCompanyAdminCount : Int=0
                var memberCount : Int=0
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        if(i.child("user_role").value == "Super Admin"){
                            superAdminCount += 1
                        }
                        else if(i.child("user_role").value == "Insurance Company Admin"){
                            insCompanyAdminCount += 1
                        }
                        else if(i.child("user_role").value == "Member"){
                            memberCount += 1
                        }
                    }

                    var userRole = UserRole("Super Admin", "Total User(s): $superAdminCount")
                    userRoleArrayList.add(userRole)
                    userRole = UserRole("Insurance Company Admin", "Total User(s): $insCompanyAdminCount")
                    userRoleArrayList.add(userRole)
                    userRole = UserRole("Member", "Total User(s): $memberCount")
                    userRoleArrayList.add(userRole)

                    userRoleRecyclerView.adapter = userRoleListAdapter(userRoleArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}