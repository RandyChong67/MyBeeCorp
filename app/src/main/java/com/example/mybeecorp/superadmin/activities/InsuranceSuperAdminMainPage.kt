package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mybeecorp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.com.superadmin.InsuranceModule.adapters.InsuranceSuperAdminListAdapter
import com.example.mybeecorp.classes.DbInsurance

class InsuranceSuperAdminMainPage : AppCompatActivity() {

    private lateinit var progressBar: ProgressDialog
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    var instance = FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

    private lateinit var totalInsurance: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_super_admin_main_page)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("Insurance List")

        totalInsurance = findViewById(R.id.totalInsurance_TextView)
        loadTotalInsurance()

        // Loading Indicator
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Loading...")
        progressBar.setCancelable(false)
        progressBar.show()

        //Add - Floating Action button
        val addBtn = findViewById<FloatingActionButton>(R.id.add_floatingActionButton)
        addBtn.setOnClickListener {
            val intent = Intent(this, AddInsuranceSuperAdmin::class.java)
            startActivity(intent)
        }

        // Get Data - Insurance
        val listView = findViewById<ListView>(R.id.InsuranceSuperAdminListView)
        val textNoInsurance = findViewById<TextView>(R.id.NoInsurance_textView)
        var dataList = arrayListOf<DbInsurance>()
        var ref = instance.getReference("Motor_Insurance").orderByChild("insurance_company").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    dataList.clear()
                    for(item in snapshot.children){
                        var insurance = item.getValue(DbInsurance::class.java)
                        if(insurance !=null){
                            dataList.add(insurance)
                        }
                    }
                    if(dataList.size > 0){
                        textNoInsurance.visibility = View.GONE
                        if (progressBar.isShowing) progressBar.dismiss()
                        val adapter = InsuranceSuperAdminListAdapter(applicationContext,dataList)
                        listView.adapter = adapter
                    }else{
                        if (progressBar.isShowing) progressBar.dismiss()
                        textNoInsurance.visibility = View.VISIBLE
                        Toast.makeText(applicationContext,"You may go and ADD new insurance.",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext,"No record(s).",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "An error has occurred.")
            }

        })
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                Log.i("LOG","Item clicked")
                val intent = Intent(this, ViewInsuranceInformationSuperAdmin::class.java)
                var insurance = dataList.get(position)

                intent.putExtra("insuranceId",insurance.insurance_uid)
                intent.putExtra("insuranceName",insurance.insurance_name)
                intent.putExtra("insuranceCompany",insurance.insurance_company)
                intent.putExtra("insuranceClass",insurance.insurance_class)
                intent.putExtra("insuranceStatus",insurance.insurance_status)
                startActivity(intent)
            }
    }

    private fun loadTotalInsurance() {
        database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        //database = Firebase.database("https://dbpmt-699fe-default-rtdb.firebaseio.com/")
        reference = database.getReference("Motor_Insurance")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var noOfInsurance = 0
                if(snapshot.exists()){
                    for(item in snapshot.children){
                        noOfInsurance += 1
                    }
                    totalInsurance.text = noOfInsurance.toString()
                }else{
                    Log.i("Information","No record(s).")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "An error occurs.")
            }
        })
    }

    //Top Bar - Menu Option(Delete)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //Actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.delete_menuItem -> {
            val intent = Intent(this, DeleteInsuranceSuperAdmin::class.java)
            startActivity(intent)
            msgShow("Delete Insurance Page")
            true
        }
        else -> { super.onOptionsItemSelected(item)
        }
    }
    private fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    //Back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}