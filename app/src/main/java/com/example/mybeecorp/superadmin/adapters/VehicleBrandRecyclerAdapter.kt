package com.example.mybeecorp.superadmin.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Brand
import com.example.mybeecorp.superadmin.activities.EditVehicleBrandActivity
import com.example.mybeecorp.superadmin.activities.EditVehicleTypeActivity

class VehicleBrandRecyclerAdapter(
    private var brandPositionList: List<String>,
    private var brandList: List<Brand>
    ): RecyclerView.Adapter<VehicleBrandRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val brandPosition: TextView = itemView.findViewById(R.id.text_brand_position)
        val brandName: TextView = itemView.findViewById(R.id.text_brand)
        init {
            itemView.setOnClickListener { v: View ->
                val position: Int = adapterPosition
                val intent = Intent(itemView.context, EditVehicleBrandActivity::class.java)
                intent.putExtra("brand_uid", brandList[position].brand_uid)
                intent.putExtra("brand_name", brandList[position].brand_name)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_brand_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.brandPosition.text = brandPositionList[position]
        holder.brandName.text = brandList[position].brand_name
    }

    override fun getItemCount(): Int {
        return if (brandList == null) 0 else brandList.size
    }

}