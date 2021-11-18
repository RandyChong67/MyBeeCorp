package com.example.mybeecorp.member.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mybeecorp.R
import my.com.customer.classes.OptionItemCovered

class OptionItemCoveredListAdapter (context: Context, optionItemCoveredList:ArrayList<OptionItemCovered>): ArrayAdapter<OptionItemCovered>(context,
    R.layout.activity_select_item_covered, optionItemCoveredList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItemView: View? = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_option_item_covered, parent, false)
        }

        val optionItemCovered: OptionItemCovered? = getItem(position)

        var itemCoveredName = listItemView!!.findViewById<TextView>(R.id.itemCoveredName_TextView)
        var itemCoveredPrice = listItemView!!.findViewById<TextView>(R.id.itemCoveredPrice_TextView)

        itemCoveredName.text = optionItemCovered!!.itemCoveredName
        itemCoveredPrice.text = optionItemCovered!!.itemCoveredPrice

        // Get data from db

        return listItemView
    }
}