package my.com.superadmin.InsuranceCompanyModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.mybeecorp.classes.DbInsCompany
import com.example.mybeecorp.R

class OptionDeleteInsCompanyListAdapter (context: Context, OptionDeleteInsCompanySuperAdmin:ArrayList<DbInsCompany>): ArrayAdapter<DbInsCompany>(context,
    R.layout.activity_delete_ins_company_super_admin, OptionDeleteInsCompanySuperAdmin) {

    var checkedList = mutableSetOf<Int>()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItemView: View? = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_option_delete_ins_company_super_admin, parent, false)
        }

        val optionInsCompany: DbInsCompany? = getItem(position)

        val companyName = listItemView!!.findViewById<TextView>(R.id.insuranceCompanyName_TextView)
        var checkbox = listItemView!!. findViewById<CheckBox>(R.id.optionDeleteInsCompany_checkbox)

        checkbox.setOnClickListener{
            if(checkbox.isChecked){
                checkedList.add(position)
            }
            else{
                checkedList.remove(position)
            }
        }
        checkbox.isChecked = checkedList.contains(position)

        companyName.text = optionInsCompany!!.company_name

        return listItemView
    }


}
