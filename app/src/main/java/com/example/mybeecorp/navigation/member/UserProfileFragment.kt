package com.example.mybeecorp.navigation.member

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mybeecorp.R
import com.example.mybeecorp.member.fragments.MemberMyProfile
import com.google.android.material.card.MaterialCardView


class UserProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonMyProfile = view.findViewById<MaterialCardView>(R.id.button_my_profile)
        buttonMyProfile.setOnClickListener {
            val intent = Intent(view.context, MemberMyProfile::class.java)
            view.context.startActivity(intent)
        }
    }
}