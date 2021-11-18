package com.example.mybeecorp.member.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.example.rewardmodule_member.member.MyRewardFragment
import com.example.rewardmodule_member.member.Pageadapter.pageAdapters
import com.example.rewardmodule_member.member.RewardListFragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
//chunyang
class RewardMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward_main)
        this.supportActionBar?.title = "Reward"
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rewardTabs()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun rewardTabs() {
        val adapter = pageAdapters(supportFragmentManager)
        adapter.addFragment(RewardListFragment(), "Reward List")
        adapter.addFragment(MyRewardFragment(), "My Reward")
        val viewPager = findViewById<ViewPager>(R.id.rewardViewPager)
        viewPager.adapter = adapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }
}