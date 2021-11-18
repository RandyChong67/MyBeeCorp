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
import com.example.mybeecorp.classes.reward
import com.example.mybeecorp.superadmin.adapters.rewardAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class View_Reward : AppCompatActivity() {
    private lateinit var rewardListRef : DatabaseReference
    private lateinit var rewardListRecyclerView: RecyclerView
    private lateinit var rewardListArrayList : ArrayList<reward>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reward)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("View Reward")

        val rewardSearchET = findViewById<EditText>(R.id.searchReward_editText)
        val rewardSearch = rewardSearchET.text.toString()

        rewardListRecyclerView = findViewById(R.id.rewardList_rv)
        rewardListRecyclerView.layoutManager = LinearLayoutManager(this)
        rewardListRecyclerView.setHasFixedSize(true)

        rewardListArrayList = arrayListOf<reward>()
        getRewardListData(rewardSearch)

        rewardSearchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val rewardSearch = rewardSearchET.text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val rewardSearch = rewardSearchET.text.toString()
                getRewardListData(rewardSearch)

            }

            override fun afterTextChanged(s: Editable?) {
                val rewardSearch = rewardSearchET.text.toString()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getRewardListData(rewardSearch:String){

        rewardListRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Reward")

        if(rewardSearch == "") {
            rewardListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    rewardListArrayList.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                                val rewardList = i.getValue(reward::class.java)
                                rewardListArrayList.add(rewardList!!)
                        }
                        rewardListRecyclerView.adapter = rewardAdapter(rewardListArrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
        else{
            rewardListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    rewardListArrayList.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            var searchName = i.child("reward_Name").value.toString()
                            if (searchName.contains(rewardSearch, ignoreCase = true)) {
                                val rewardList = i.getValue(reward::class.java)
                                rewardListArrayList.add(rewardList!!)
                            }
                        }

                        rewardListRecyclerView.adapter = rewardAdapter(rewardListArrayList)
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


