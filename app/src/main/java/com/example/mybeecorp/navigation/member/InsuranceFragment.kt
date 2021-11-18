package com.example.mybeecorp.navigation.member

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import com.example.mybeecorp.R
import com.example.mybeecorp.member.activities.InsuranceCompany_MainPage
import com.example.mybeecorp.member.activities.MyInsurance_MainPage
import com.example.mybeecorp.member.activities.ViewMyVehicleActivity
import com.google.android.material.card.MaterialCardView

class InsuranceFragment : Fragment() {

    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insurance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonMyInsurance = view.findViewById<MaterialCardView>(R.id.button_my_insurance)
        val buttonBuyInsurance = view.findViewById<MaterialCardView>(R.id.button_buy_insurance)
        val buttonMyVehicle = view.findViewById<MaterialCardView>(R.id.button_view_my_vehicle)


        buttonMyInsurance.setOnClickListener {
            val intent = Intent(view.context, MyInsurance_MainPage::class.java)
            view.context.startActivity(intent)
        }

        buttonBuyInsurance.setOnClickListener {

            val intent = Intent(view.context, InsuranceCompany_MainPage::class.java)
            view.context.startActivity(intent)

        }

        buttonMyVehicle.setOnClickListener {

            val intent = Intent(view.context, ViewMyVehicleActivity::class.java)
            view.context.startActivity(intent)

        }
    }
}