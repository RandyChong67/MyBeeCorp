package com.example.rewardmodule_member.member

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.reward
import com.example.rewardmodule_member.member.adapters.rewardListAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
//chunyang
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RewardListFragment : Fragment() {

    private lateinit var rewardListRef : DatabaseReference
    private lateinit var rewardListRecyclerView: RecyclerView
    private lateinit var rewardListArrayList : ArrayList<reward>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reward_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val userID = FirebaseAuth.getInstance().uid

        rewardListRecyclerView = view.findViewById(R.id.myReward_rv)
        rewardListRecyclerView.layoutManager = LinearLayoutManager(view.context)
        rewardListRecyclerView.setHasFixedSize(true)

        rewardListArrayList = arrayListOf<reward>()

        rewardListRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Reward")

        rewardListRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rewardListArrayList.clear()
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val rewardList = i.getValue(reward::class.java)
                        rewardListArrayList.add(rewardList!!)
                    }
                    rewardListRecyclerView.adapter = rewardListAdapter(rewardListArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(view.context, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
        loadUser(userID!!)
    }

    private fun loadUser(userID:String){

        val userName = view?.findViewById<TextView>(R.id.userName_textview)
        val userPointAwarded = view?.findViewById<TextView>(R.id.pointAwarded_textview)

        var rewardRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        rewardRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        if(i.child("user_uid").value.toString() == userID){
                            if (userName != null) {
                                userName.setText(i.child("user_full_name").value.toString())
                            }
                            if (userPointAwarded != null) {
                                userPointAwarded.setText(i.child("point_awarded").value.toString())
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(view?.context, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}