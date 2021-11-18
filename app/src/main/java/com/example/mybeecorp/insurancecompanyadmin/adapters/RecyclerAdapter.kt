package com.example.mybeecorp.insurancecompanyadmin.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.DbInsCompany
import my.com.customer.classes.Insurance_Company

class RecyclerAdapter(private var companyList: List<DbInsCompany>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val companyLogo: ImageView = itemView.findViewById(R.id.companyLogo_imageView)
        val companyName: TextView = itemView.findViewById(R.id.insuranceCompanyName_TextView)

        init{
            itemView.setOnClickListener{ v: View ->
                val position: Int = adapterPosition
                Log.i("Information","User had clicked on insuranceCompany #${position + 1}")
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fragment_all_ins_company_c_a,parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.companyLogo.context).load(companyList[position].company_logo).into(holder.companyLogo)
        holder.companyName.text = companyList[position].company_name
    }

    override fun getItemCount(): Int {
        return companyList.size
    }
}










