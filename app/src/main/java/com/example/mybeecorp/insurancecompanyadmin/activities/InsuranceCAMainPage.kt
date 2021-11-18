package com.example.mybeecorp.insurancecompanyadmin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.mybeecorp.R
import com.example.mybeecorp.insurancecompanyadmin.adapters.InsuranceListCAAdapter
import com.example.mybeecorp.classes.DbInsurance

class InsuranceCAMainPage : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    var instance = FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

    private lateinit var totalInsurance: TextView
    private var company_name = ""
    private var dataList = arrayListOf<DbInsurance>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_camain_page)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Insurance List"

        totalInsurance = findViewById(R.id.totalInsurance_TextView)
        val sortButton = findViewById<ImageView>(R.id.sortByAlpha_icon)
        //loadTotalInsurance(company_name)

        //Add - Floating Action button
        val addBtn = findViewById<FloatingActionButton>(R.id.add_floatingActionButton)
        addBtn.setOnClickListener {
            val intent = Intent(this, AddInsuranceCA::class.java)
            startActivity(intent)
        }
        //Fragment for Insurance List

        loadUID()
    }

    private fun loadUID() {
        val uid = FirebaseAuth.getInstance().uid // ----------------------------------Hardcode - Insurance Company Name !!!
        database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("User")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if(data.child("user_uid").value.toString() == uid){
                        company_name = data.child("ins_company_name").value.toString()

                    }
                }
                loadCompany()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }
        })
    }

    private fun loadCompany() {
        val textNoInsurance = findViewById<TextView>(R.id.NoInsurance_textView)

        reference = database.getReference("Motor_Insurance")
        reference.orderByChild("insurance_company").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    dataList.clear()
                    for(item in snapshot.children){
                        if(item.child("insurance_company").value.toString() == company_name){
                            var insurance = item.getValue(DbInsurance::class.java)
                            dataList.add(insurance!!)
                            loadTotalInsurance(company_name)
                        }
                    }

                    if(dataList.size > 0){
                        textNoInsurance.visibility = View.GONE
                        val adapter = InsuranceListCAAdapter(applicationContext,dataList)
                        val listView: ListView = findViewById(R.id.InsuranceCompanyAdminListView)
                        listView.adapter = adapter
                        listView.onItemClickListener =
                            AdapterView.OnItemClickListener { parent, view, position, id ->
                                Log.i("LOG","Item clicked")
                                val intent = Intent(this@InsuranceCAMainPage, ViewInsuranceInformationCA::class.java)
                                var insurance = dataList.get(position)

                                intent.putExtra("insuranceId",insurance.insurance_uid)
                                intent.putExtra("insuranceName",insurance.insurance_name)
                                intent.putExtra("insuranceCompany",insurance.insurance_company)
                                intent.putExtra("insuranceClass",insurance.insurance_class)
                                intent.putExtra("insuranceStatus",insurance.insurance_status)
                                startActivity(intent)
                            }
                    }else{
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
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun loadTotalInsurance(company_name: String) {
        database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Motor_Insurance")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var noOfInsurance = 0
                if(snapshot.exists()){
                    for(item in snapshot.children){
                        if(item.child("insurance_company").value.toString() == company_name){
                            noOfInsurance += 1
                        }
                        totalInsurance.text = noOfInsurance.toString()
                    }
                }else{
                    Log.i("Informations","No record(s).")
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
        inflater.inflate(R.menu.delete_menu, menu)
        return true
    }
    //actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.delete_menuItem -> {
            val intent = Intent(this, DeleteInsuranceCA::class.java)
            startActivity(intent)
            Log.i("Information","User had clicked delete menu icon button.")
            true
        }
        else -> { super.onOptionsItemSelected(item)
        }
    }

}