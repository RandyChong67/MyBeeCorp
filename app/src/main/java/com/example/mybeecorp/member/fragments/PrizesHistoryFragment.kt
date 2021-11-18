package com.example.mybeecorp.member.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Spin_History
import com.example.mybeecorp.member.adapters.SpinWheelHistoryRecyclerAdapter
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
    private lateinit var userName: String
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prizes_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        userName = ""
        progressBar = view.findViewById(R.id.progress_bar)
        loadUserName()
        readDatabase(view)
    }

    private fun loadUserName() {
        val userUID = FirebaseAuth.getInstance().uid ?: ""
        reference = database.getReference("User")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    if (data.child("user_uid").value.toString() == userUID){
                        userName = data.child("user_full_name").value.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun readDatabase(view: View) {
        val userUID = FirebaseAuth.getInstance().uid
        reference = database.getReference("Spin_History/$userUID")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                spinHistoryList.clear()
                for (data in snapshot.children) {
                    if (data.child("spin_status").value.toString() == "Active"){
                        var model = data.getValue(Spin_History::class.java)
                        spinHistoryList.add(model as Spin_History)
                    }
                }

                if (spinHistoryList.size > 0) {
                    progressBar.isVisible = false
                    linearLayoutManager = LinearLayoutManager(view.context)
                    linearLayoutManager.reverseLayout = true
                    linearLayoutManager.stackFromEnd = true
                    recycler_view_spin_history.layoutManager = linearLayoutManager
                    recycler_view_spin_history.adapter = SpinWheelHistoryRecyclerAdapter(spinHistoryList)
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