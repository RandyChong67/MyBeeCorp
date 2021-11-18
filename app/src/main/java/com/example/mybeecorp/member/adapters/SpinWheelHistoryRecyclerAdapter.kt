package com.example.mybeecorp.member.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.mybeecorp.R
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.classes.Spin_History

class SpinWheelHistoryRecyclerAdapter(private var spinHistoryList: List<Spin_History>) :
    RecyclerView.Adapter<SpinWheelHistoryRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val spinWheelPrize: TextView = itemView.findViewById(R.id.text_spin_history)
        val itemDate: TextView = itemView.findViewById(R.id.text_spin_history_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.spin_wheel_history_recycleview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.spinWheelPrize.text = spinHistoryList[position].spin_wheel_name
        holder.itemDate.text = spinHistoryList[position].spin_date_time
    }

    override fun getItemCount(): Int {
        return spinHistoryList.size
    }

}