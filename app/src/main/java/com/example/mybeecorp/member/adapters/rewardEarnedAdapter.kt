package com.example.rewardmodule_member.member.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.reward_history
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList


class rewardEarnedAdapter(private val reward_history: ArrayList<reward_history>)  :
    RecyclerView.Adapter<rewardEarnedAdapter.rewardEarnedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rewardEarnedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rewardearned_cardview, parent, false)
        return rewardEarnedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: rewardEarnedViewHolder, position: Int) {
        val currentItem = reward_history[position]

        holder.ori_reward_UID.text = currentItem.ori_reward_uid
        holder.earned_reward_UID.text = currentItem.reward_earned_uid
        holder.earned_reward_Name.text = currentItem.claimed_rewardname
        holder.earned_reward_Point.text = currentItem.claimed_rewardpoint.toString()

        var rewardRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Reward")

        rewardRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        if(i.child("reward_uid").value.toString() == holder.ori_reward_UID.text){
                            //if(!this@rewardEarnedAdapter.isFinishing) {
                                Glide.with(holder.earned_reward_Image.context).load(i.child("reward_image").value).into(holder.earned_reward_Image)
                           // }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                /*Toast.makeText(view.context, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()*/
            }
        })
    }

    override fun getItemCount(): Int {
        return reward_history.size
    }

    class rewardEarnedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ori_reward_UID: TextView = itemView.findViewById(R.id.ori_reward_uid)
        val earned_reward_UID: TextView = itemView.findViewById(R.id.earned_reward_uid)
        val earned_reward_Image: CircleImageView = itemView.findViewById(R.id.earned_reward_img)
        val earned_reward_Name: TextView = itemView.findViewById(R.id.earned_reward_name)
        val earned_reward_Point: TextView = itemView.findViewById(R.id.earned_reward_point)
        }
    }
