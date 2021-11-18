package com.example.mybeecorp.insurancecompanyadmin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mybeecorp.classes.DbInsurance
import com.example.mybeecorp.R

class InsuranceListCAAdapter (context: Context, InsuranceCompanyAdminList:ArrayList<DbInsurance>): ArrayAdapter<DbInsurance>(context,
    R.layout.activity_insurance_camain_page, InsuranceCompanyAdminList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItemView: View? = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_insurance_list_company_admin, parent, false)
        }

        val insurance: DbInsurance? = getItem(position)

        val insuranceName = listItemView!!.findViewById<TextView>(R.id.insuranceName_TextView)
//        val insuranceCompanyName = listItemView!!.findViewById<TextView>(R.id.insuranceCompanyName_TextView)

        insuranceName.text = insurance!!.insurance_name
//        insuranceCompanyName.text = insurance!!.insurance_company

        return listItemView
    }
}