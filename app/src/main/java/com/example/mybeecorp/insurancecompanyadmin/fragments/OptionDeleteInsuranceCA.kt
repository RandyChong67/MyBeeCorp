package com.example.mybeecorp.insurancecompanyadmin.fragments

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

/**
 * A simple [Fragment] subclass.
 * Use the [OptionDeleteInsuranceCA.newInstance] factory method to
 * create an instance of this fragment.
 */
class OptionDeleteInsuranceCA : Fragment() {
    // TODO: Rename and change types of parameters
    var insuranceName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            insuranceName = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_option_delete_insurance_c_a, container, false)
        view.findViewById<TextView>(R.id.insuranceName_TextView).text = insuranceName

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param insuranceName Parameter 1.
         * @return A new instance of fragment OptionDeleteInsuranceCA.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(insuranceName: String) =
            OptionDeleteInsuranceCA().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, insuranceName)
                }
            }
    }
}