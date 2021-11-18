package com.example.mybeecorp.member.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mybeecorp.R
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.classes.Vehicle
import com.example.mybeecorp.member.activities.MyVehicleDetailActivity
import com.example.mybeecorp.superadmin.activities.VehicleDetailActivity
import my.com.customer.classes.Insurance_Bought

class ViewMyVehicleRecyclerAdapter(var vehicleList: List<Vehicle>, var insuranceList: List<Insurance_Bought>) :
    RecyclerView.Adapter<ViewMyVehicleRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plateNum: TextView = itemView.findViewById(R.id.text_car_plate)
        val brandName: TextView = itemView.findViewById(R.id.text_brand)
        val modelName: TextView = itemView.findViewById(R.id.text_model)

        init {
            itemView.setOnClickListener { v: View ->
                val context = v.context
                val position: Int = adapterPosition
                val intent = Intent(context, MyVehicleDetailActivity::class.java)
                intent.putExtra("vehicle_uid", vehicleList[position].vehicle_uid)
                intent.putExtra("insurance_bought_uid", insuranceList[position].ins_bought_uid)
                context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_my_vehicle_recylerview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.plateNum.text = vehicleList[position].vehicle_plate_num
        holder.brandName.text = vehicleList[position].vehicle_brand
        holder.modelName.text = vehicleList[position].vehicle_model
    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }


}