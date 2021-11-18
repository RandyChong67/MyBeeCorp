package com.example.mybeecorp.superadmin.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Model
import com.example.mybeecorp.superadmin.activities.EditVehicleBrandActivity
import com.example.mybeecorp.superadmin.activities.EditVehicleModelActivity

class VehicleModelRecyclerAdapter(
    private var modelPositionList: List<String>,
    private var modelList: List<Model>
    ): RecyclerView.Adapter<VehicleModelRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val modelPosition: TextView = itemView.findViewById(R.id.text_model_position)
        val modelName: TextView = itemView.findViewById(R.id.text_model)
        val brandName: TextView = itemView.findViewById(R.id.text_brand)
        init {
            itemView.setOnClickListener{ v: View ->
                val position: Int = adapterPosition
                val intent = Intent(itemView.context, EditVehicleModelActivity::class.java)
                intent.putExtra("model_uid", modelList[position].model_uid)
                intent.putExtra("model_name", modelList[position].model_name)
                intent.putExtra("brand_name", modelList[position].brand_name)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_model_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.modelPosition.text = modelPositionList[position]
        holder.modelName.text = modelList[position].model_name
        holder.brandName.text = modelList[position].brand_name
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}