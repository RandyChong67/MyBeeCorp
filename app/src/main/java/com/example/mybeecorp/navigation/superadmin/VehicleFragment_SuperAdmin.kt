package com.example.mybeecorp.navigation.superadmin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybeecorp.R
import com.example.mybeecorp.superadmin.activities.VehicleSettingsActivity
import com.example.mybeecorp.superadmin.activities.ViewVehicleActivity
import com.google.android.material.card.MaterialCardView


class VehicleFragment_SuperAdmin : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private var carImgList = mutableListOf<Int>()
    private var plateNumList = mutableListOf<String>()
    private var memberNameList = mutableListOf<String>()
    private var insuranceNameList = mutableListOf<String>()
    private var arrowImgList = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vehicle_super_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonViewVehicle = view.findViewById<MaterialCardView>(R.id.button_view_vehicle)
        val buttonVehicleSettings = view.findViewById<MaterialCardView>(R.id.button_vehicle_settings)

        buttonViewVehicle.setOnClickListener{
            val intent = Intent(view.context, ViewVehicleActivity::class.java)
            view.context.startActivity(intent)
        }

        buttonVehicleSettings.setOnClickListener {
            val intent = Intent(view.context, VehicleSettingsActivity::class.java)
            view.context.startActivity(intent)
        }
    }


}
