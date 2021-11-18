package my.com.insurancecompanyadmin.InsuranceModule.fragments

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
 * Use the [EditItemCoveredCA.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditItemCoveredCA : Fragment() {
    // TODO: Rename and change types of parameters
    private var editItemCoveredName: String? = null
    private var editItemCoveredPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            editItemCoveredName = it.getString(ARG_PARAM1)
            editItemCoveredPrice = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_item_covered_c_a, container, false)
        view.findViewById<TextView>(R.id.itemCoveredName_TextView).text = editItemCoveredName
        view.findViewById<TextView>(R.id.itemCoveredPrice_TextView).text = editItemCoveredPrice
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param editItemCoveredName Parameter 1.
         * @param editItemCoveredPrice Parameter 2.
         * @return A new instance of fragment EditItemCoveredCA.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(editItemCoveredName: String, editItemCoveredPrice: String) =
            EditItemCoveredCA().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, editItemCoveredName)
                    putString(ARG_PARAM2, editItemCoveredPrice)
                }
            }
    }
}