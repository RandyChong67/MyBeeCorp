package com.example.mybeecorp.member.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.member.activities.ViewDetail_InsuranceInformation
import my.com.customer.classes.Insurance_Bought
import my.com.customer.classes.Insurance_Company

class MyInsuranceListAdapter(private var insuranceList: List<Insurance_Bought>) :
    RecyclerView.Adapter<MyInsuranceListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val insuranceName = itemView.findViewById<TextView>(R.id.CompanyInsuranceName_TextView)
        val yearlyPayment = itemView.findViewById<TextView>(R.id.yearlyPayment_TextView)

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                val intent = Intent(itemView.context, ViewDetail_InsuranceInformation::class.java)
                intent.putExtra("insurance_bought_uid", insuranceList[position].ins_bought_uid)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_my_insurance_list_, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.insuranceName.text = insuranceList[position].insurance_name
        holder.yearlyPayment.text = String.format("RM %.2f", insuranceList[position].yearly_payment)
    }

    override fun getItemCount(): Int {
        return insuranceList.size
    }


//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//
//        var listItemView: View? = convertView
//
//        if (listItemView == null) {
//            listItemView = LayoutInflater.from(getContext())
//                .inflate(R.layout.fragment_my_insurance_list_, parent, false)
//        }
//
//        val myInsurance: Insurance_Bought? = getItem(position)
//
//        var insuranceName = listItemView!!.findViewById<TextView>(R.id.CompanyInsuranceName_TextView)
//        var yearlyPayment = listItemView!!.findViewById<TextView>(R.id.yearlyPayment_TextView)
//
//        insuranceName.text = myInsurance!!.insurance_name
//        yearlyPayment.text = myInsurance!!.yearly_payment.toString()
//
//        // Get data from db
//
//
//        return listItemView
//    }
}