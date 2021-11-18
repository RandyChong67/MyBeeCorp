package com.example.mybeecorp.member.adapters

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.example.mybeecorp.member.activities.ViewMoreCompanyInsurance
import my.com.customer.classes.Insurance_Company
import java.io.ByteArrayOutputStream

class CompanyListRecyclerAdapter(private var companyList: List<Insurance_Company>) :
    RecyclerView.Adapter<CompanyListRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val companyLogo = itemView.findViewById<ImageView>(R.id.companyLogo_imageView)
        val companyName = itemView.findViewById<TextView>(R.id.CompanyInsuranceName_TextView)

        init{
            itemView.setOnClickListener { v: View ->
                val intent = Intent(itemView.context, ViewMoreCompanyInsurance::class.java)
                val position: Int = adapterPosition

                intent.putExtra("companyId", companyList[position].company_uid)
                intent.putExtra("companyName", companyList[position].company_name)
                intent.putExtra("companyPic", companyList[position].company_logo)

                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_company_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.companyLogo.context).load(companyList[position].company_logo).into(holder.companyLogo)
        holder.companyName.text = companyList[position].company_name
    }

    override fun getItemCount(): Int {
        return companyList.size
    }

//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//
//        var listItemView: View? = convertView
//
//        if (listItemView == null) {
//            listItemView = LayoutInflater.from(getContext())
//                .inflate(R.layout.fragment_company_list, parent, false);
//        }
//
//        val insCompany: Insurance_Company? = getItem(position)
//
////        val companyLogo = listItemView!!.findViewById<ImageView>(R.id.companyLogo_imageView)
//        val companyName = listItemView!!.findViewById<TextView>(R.id.CompanyInsuranceName_TextView)
//
//        companyName.text = insCompany!!.company_name
////        companyLogo.setImageBitmap(insCompany!!.bitmap)
//
//        // Get data from db
//
//
//        return listItemView
//    }
}