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
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllInsCompanyCA.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllInsCompanyCA : Fragment() {
    // TODO: Rename and change types of parameters
    private var companyLogo: String? = null
    private var companyName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            companyLogo = it.getString(ARG_PARAM1)
            companyName = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_ins_company_c_a, container, false)
        view.findViewById<TextView>(R.id.companyLogo_imageView).text = companyLogo
        view.findViewById<TextView>(R.id.insuranceCompanyName_TextView).text = companyName

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param companyLogo Parameter 1.
         * @param companyName Parameter 2.
         * @return A new instance of fragment AllInsCompanyCA.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(companyLogo: String, companyName: String) =
            AllInsCompanyCA().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, companyLogo)
                    putString(ARG_PARAM2, companyName)
                }
            }
    }
}