package com.example.mybeecorp.superadmin.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.reward
import com.example.mybeecorp.superadmin.activities.Update_Reward
import de.hdodenhof.circleimageview.CircleImageView

class rewardAdapter (private val rewardList:ArrayList<reward>)  :
    RecyclerView.Adapter<rewardAdapter.rewardListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rewardListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.reward_cardview, parent, false)
        return rewardListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: rewardListViewHolder, position: Int) {
        val currentItem = rewardList[position]

        holder.rewardUID.text = currentItem.reward_uid
        holder.rewardName.text = currentItem.reward_Name
        holder.rewardPoint.text = currentItem.reward_Point.toString()
        Glide.with(holder.rewardImage.context).load(currentItem.reward_image).into(holder.rewardImage)
    }

    override fun getItemCount(): Int {
        return rewardList.size
    }

    class rewardListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rewardUID: TextView = itemView.findViewById(R.id.reward_uid)
        val rewardImage: CircleImageView = itemView.findViewById(R.id.reward_img)
        val rewardName: TextView = itemView.findViewById(R.id.reward_name)
        val rewardPoint: TextView = itemView.findViewById(R.id.reward_point)

        init {
            itemView.setOnClickListener {
                val context = itemView.context
                val position: Int = adapterPosition

                val intent = Intent(context, Update_Reward::class.java)
                intent.putExtra("rewardID", rewardUID.text.toString())
                context.startActivity(intent)

            }
        }
    }
}