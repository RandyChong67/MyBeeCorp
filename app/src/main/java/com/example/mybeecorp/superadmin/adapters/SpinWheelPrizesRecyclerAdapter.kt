package com.example.mybeecorp.superadmin.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.mybeecorp.R
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.classes.Spin_Wheel
import com.example.mybeecorp.superadmin.activities.EditPrizesActivity

class SpinWheelPrizesRecyclerAdapter(
    private var spinWheelList: List<Spin_Wheel>,
    private var spinWheelPosition: List<String>
) : RecyclerView.Adapter<SpinWheelPrizesRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val prizeName: TextView = itemView.findViewById(R.id.textPrizes)
        val prizePosition: TextView = itemView.findViewById(R.id.text_prize_position)

        init {
            itemView.setOnClickListener { v: View ->
                val position: Int = adapterPosition
                val intent = Intent(v.context, EditPrizesActivity::class.java)
                intent.putExtra("prize_uid", spinWheelList[position].spin_wheel_uid)
                intent.putExtra("prize_name", spinWheelList[position].spin_wheel_name)
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.spin_wheel_prizes_super_admin_recycleview, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.prizeName.text = spinWheelList[position].spin_wheel_name
        holder.prizePosition.text = "${spinWheelPosition[position]}."
    }

    override fun getItemCount(): Int {
        return spinWheelList.size
    }

}