package my.com.superadmin.InsuranceModule.fragments

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
 * Use the [DeleteInsuranceListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeleteInsuranceListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var insuranceName: String? = null
    private var insuranceCompanyName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            insuranceName = it.getString(ARG_PARAM1)
            insuranceCompanyName = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_delete_insurance_list, container, false)
        view.findViewById<TextView>(R.id.insuranceName_TextView).text = insuranceName
        view.findViewById<TextView>(R.id.insuranceCompanyName_TextView).text = insuranceCompanyName

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param insuranceName Parameter 1.
         * @param insuranceCompanyName Parameter 2.
         * @return A new instance of fragment DeleteInsuranceListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(insuranceName: String, insuranceCompanyName: String) =
            DeleteInsuranceListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, insuranceName)
                    putString(ARG_PARAM2, insuranceCompanyName)
                }
            }
    }
}