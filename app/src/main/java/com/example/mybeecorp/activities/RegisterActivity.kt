package com.example.mybeecorp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mybeecorp.R
import com.example.mybeecorp.fragments.Register1Fragment
import com.example.mybeecorp.fragments.Register2Fragment
import com.example.mybeecorp.fragments.Register3Fragment
import com.example.mybeecorp.interfaces.Communicator

class RegisterActivity : AppCompatActivity(), Communicator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val fragmentRegister1 = Register1Fragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentRegister1).commit()
    }

    override fun passDataCom2(
        fullname: String?,
        role: String?,
        email: String?,
        password: String?,
        insuranceCompany: String?
    ) {
        val bundle = Bundle()
        bundle.putString("fullname", fullname)
        bundle.putString("role", role)
        bundle.putString("email", email)
        bundle.putString("password", password)
        bundle.putString("company", insuranceCompany)

        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentRegister3 = Register3Fragment()
        fragmentRegister3.arguments = bundle

        transaction.replace(R.id.fragment_container, fragmentRegister3)
        transaction.commit()
    }

    override fun goBackFragment1(
        fullname: String?,
        role: String?,
        email: String?,
        password: String?,
        insuranceCompany: String?
    ) {
        val bundle = Bundle()
        bundle.putString("fullname", fullname)
        bundle.putString("role", role)
        bundle.putString("email", email)
        bundle.putString("password", password)
        bundle.putString("company", insuranceCompany)

        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentRegister1 = Register1Fragment()
        fragmentRegister1.arguments = bundle

        transaction.replace(R.id.fragment_container, fragmentRegister1)
        transaction.commit()
    }

    override fun goBackFragment2(
        fullname: String?,
        role: String?,
        email: String?,
        password: String?,
        insuranceCompany: String?
    ) {
        val bundle = Bundle()
        bundle.putString("fullname", fullname)
        bundle.putString("role", role)
        bundle.putString("email", email)
        bundle.putString("password", password)
        bundle.putString("company", insuranceCompany)

        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentRegister2 = Register2Fragment()
        fragmentRegister2.arguments = bundle

        transaction.replace(R.id.fragment_container, fragmentRegister2)
        transaction.commit()
    }
}