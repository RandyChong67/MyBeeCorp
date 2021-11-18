package com.example.mybeecorp.superadmin.fragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.example.mybeecorp.superadmin.activities.InsuranceCompanyAdminUserProfile
import com.example.mybeecorp.superadmin.activities.MemberUserProfile
import com.example.yangloosuperadmin.classes.UserList
import de.hdodenhof.circleimageview.CircleImageView

class userListAdapter(private val userList:ArrayList<UserList>) :
    RecyclerView.Adapter<userListAdapter.userListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_list, parent, false)
        return userListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: userListViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.userUID.text = currentItem.user_uid
        holder.userlist_userRole.text = currentItem.user_role
        Glide.with(holder.userProfilePic.context).load(currentItem.user_avatar).placeholder(R.drawable.ic_baseline_account_circle_24).circleCrop().error(
            R.drawable.ic_baseline_account_circle_24
        ).into(holder.userProfilePic)
        holder.userlist_name.text = currentItem.user_full_name
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class userListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userUID : TextView = itemView.findViewById(R.id.userList_UID)
        val userProfilePic : CircleImageView = itemView.findViewById(R.id.userList_profilePic)
        val userlist_name : TextView = itemView.findViewById(R.id.userList_userName)
        val userlist_userRole : TextView = itemView.findViewById(R.id.userList_userRole)

        init{
            itemView.setOnClickListener {
                val context = itemView.context
                val position : Int = adapterPosition
                if(userlist_userRole.text.toString() == "Super Admin") {
                    val intent = Intent(context, SuperAdminUserProfile::class.java)
                    intent.putExtra("userID", userUID.text.toString())
                    context.startActivity(intent)
                }
                else if(userlist_userRole.text.toString() == "Insurance Company Admin"){
                    val intent = Intent(context, InsuranceCompanyAdminUserProfile::class.java)
                    intent.putExtra("userID", userUID.text.toString())
                    context.startActivity(intent)
                }
                else{
                    val intent = Intent(context, MemberUserProfile::class.java)
                    intent.putExtra("userID", userUID.text.toString())
                    context.startActivity(intent)
                }
            }
        }
    }
}