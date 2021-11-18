package com.example.mybeecorp.superadmin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.mybeecorp.R
import com.example.mybeecorp.superadmin.fragments.*
import com.example.mybeecorp.superadmin.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class VehicleSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_settings)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.title = "Vehicle Settings"
        setUpTabs()
    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(VehicleTypeFragment(),"Type")
        adapter.addFragment(VehicleBrandFragment(),"Brand")
        adapter.addFragment(VehicleModelFragment(),"Model")
        adapter.addFragment(VehicleVariantFragment(),"Variant")
        val viewPager = findViewById<ViewPager>(R.id.viewPager_editVehicle)
        viewPager.adapter = adapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}