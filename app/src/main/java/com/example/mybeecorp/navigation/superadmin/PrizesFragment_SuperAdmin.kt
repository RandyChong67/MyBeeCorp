package com.example.mybeecorp.navigation.superadmin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mybeecorp.R
import com.example.mybeecorp.superadmin.activities.*
import com.google.android.material.card.MaterialCardView


class PrizesFragment_SuperAdmin : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_super_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonSpinWheel = view.findViewById<MaterialCardView>(R.id.button_spin_wheel)
        val buttonCreateReward = view.findViewById<MaterialCardView>(R.id.button_create_reward)
        val buttonViewReward = view.findViewById<MaterialCardView>(R.id.button_view_reward)
        val buttonCreateVoucher = view.findViewById<MaterialCardView>(R.id.button_create_voucher)
        val buttonViewVoucher = view.findViewById<MaterialCardView>(R.id.button_view_voucher)

        buttonSpinWheel.setOnClickListener {
            val intent = Intent(view.context, EditSpinWheelActivity::class.java)
            view.context.startActivity(intent)
        }

        buttonCreateReward.setOnClickListener {
            val intent = Intent(view.context, Create_Reward::class.java)
            view.context.startActivity(intent)
        }

        buttonViewReward.setOnClickListener {
            val intent = Intent(view.context, View_Reward::class.java)
            view.context.startActivity(intent)
        }

        buttonCreateVoucher.setOnClickListener {
            val intent = Intent(view.context, Create_Voucher::class.java)
            view.context.startActivity(intent)
        }

        buttonViewVoucher.setOnClickListener {
            val intent = Intent(view.context, View_Voucher::class.java)
            view.context.startActivity(intent)
        }

    }
}