package com.example.mybeecorp.superadmin.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Variant
import com.example.mybeecorp.superadmin.activities.EditVehicleModelActivity
import com.example.mybeecorp.superadmin.activities.EditVehicleVariantActivity
import java.math.RoundingMode
import java.text.DecimalFormat

class VehicleVariantRecyclerAdapter(
    private var variantPositionList: List<String>,
    private var variantList: List<Variant>
    ): RecyclerView.Adapter<VehicleVariantRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val variantPosition: TextView = itemView.findViewById(R.id.text_variant_position)
        val variantName: TextView = itemView.findViewById(R.id.text_variant)
        val modelName: TextView = itemView.findViewById(R.id.text_model)
        val variantPrice: TextView = itemView.findViewById(R.id.text_price)

        init {
            itemView.setOnClickListener{ v: View ->
                val position: Int = adapterPosition
                val intent = Intent(itemView.context, EditVehicleVariantActivity::class.java)
                intent.putExtra("variant_uid", variantList[position].variant_id)
                intent.putExtra("variant_name", variantList[position].variant_name)
                intent.putExtra("variant_price", variantList[position].variant_price.toString())
                intent.putExtra("model_name", variantList[position].model_name)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_variant_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        val price = df.format(variantList[position].variant_price).toDouble()

        holder.variantPosition.text = variantPositionList[position]
        holder.variantName.text = variantList[position].variant_name
        holder.modelName.text = variantList[position].model_name
        holder.variantPrice.text = String.format("RM %.2f", price)
    }

    override fun getItemCount(): Int {
        return variantList.size
    }
}