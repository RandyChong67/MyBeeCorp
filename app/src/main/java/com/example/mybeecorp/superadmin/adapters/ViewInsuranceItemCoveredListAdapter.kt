package my.com.superadmin.InsuranceModule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.DbItemCovered

class ViewInsuranceItemCoveredListAdapter(context: Context, viewItemCoveredList:ArrayList<DbItemCovered>): ArrayAdapter<DbItemCovered>(context,
    R.layout.activity_view_insurance_information_super_admin, viewItemCoveredList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItemView: View? = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_view_detail_insurance_item_covered, parent, false)
        }

        val itemCovered: DbItemCovered? = getItem(position)

        val itemCoveredName = listItemView!!.findViewById<TextView>(R.id.itemCoveredName_TextView)
        //val itemCoveredStatus = listItemView!!.findViewById<TextView>(R.id.itemCoveredStatus_TextView)
        val itemCoveredPrice = listItemView!!.findViewById<TextView>(R.id.itemCoveredPrice_TextView)

        itemCoveredName.text = itemCovered!!.item_name
        //itemCoveredStatus.text = itemCovered!!.item_status
        itemCoveredPrice.text = itemCovered!!.item_price

        return listItemView
    }
}