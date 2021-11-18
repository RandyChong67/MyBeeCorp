package com.example.mybeecorp.member.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import my.com.customer.classes.Item_Covered

class ItemCoveredListAdapter( private var itemCoveredList:List<Item_Covered>): RecyclerView.Adapter<ItemCoveredListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemCoveredName = itemView.findViewById<TextView>(R.id.itemCoveredName_TextView)
        val itemCoveredPrice = itemView.findViewById<TextView>(R.id.itemCoveredPrice_TextView)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_my_insurance_item_covered, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.itemCoveredName.text = itemCoveredList[position].item_name
        holder.itemCoveredPrice.text = itemCoveredList[position].item_price

        Log.i("Test Recycler Adapter", itemCoveredList[position].item_name)
        Log.i("Test Recycler Adapter", itemCoveredList[position].item_price)
    }

    override fun getItemCount(): Int {
        return itemCoveredList.size
    }
}