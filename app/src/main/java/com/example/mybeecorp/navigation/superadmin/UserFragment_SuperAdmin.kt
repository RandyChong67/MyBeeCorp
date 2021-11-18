package com.example.mybeecorp.navigation.superadmin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mybeecorp.R
import com.example.mybeecorp.superadmin.activities.SuperAdminUserRoleList
import com.google.android.material.card.MaterialCardView


class UserFragment_SuperAdmin : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_super_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonUserRole = view.findViewById<MaterialCardView>(R.id.button_user_role)

        buttonUserRole.setOnClickListener {
            val intent = Intent(view.context, SuperAdminUserRoleList::class.java)
            view.context.startActivity(intent)
        }
    }
}