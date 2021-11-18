package com.example.mybeecorp.insurancecompanyadmin.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mybeecorp.R
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.classes.Vehicle
import com.example.mybeecorp.insurancecompanyadmin.activities.MemberVehicleDetailActivity
import my.com.customer.classes.Insurance_Bought

class ViewMemberVehicleRecyclerAdapter(var vehicleList: List<Vehicle>, var insuranceList: List<Insurance_Bought>) :
    RecyclerView.Adapter<ViewMemberVehicleRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plateNum: TextView = itemView.findViewById(R.id.text_car_plate)
        val memberName: TextView = itemView.findViewById(R.id.text_member)
        val insurance: TextView = itemView.findViewById(R.id.text_insurance_name)

        init {
            itemView.setOnClickListener { v: View ->
                val context = v.context
                val position: Int = adapterPosition
                val intent = Intent(context, MemberVehicleDetailActivity::class.java)
                intent.putExtra("member_name", vehicleList[position].vehicle_owner)
                intent.putExtra("insurance_name", insuranceList[position].insurance_name)
                intent.putExtra("vehicle_uid", vehicleList[position].vehicle_uid)
                intent.putExtra("insurance_bought_uid", insuranceList[position].ins_bought_uid)
                context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_vehicle_recylerview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.plateNum.text = vehicleList[position].vehicle_plate_num
        holder.memberName.text = vehicleList[position].vehicle_owner
        holder.insurance.text = insuranceList[position].insurance_name
    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }


}