package com.example.mybeecorp.superadmin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.superadmin.fragments.userListAdapter
import com.example.yangloosuperadmin.classes.UserList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SuperAdminUserList : AppCompatActivity() {
    private lateinit var userListRef : DatabaseReference
    private lateinit var userListRecyclerView: RecyclerView
    private lateinit var userListArrayList : ArrayList<UserList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_super_admin_user_list)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val rolePassed = intent.getStringExtra("userRole").toString()
        if(rolePassed == "Super Admin"){
            this.supportActionBar?.title = "Super Admin"
        }else if(rolePassed == "Insurance Company Admin"){
            this.supportActionBar?.title = "Insurance Company Admin"
        }else{
            this.supportActionBar?.title = "Member"
        }
        val userSearchET = findViewById<EditText>(R.id.userList_Search)
        val userSearch = userSearchET.text.toString()

        userListRecyclerView = findViewById(R.id.userList_recycle)
        userListRecyclerView.layoutManager = LinearLayoutManager(this)
        userListRecyclerView.setHasFixedSize(true)

        userListArrayList = arrayListOf<UserList>()
        getUserListData(rolePassed, userSearch)

        userSearchET.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val userSearch = userSearchET.text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val userSearch = userSearchET.text.toString()
                getUserListData(rolePassed, userSearch)

            }

            override fun afterTextChanged(s: Editable?) {
                val userSearch = userSearchET.text.toString()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getUserListData(userRole:String, userSearch:String){
        /*userListRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")*/
        userListRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        if(userSearch == "") {
            userListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userListArrayList.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            if (i.child("user_role").value == userRole) {
                                val userList = i.getValue(UserList::class.java)
                                userListArrayList.add(userList!!)
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
            userListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userListArrayList.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            var searchName = i.child("user_full_name").value.toString()
                            if (i.child("user_role").value == userRole && searchName.contains(userSearch, ignoreCase = true)) {
                                val userList = i.getValue(UserList::class.java)
                                userListArrayList.add(userList!!)
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


