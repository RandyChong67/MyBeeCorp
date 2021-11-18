package com.example.mybeecorp.superadmin.activities

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybeecorp.R
import com.google.firebase.database.*
import my.com.superadmin.InsuranceCompanyModule.adapters.OptionDeleteInsCompanyListAdapter
import com.example.mybeecorp.classes.DbInsCompany

class DeleteInsCompanySuperAdmin : AppCompatActivity() {

    var instance = FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
    var dataList = arrayListOf<DbInsCompany>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_ins_company_super_admin)
        supportActionBar?.setTitle("Delete Insurance Company")
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val optionInsCompany = findViewById<CheckBox>(R.id.optionDeleteInsCompany_checkbox)
        val listView = findViewById<ListView>(R.id.InsCompanyListView)
        val resetBtn: Button = findViewById(R.id.back_button)
        val deleteBtn: Button = findViewById(R.id.save_button)
        val sortButton = findViewById<ImageView>(R.id.sortByAlpha_icon)

        sortButton.setOnClickListener{
            Log.i("Information", "Sort icon had been clicked.")
            val linearLayoutManager = LinearLayoutManager(this@DeleteInsCompanySuperAdmin)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true
        }

        resetBtn.setOnClickListener{
            var index = 0
            do{
                var view = getViewByPosition(index,listView)
                var checkBox = view?.findViewById<CheckBox>(R.id.optionDeleteInsCompany_checkbox)
                Log.i("Task",view?.findViewById<TextView>(R.id.insuranceCompanyName_TextView)?.text.toString())
                checkBox?.isChecked = false
                (listView.adapter as OptionDeleteInsCompanyListAdapter).checkedList.clear()
                index++
            }while (listView.count > index)
        }

        deleteBtn.setOnClickListener{
            Log.i("test","delete button clicked")
            var list = mutableMapOf<String,Any?>()
            var checkList = (listView.adapter as OptionDeleteInsCompanyListAdapter).checkedList
            if(checkList.size > 0){
                val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@DeleteInsCompanySuperAdmin)
                builder.setTitle("Confirmation Message")
                builder.setMessage("Are you sure to delete?")

                builder.setPositiveButton("Yes") { dialog, which ->
                    for(index in checkList){
                        var data = dataList[index]
                        var key  = "${data.company_uid}/company_status"
                        list[key] = "Closed"
                    }
                    Log.i("test","Remove list "+list.size.toString())
                    var ref = instance.getReference("Insurance_Company").updateChildren(list)
                    ref.addOnSuccessListener{
                        //Toast.makeText(applicationContext, "Insurance had been deleted.", Toast.LENGTH_SHORT).show()
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Message")
                        builder.setMessage("Insurance Company deleted successfully!")
                        builder.setCancelable(true)
                        builder.setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        val alert = builder.create()
                        alert.show()
                    }
                    return@setPositiveButton
                }
                builder.setNegativeButton("No") { dialog, which ->
                    Log.i("Information", "No item had been deleted.")
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            else{
                Log.i("Information", "No item had been deleted.")
            }
        }

        //Data display in ListView
        val textNoInsCompany= findViewById<TextView>(R.id.NoInsuranceCompany_textView)
        var ref = instance.getReference("Insurance_Company").orderByChild("company_name").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    dataList.clear()
                    for(item in snapshot.children){
                        var insCompany = item.getValue(DbInsCompany::class.java)
                        if(insCompany !=null && insCompany.company_status == "Available"){
                            dataList.add(insCompany)
                        }
                    }
                    if(dataList.size > 0){
                        textNoInsCompany.visibility = View.GONE
                        val adapter = OptionDeleteInsCompanyListAdapter(applicationContext,dataList)
                        listView.adapter = adapter
                    }else{
                        //Toast.makeText(applicationContext,"No Insurance Company Record.", Toast.LENGTH_SHORT).show()
                        val builder = AlertDialog.Builder(this@DeleteInsCompanySuperAdmin)
                        builder.setTitle("Warmly Message")
                        builder.setMessage("Currently having NO or AVAILABLE status of Insurance Company!")
                        builder.setCancelable(true)
                        builder.setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        val alert = builder.create()
                        alert.show()
                        return
                    }
                }else{
                    Toast.makeText(applicationContext,"No record.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG).show()
            }
        })

        listView.onItemClickListener=
            AdapterView.OnItemClickListener { parent, view, position, id ->
                Log.i("LOG","Item clicked")
                var optionItemCovered = view.findViewById<CheckBox>(R.id.optionDeleteInsCompany_checkbox)
                optionItemCovered.performClick()
            }
    }

    private fun getViewByPosition(position: Int, listView: ListView): View? {
        val firstListItemPosition = listView.firstVisiblePosition
        val lastListItemPosition = firstListItemPosition + listView.childCount - 1
        return if (position < firstListItemPosition || position > lastListItemPosition) {
            listView.adapter.getView(position, listView.getChildAt(position), listView)
        } else {
            val childIndex = position - firstListItemPosition
            listView.getChildAt(childIndex)
        }
    }

    //Back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}