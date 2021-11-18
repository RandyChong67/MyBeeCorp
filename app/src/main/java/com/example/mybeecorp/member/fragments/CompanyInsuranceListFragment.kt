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

/**
 * A simple [Fragment] subclass.
 * Use the [CompanyInsuranceListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompanyInsuranceListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var companyInsuranceName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            companyInsuranceName = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_company_insurance_list, container, false)
        view.findViewById<TextView>(R.id.CompanyInsuranceName_TextView).text = companyInsuranceName
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param companyInsuranceName Parameter 1.
         * @return A new instance of fragment CompanyInsuranceListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(companyInsuranceName: String) =
            CompanyInsuranceListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, companyInsuranceName)
                }
            }
    }
}