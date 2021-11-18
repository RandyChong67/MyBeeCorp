package com.example.mybeecorp.member.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.mybeecorp.R
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.classes.Spin_Wheel

class SpinWheelPrizesRecyclerAdapter(private var prize: ArrayList<Spin_Wheel>) : RecyclerView.Adapter<SpinWheelPrizesRecyclerAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.textPrizes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.spin_wheel_prizes_recycleview,parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = "${position + 1}. ${prize[position].spin_wheel_name}"
    }

    override fun getItemCount(): Int {
        return prize.size
    }

}