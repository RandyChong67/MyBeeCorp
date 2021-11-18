package com.example.mybeecorp.member.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mybeecorp.R
import my.com.customer.classes.Motor_Insurance

class CompanyInsuranceListAdapter(context: Context, var companyInsuranceList: List<Motor_Insurance>): ArrayAdapter<Motor_Insurance>(context,
    R.layout.activity_insurance_company_main_page, companyInsuranceList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItemView: View? = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_company_insurance_list, parent, false)
        }

        val companyinsurancelist: Motor_Insurance? = getItem(position)

        var companyInsuranceName = listItemView!!.findViewById<TextView>(R.id.CompanyInsuranceName_TextView)

        companyInsuranceName.text = companyinsurancelist!!.insurance_name

        return listItemView
    }
}