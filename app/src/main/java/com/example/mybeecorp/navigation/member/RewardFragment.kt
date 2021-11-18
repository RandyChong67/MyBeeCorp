package com.example.mybeecorp.navigation.member

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.mybeecorp.R
import com.example.mybeecorp.member.activities.ViewSpinWheel
import com.example.mybeecorp.member.activities.RewardMainActivity
import com.google.android.material.card.MaterialCardView


class RewardFragment : Fragment() {
    private var userName: String? =""
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reward, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userName = arguments?.getString("user_full_name")
        val buttonSpinWheel = view.findViewById<MaterialCardView>(R.id.buttonGoToSpinWheel)
        val buttonReward = view.findViewById<MaterialCardView>(R.id.button_reward)
        buttonSpinWheel.setOnClickListener{
            val intent = Intent (getActivity(), ViewSpinWheel::class.java)
            intent.putExtra("user_full_name", userName)
            startActivity(intent)
        }
        buttonReward.setOnClickListener{
            val intent = Intent (getActivity(), RewardMainActivity::class.java)
            intent.putExtra("user_full_name", userName)
            startActivity(intent)
        }
    }
}