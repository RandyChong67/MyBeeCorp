package com.example.mybeecorp.navigation.insurancecompanyadmin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mybeecorp.R
import com.example.mybeecorp.insurancecompanyadmin.fragments.InsuranceCompanyAdminMemberList
import com.google.android.material.card.MaterialCardView

class MemberFragment_InsuranceAdmin : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_insurance_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonMember = view.findViewById<MaterialCardView>(R.id.button_member)
        buttonMember.setOnClickListener {
            val intent = Intent(view.context, InsuranceCompanyAdminMemberList::class.java)
            view.context.startActivity(intent)
        }
    }
}