package com.example.mybeecorp.superadmin.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.superadmin.activities.Update_Voucher
import my.com.customer.classes.Voucher

class voucherListAdapter (private val voucherList:ArrayList<Voucher>) :
    RecyclerView.Adapter<voucherListAdapter.voucherListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): voucherListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.voucher_list, parent, false)
        return voucherListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: voucherListViewHolder, position: Int) {
        val currentItem = voucherList[position]

        holder.voucherUID.text = currentItem.voucher_uid
        holder.voucherCode.text = currentItem.voucher_code
        holder.voucherExpiryDate.text = currentItem.voucher_expiry_date
        holder.voucherDiscount.text = currentItem.voucher_discount.toString()
    }

    override fun getItemCount(): Int {
        return voucherList.size
    }

    class voucherListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val voucherUID : TextView = itemView.findViewById(R.id.voucher_uid)
        val voucherCode : TextView = itemView.findViewById(R.id.voucher_code)
        val voucherExpiryDate : TextView = itemView.findViewById(R.id.voucher_expirydate)
        val voucherDiscount: TextView = itemView.findViewById(R.id.voucher_discount)

        init{
            itemView.setOnClickListener {
                val context = itemView.context
                val position : Int = adapterPosition
                val intent = Intent(context, Update_Voucher::class.java)
                intent.putExtra("voucherID", voucherUID.text.toString())
                context.startActivity(intent)
            }
        }
    }
}