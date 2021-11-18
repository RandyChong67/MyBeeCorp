package com.example.mybeecorp.insurancecompanyadmin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.mybeecorp.classes.DbInsurance
import com.example.mybeecorp.R

class OptionDeleteInsuranceCAListAdapter(context: Context, OptionDeleteInsuranceCompanyAdminList:ArrayList<DbInsurance>): ArrayAdapter<DbInsurance>(context,
    R.layout.activity_delete_insurance_ca, OptionDeleteInsuranceCompanyAdminList) {

    var checkedList = mutableSetOf<Int>()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItemView: View? = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_option_delete_insurance_c_a, parent, false)
        }

        val insurance: DbInsurance? = getItem(position)

        val insuranceName = listItemView!!.findViewById<TextView>(R.id.insuranceName_TextView)
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

        return listItemView
    }
}