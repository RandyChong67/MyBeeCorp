package com.example.mybeecorp.insurancecompanyadmin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.DbItemCovered

class EditItemCoveredCAListAdapter (context: Context, EditItemCoveredList:ArrayList<DbItemCovered>): ArrayAdapter<DbItemCovered>(context,
    R.layout.activity_edit_insurance_ca, EditItemCoveredList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItemView: View? = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_edit_item_covered_c_a, parent, false)
        }

        val itemCovered: DbItemCovered? = getItem(position)

        val editItemCoveredName = listItemView!!.findViewById<TextView>(R.id.itemCoveredName_TextView)
        val editItemCoveredPrice = listItemView.findViewById<TextView>(R.id.itemCoveredPrice_TextView)

        editItemCoveredName.text = itemCovered!!.item_name
        editItemCoveredPrice.text = itemCovered.item_price

        return listItemView
    }
}