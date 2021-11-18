package com.example.mybeecorp.insurancecompanyadmin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.example.mybeecorp.R
import com.example.mybeecorp.insurancecompanyadmin.adapters.ViewPagerAdapterCA
import com.example.mybeecorp.insurancecompanyadmin.fragments.AllInsuranceCompany
import com.example.mybeecorp.insurancecompanyadmin.fragments.MyCompany

class InsCompanyCAMainPage : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_ins_company_camain_page)
        setUpTabs()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapterCA(supportFragmentManager)
        adapter.addFragment(MyCompany(),"My Company")
        adapter.addFragment(AllInsuranceCompany(),"All Insurance Company")
        val viewPager = findViewById<ViewPager>(R.id.viewPagerCA)
        viewPager.adapter = adapter
        val tabs = findViewById<TabLayout>(R.id.tabsCA)
        tabs.setupWithViewPager(viewPager)
    }
}