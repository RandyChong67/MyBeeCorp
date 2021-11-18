package com.example.mybeecorp.member.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.member.activities.MemberReferralDetails
import com.example.yangloosuperadmin.classes.MemberInsuranceList

class MemberInsuranceListAdapter(private val insuranceList: ArrayList<MemberInsuranceList>) :
    RecyclerView.Adapter<MemberInsuranceListAdapter.insuranceListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): insuranceListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.referral_insurance_list, parent, false)
        return insuranceListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: insuranceListViewHolder, position: Int) {
        val currentItem = insuranceList[position]

        holder.insUID.text = currentItem.insurance_uid
        holder.insTitle.text = currentItem.insurance_name
        holder.insCompany.text = currentItem.insurance_company
        holder.referralCount.text = currentItem.referralCount.toString()
    }

    override fun getItemCount(): Int {
        return insuranceList.size
    }

    class insuranceListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val insUID = itemView.findViewById<TextView>(R.id.insuranceUID)
        val insTitle = itemView.findViewById<TextView>(R.id.insTitle)
        val insCompany = itemView.findViewById<TextView>(R.id.insCompanyName)
        val referralCount = itemView.findViewById<TextView>(R.id.referralCount)

        init{
            itemView.setOnClickListener {
                val context = itemView.context
                val position : Int = adapterPosition

                val intent = Intent(context, MemberReferralDetails::class.java)
                intent.putExtra("insUID", insUID.text.toString())
                intent.putExtra("insName", insTitle.text.toString())
                context.startActivity(intent)
            }
        }
    }
}