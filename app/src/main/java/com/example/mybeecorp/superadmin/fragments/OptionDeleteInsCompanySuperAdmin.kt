package my.com.superadmin.InsuranceCompanyModule.fragments

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
 * Use the [OptionDeleteInsCompanySuperAdmin.newInstance] factory method to
 * create an instance of this fragment.
 */
class OptionDeleteInsCompanySuperAdmin : Fragment() {
    // TODO: Rename and change types of parameters
    private var companyName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            companyName = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_option_delete_ins_company_super_admin, container, false)
        view.findViewById<TextView>(R.id.insuranceCompanyName_TextView).text = companyName

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param companyName Parameter 1.
         * @return A new instance of fragment OptionDeleteInsCompanySuperAdmin.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(companyName: String) =
            OptionDeleteInsCompanySuperAdmin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, companyName)
                }
            }
    }
}