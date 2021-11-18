package com.example.mybeecorp.superadmin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Spin_History
import com.example.mybeecorp.superadmin.adapters.SpinWheelHistoryRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_prizes_history.*


class PrizesHistoryFragment : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private var spinHistoryList = mutableListOf<Spin_History>()
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var spinHistoryRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prizes_history_sa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        spinHistoryRV = view.findViewById(R.id.recycler_view_spin_history)

        verifyUserRole(view)
    }

    private fun verifyUserRole(view: View) {
        val uid = FirebaseAuth.getInstance().uid
        reference = database.getReference("User")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children){
                    if (item.child("user_uid").value.toString() == uid){
                        if (item.child("user_role").value.toString() == "Super Admin"){
                            readDatabase(view)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun readDatabase(view: View) {
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        reference = database.getReference("Spin_History")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                spinHistoryList.clear()
                for (data in snapshot.children) {
                    for (i in data.children){
                        if (i.child("spin_status").value.toString() == "Active"){
                            var model = i.getValue(Spin_History::class.java)
                            spinHistoryList.add(model as Spin_History)
                        }
                    }
                }
                if (spinHistoryList.size > 0) {
                    progressBar.isVisible = false
                    linearLayoutManager = LinearLayoutManager(view.context)
                    linearLayoutManager.reverseLayout = true
                    linearLayoutManager.stackFromEnd = true
                    spinHistoryRV.layoutManager = linearLayoutManager
                    spinHistoryRV.adapter =
                        SpinWheelHistoryRecyclerAdapter(spinHistoryList)
                }else{
                    spinHistoryList.clear()
                    linearLayoutManager = LinearLayoutManager(view.context)
                    linearLayoutManager.reverseLayout = true
                    linearLayoutManager.stackFromEnd = true
                    spinHistoryRV.layoutManager = linearLayoutManager
                    spinHistoryRV.adapter =
                        SpinWheelHistoryRecyclerAdapter(spinHistoryList)
                    progressBar.isVisible = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })

    }

}