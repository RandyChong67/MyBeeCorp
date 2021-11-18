package com.example.mybeecorp.insurancecompanyadmin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.mybeecorp.activities.LoginActivity
import com.example.mybeecorp.R
import com.example.mybeecorp.navigation.insurancecompanyadmin.CompanyFragment_InsuranceAdmin
import com.example.mybeecorp.navigation.insurancecompanyadmin.InsuranceFragment_InsuranceAdmin
import com.example.mybeecorp.navigation.insurancecompanyadmin.MemberFragment_InsuranceAdmin
import com.example.mybeecorp.navigation.insurancecompanyadmin.MyProfileFragment_InsuranceAdmin
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class InsuranceCompanyAdminMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_company_admin_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val insuranceFragment = InsuranceFragment_InsuranceAdmin()
        val memberFragment = MemberFragment_InsuranceAdmin()
        val companyFragment = CompanyFragment_InsuranceAdmin()
        val myProfileFragment = MyProfileFragment_InsuranceAdmin()
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation_insurance_admin)

        makeCurrentFragment(insuranceFragment)
        bottomNavigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.ic_insurance -> makeCurrentFragment(insuranceFragment)
                R.id.ic_users -> makeCurrentFragment(memberFragment)
                R.id.ic_company -> makeCurrentFragment(companyFragment)
                R.id.ic_user -> makeCurrentFragment(myProfileFragment)
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
        menuInflater.inflate(R.menu.ins_company_admin_main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}