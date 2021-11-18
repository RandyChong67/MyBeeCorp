package com.example.rewardmodule_member.member.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.reward
import com.example.mybeecorp.member.activities.claimReward
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class rewardListAdapter (private val rewardList:ArrayList<reward>)  :
    RecyclerView.Adapter<rewardListAdapter.rewardListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rewardListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rewardlist_cardview, parent, false)
        return rewardListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: rewardListViewHolder, position: Int) {
        val currentItem = rewardList[position]

        holder.reward_UID.text = currentItem.reward_uid
        holder.reward_Name.text = currentItem.reward_Name
        holder.reward_Point.text = currentItem.reward_Point.toString()
        Glide.with(holder.reward_Image.context).load(currentItem.reward_image)
            .into(holder.reward_Image)
    }

    override fun getItemCount(): Int {
        return rewardList.size
    }

    class rewardListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val reward_UID: TextView = itemView.findViewById(R.id.reward_uid)
        val reward_Image: CircleImageView = itemView.findViewById(R.id.reward_img)
        val reward_Name: TextView = itemView.findViewById(R.id.reward_name)
        val reward_Point: TextView = itemView.findViewById(R.id.reward_point)
        val getRewardBtn: Button = itemView.findViewById(R.id.getReward_btn)

        init {
            getRewardBtn.setOnClickListener {

                val context = itemView.context
                val position: Int = adapterPosition
                val intent = Intent(context, claimReward::class.java);
                intent.putExtra("rewardID", reward_UID.text.toString())
                context.startActivity(intent)
            }
        }
    }
}
