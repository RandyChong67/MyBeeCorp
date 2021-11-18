package com.example.mybeecorp.member.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mybeecorp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyInsuranceList_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyInsuranceList_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var insuranceName: String? = null
    private var yearlyPayment: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            insuranceName = it.getString(ARG_PARAM1)
            yearlyPayment = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_my_insurance_list_, container, false)
        view.findViewById<TextView>(R.id.companyLogo_imageView).text = insuranceName
        view.findViewById<TextView>(R.id.yearlyPayment_TextView).text = yearlyPayment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param insuranceName Parameter 1.
         * @param yearlyPayment Parameter 2.
         * @return A new instance of fragment MyInsuranceList_Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(insuranceName: String, yearlyPayment: String) =
            MyInsuranceList_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, insuranceName)
                    putString(ARG_PARAM2, yearlyPayment)
                }
            }
    }
}