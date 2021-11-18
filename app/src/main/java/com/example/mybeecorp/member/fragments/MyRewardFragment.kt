package com.example.rewardmodule_member.member

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.reward_history
import com.example.rewardmodule_member.member.adapters.rewardEarnedAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyRewardFragment : Fragment() {

    private lateinit var rewardEarnedRef : DatabaseReference
    private lateinit var rewardEarnedRecyclerView: RecyclerView
    private lateinit var rewardEarnedArrayList : ArrayList<reward_history>

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_reward, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val userID = FirebaseAuth.getInstance().uid //login user's id

        rewardEarnedRecyclerView = view.findViewById(R.id.myReward_rv)
        rewardEarnedRecyclerView.layoutManager = LinearLayoutManager(view.context)
        rewardEarnedRecyclerView.setHasFixedSize(true)

        rewardEarnedArrayList = arrayListOf<reward_history>()

        rewardEarnedRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Reward_Earned/$userID")

        rewardEarnedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rewardEarnedArrayList.clear()
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val rewardHistory = i.getValue(reward_history::class.java)
                        rewardEarnedArrayList.add(rewardHistory!!)
                    }
                    rewardEarnedRecyclerView.adapter = rewardEarnedAdapter(rewardEarnedArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(view.context, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}