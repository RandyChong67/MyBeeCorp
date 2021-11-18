package com.example.mybeecorp.superadmin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.DbInsurance

class DeleteInsuranceListAdapter (context: Context, optionDeleteInsuranceList:ArrayList<DbInsurance>): ArrayAdapter<DbInsurance>(context,
    R.layout.activity_delete_insurance_super_admin, optionDeleteInsuranceList) {

    var checkedList = mutableSetOf<Int>()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItemView: View? = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_delete_insurance_list, parent, false)
        }

        val insurance: DbInsurance? = getItem(position)

        val insuranceName = listItemView!!.findViewById<TextView>(R.id.insuranceName_TextView)
        val insuranceCompanyName = listItemView.findViewById<TextView>(R.id.insuranceCompanyName_TextView)
        //listItemView!!. findViewById<CheckBox>(R.id.optionDeleteInsurance_checkbox).isChecked = checkedList.contains(position)
        var checkbox = listItemView!!. findViewById<CheckBox>(R.id.optionDeleteInsurance_checkbox)

        checkbox.setOnClickListener{
            if(checkbox.isChecked){
                checkedList.add(position)
            }
            else{
                checkedList.remove(position)
            }
        }
        checkbox.isChecked = checkedList.contains(position)

        insuranceName.text = insurance!!.insurance_name
        insuranceCompanyName.text = insurance.insurance_company

        return listItemView
    }
}