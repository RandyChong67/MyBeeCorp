package com.example.mybeecorp.superadmin.fragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.superadmin.activities.SuperAdminUserList
import com.example.yangloosuperadmin.classes.UserRole

class userRoleListAdapter(private val userRoleList: ArrayList<UserRole>) :
    RecyclerView.Adapter<userRoleListAdapter.userRoleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userRoleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_role_list, parent, false)
        return userRoleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: userRoleViewHolder, position: Int) {
        val currentItem = userRoleList[position]

        holder.userRoleTitle.text = currentItem.user_role
        holder.userTotalCount.text = currentItem.userTotalCount
    }

    override fun getItemCount(): Int {
        return userRoleList.size
    }

    class userRoleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val userRoleTitle : TextView = itemView.findViewById(R.id.userRoleList_Title)
        val userTotalCount : TextView = itemView.findViewById(R.id.userRoleList_totalUser)

        init{
            itemView.setOnClickListener {
                val context = itemView.context
                val position : Int = adapterPosition
                if(position == 0){
                    val intent = Intent(context, SuperAdminUserList::class.java)
                    intent.putExtra("userRole", "Super Admin")
                    context.startActivity(intent)
                }
                else if(position == 1){
                    val intent = Intent(context, SuperAdminUserList::class.java)
                    intent.putExtra("userRole", "Insurance Company Admin")
                    context.startActivity(intent)
                }
                else{
                    val intent = Intent(context, SuperAdminUserList::class.java)
                    intent.putExtra("userRole", "Member")
                    context.startActivity(intent)
                }
            }
        }
    }
}