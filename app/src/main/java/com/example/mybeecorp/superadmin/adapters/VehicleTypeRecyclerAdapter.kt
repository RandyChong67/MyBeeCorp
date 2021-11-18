package com.example.mybeecorp.superadmin.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Type
import com.example.mybeecorp.superadmin.activities.EditVehicleTypeActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class VehicleTypeRecyclerAdapter(
    private var typePositionList: List<String>,
    private var typeList: List<Type>
    ): RecyclerView.Adapter<VehicleTypeRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val typePosition: TextView = itemView.findViewById(R.id.text_type_position)
        val typeName: TextView = itemView.findViewById(R.id.text_type)
        val typeUID: TextView = itemView.findViewById(R.id.text_type_uid)
        init {
            itemView.setOnClickListener{ v: View ->
                val position: Int = adapterPosition
                val intent = Intent(itemView.context, EditVehicleTypeActivity::class.java)
                intent.putExtra("type_uid", typeList[position].type_uid)
                intent.putExtra("type_name", typeList[position].type_name)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_type_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.typePosition.text = typePositionList[position]
        holder.typeName.text = typeList[position].type_name
        holder.typeUID.text = typeList[position].type_uid
    }

    override fun getItemCount(): Int {
        return typeList.size
    }
}