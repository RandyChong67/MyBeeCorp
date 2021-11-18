package com.example.mybeecorp.superadmin.activities

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybeecorp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.mybeecorp.superadmin.adapters.DeleteInsuranceListAdapter
import com.example.mybeecorp.classes.DbInsurance

class DeleteInsuranceSuperAdmin : AppCompatActivity() {

    var instance = FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
    var dataList = arrayListOf<DbInsurance>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_insurance_super_admin)
        getSupportActionBar()?.setTitle("Delete Insurance")
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val listView = findViewById<ListView>(R.id.DeleteInsuranceListView)
        val resetBtn: Button = findViewById(R.id.back_button)
        val deleteBtn: Button = findViewById(R.id.save_button)
        val sortButton = findViewById<ImageView>(R.id.sortByAlpha_icon)

        sortButton.setOnClickListener{
            Log.i("Information", "Sort icon had been clicked.")
            val linearLayoutManager = LinearLayoutManager(this@DeleteInsuranceSuperAdmin)
            linearLayoutManager.reverseLayout = true
            linearLayoutManager.stackFromEnd = true
        }

        resetBtn.setOnClickListener{
            var index = 0
            do{
                var view = getViewByPosition(index,listView)
                var checkBox = view?.findViewById<CheckBox>(R.id.optionDeleteInsurance_checkbox)
                Log.i("Task",view?.findViewById<TextView>(R.id.insuranceName_TextView)?.text.toString())
                checkBox?.isChecked = false
                (listView.adapter as DeleteInsuranceListAdapter).checkedList.clear()
                index++
            }while (listView.count > index)
        }

        deleteBtn.setOnClickListener{
            Log.i("test","delete button clicked")
            var list = mutableMapOf<String,Any?>()
            var checkList = (listView.adapter as DeleteInsuranceListAdapter).checkedList
            if(checkList.size > 0){
                val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@DeleteInsuranceSuperAdmin)
                builder.setTitle("Confirmation Message")
                builder.setMessage("Are you sure to delete?")

                builder.setPositiveButton("Yes") { dialog, which ->
                    for(index in checkList){
                        var data = dataList[index]
                        //list[data.insurance_uid] = null
                        var key  = "${data.insurance_uid}/insurance_status"
                        list[key] = "Unavailable"
                    }
                    Log.i("test","Remove list "+list.size.toString())
                    val ref = instance.getReference("Motor_Insurance").updateChildren(list)
                    ref.addOnSuccessListener {
                        val builder = AlertDialog.Builder(this@DeleteInsuranceSuperAdmin)
                        builder.setTitle("Message")
                        builder.setMessage("Delete Insurance successfully.")
                        builder.setCancelable(true)
                        builder.setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        val alert = builder.create()
                        alert.show()
                        //Toast.makeText(applicationContext,"Insurance(s) deleted.", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.setNegativeButton("No") { dialog, which ->
                    Log.i("Information", "No item(s) had been deleted.")
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            else{
                Toast.makeText(applicationContext,"No item(s) to delete.", Toast.LENGTH_SHORT).show()
            }
        }

        //Data display in ListView
        val textNoInsurance = findViewById<TextView>(R.id.NoInsurance_TextView)
        var ref = instance.getReference("Motor_Insurance").orderByChild("insurance_company").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    dataList.clear()
                    for(item in snapshot.children){
                        var insurance = item.getValue(DbInsurance::class.java)
                        if(insurance !=null && insurance.insurance_status == "Available"){
                            dataList.add(insurance)
                        }
                    }
                    if(dataList.size > 0){
                        textNoInsurance.visibility = View.GONE
                        val adapter = DeleteInsuranceListAdapter(applicationContext,dataList)
                        listView.adapter = adapter
                    }else{
                        //textNoInsurance.visibility = View.VISIBLE
                        val builder = AlertDialog.Builder(this@DeleteInsuranceSuperAdmin)
                        builder.setTitle("Message")
                        builder.setMessage("Currently having NO or AVAILABLE status of Insurance!")
                        builder.setCancelable(true)
                        builder.setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        val alert = builder.create()
                        alert.show()
                        return
                        //Toast.makeText(applicationContext, "No Insurance Record(s).", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext,"No record(s).", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG).show()
            }
        })

        listView.onItemClickListener=
            AdapterView.OnItemClickListener { parent, view, position, id ->
                Log.i("LOG","Item clicked")
                var optionInsurance = view.findViewById<CheckBox>(R.id.optionDeleteInsurance_checkbox)
                optionInsurance.performClick()
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}