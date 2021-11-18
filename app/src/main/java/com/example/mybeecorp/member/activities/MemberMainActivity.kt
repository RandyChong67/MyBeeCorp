package com.example.mybeecorp.member.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.mybeecorp.activities.LoginActivity
import com.example.mybeecorp.R
import com.example.mybeecorp.navigation.member.InsuranceFragment
import com.example.mybeecorp.navigation.member.ReferralFragment
import com.example.mybeecorp.navigation.member.RewardFragment
import com.example.mybeecorp.navigation.member.UserProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MemberMainActivity : AppCompatActivity() {

    private var userName: String? = ""
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        userName = intent.getStringExtra("user_full_name")
        bundle = Bundle()
        bundle.putString("user_full_name", userName)
        val insuranceFragment = InsuranceFragment()
        val referralFragment = ReferralFragment()
        val rewardFragment = RewardFragment()
        val userProfileFragment = UserProfileFragment()
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        makeCurrentFragment(insuranceFragment)
        bottomNavigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.ic_insurance -> makeCurrentFragment(insuranceFragment)
                R.id.ic_referrals -> makeCurrentFragment(referralFragment)
                R.id.ic_reward -> makeCurrentFragment(rewardFragment)
                R.id.ic_user -> makeCurrentFragment(userProfileFragment)
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.member_main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            fragment.arguments = bundle
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

}