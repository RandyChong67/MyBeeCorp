package com.example.mybeecorp.member.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.example.yangloosuperadmin.classes.ReferralMemberList
import de.hdodenhof.circleimageview.CircleImageView

class ReferralMemberListAdapter(private val memberList:ArrayList<ReferralMemberList>) : RecyclerView.Adapter<ReferralMemberListAdapter.memberListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): memberListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.referral_list, parent, false)
        return memberListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: memberListViewHolder, position: Int) {
        val currentItem = memberList[position]

        holder.userfullname.text = currentItem.user_full_name
        Glide.with(holder.profilePicCIV.context).load(currentItem.user_avatar).placeholder(R.drawable.ic_baseline_account_circle_24).circleCrop().error(
            R.drawable.ic_baseline_account_circle_24
        ).into(holder.profilePicCIV)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    class memberListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val profilePicCIV = itemView.findViewById<CircleImageView>(R.id.referralDetails_CIV)
        val userfullname = itemView.findViewById<TextView>(R.id.referralDetails_memberName)
    }
}