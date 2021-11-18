package com.example.mybeecorp.insurancecompanyadmin.activities

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.mybeecorp.R
import com.example.mybeecorp.insurancecompanyadmin.adapters.OptionDeleteInsuranceCAListAdapter
import com.example.mybeecorp.classes.DbInsurance

class DeleteInsuranceCA : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    var instance = FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

    var dataList = arrayListOf<DbInsurance>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_delete_insurance_ca)


        val listView = findViewById<ListView>(R.id.DeleteInsuranceListView)
        val resetBtn: Button = findViewById(R.id.back_button)
        val deleteBtn: Button = findViewById(R.id.save_button)
        //val sortButton = findViewById<ImageView>(R.id.sortByAlpha_icon)

        resetBtn.setOnClickListener{
            var index = 0
            do{
                var view = getViewByPosition(index,listView)
                var checkBox = view?.findViewById<CheckBox>(R.id.optionDeleteInsurance_checkbox)
                Log.i("Task",view?.findViewById<TextView>(R.id.insuranceName_TextView)?.text.toString())
                checkBox?.isChecked = false
                (listView.adapter as OptionDeleteInsuranceCAListAdapter).checkedList.clear()
                index++
            }while (listView.count > index)
        }
        deleteBtn.setOnClickListener{
            Log.i("test","delete button clicked")
            var list = mutableMapOf<String,Any?>()
            var checkList = (listView.adapter as OptionDeleteInsuranceCAListAdapter).checkedList
            if(checkList.size > 0){
                val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@DeleteInsuranceCA)
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
                        //Toast.makeText(applicationContext,"Insurance(s) deleted.", Toast.LENGTH_SHORT).show()
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Message")
                        builder.setMessage("Insurance deleted successfully!")
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
        var insurance_companyName = ""  //hardcode Insurance Company Name !!!
        val textNoInsurance= findViewById<TextView>(R.id.NoInsurance_textView)
        val uid = FirebaseAuth.getInstance().uid
        database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("User")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if (data.child("user_uid").value.toString() == uid) {
                        var ref =
                            instance.getReference("Motor_Insurance").orderByChild("insurance_name")
                                .addValueEventListener(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            dataList.clear()
                                            for (item in snapshot.children) {
                                                var insurance =
                                                    item.getValue(DbInsurance::class.java)
                                                if (insurance != null && insurance.insurance_company == data.child("ins_company_name").value.toString() && insurance.insurance_status == "Available") {
                                                    dataList.add(insurance)
                                                }
                                            }
                                            if (dataList.size > 0) {
                                                textNoInsurance.visibility = View.GONE
                                                val adapter = OptionDeleteInsuranceCAListAdapter(applicationContext, dataList)
                                                listView.adapter = adapter
                                            } else {
                                                //Toast.makeText(applicationContext, "No Insurance Record(s).", Toast.LENGTH_SHORT).show()
                                                val builder = AlertDialog.Builder(this@DeleteInsuranceCA)
                                                builder.setTitle("Warmly Message")
                                                builder.setMessage("Currently having NO or AVAILABLE status of Insurance. You might go and ADD some insurances.")
                                                builder.setCancelable(true)
                                                builder.setPositiveButton("OK") { dialog, _ ->
                                                    dialog.dismiss()
                                                }
                                                val alert = builder.create()
                                                alert.show()
                                                return
                                            }
                                        } else {
                                            Toast.makeText(applicationContext, "No record(s).", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG).show()
                                    }
                                })
                        listView.onItemClickListener =
                            AdapterView.OnItemClickListener { parent, view, position, id ->
                                Log.i("LOG", "Item clicked")
                                val optionInsurance = view.findViewById<CheckBox>(R.id.optionDeleteInsurance_checkbox)
                                optionInsurance.performClick()
                            }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }
        })
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