package com.example.mybeecorp.superadmin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.DbInsCompany

class InsCompanyListAdapter (context: Context, InsCompanyList:ArrayList<DbInsCompany>): ArrayAdapter<DbInsCompany>(context,
    R.layout.activity_ins_company_super_admin_main_page, InsCompanyList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItemView: View? = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_ins_company_list, parent, false)
        }

        val insCompany: DbInsCompany? = getItem(position)

        val companyLogo = listItemView!!.findViewById<ImageView>(R.id.companyLogo_img)
        val companyName = listItemView!!.findViewById<TextView>(R.id.CompanyInsuranceName_TextView)
        companyName.text = insCompany!!.company_name
        Glide.with(companyLogo.context).load(insCompany.company_logo).into(companyLogo)

        return listItemView
    }
}