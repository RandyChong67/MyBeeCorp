package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.example.mybeecorp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.mybeecorp.classes.DbInsCompany
import com.example.mybeecorp.superadmin.adapters.InsCompanyListAdapter
import java.io.ByteArrayOutputStream

class InsCompanySuperAdminMainPage : AppCompatActivity() {

    private lateinit var progressBar: ProgressDialog
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    var instance = FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

    private lateinit var totalInsuranceCompany: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ins_company_super_admin_main_page)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Insurance Company")

        // Loading Indicator
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Loading...")
        progressBar.setCancelable(false)
        progressBar.show()

        totalInsuranceCompany = findViewById(R.id.totalInsuranceCompany_TextView)
        loadTotalInsCompany()

        //Add - Floating Action button
        val addBtn = findViewById<FloatingActionButton>(R.id.add_floatingActionButton)
        addBtn.setOnClickListener {
            val intent = Intent(this, AddInsCompanySuperAdmin::class.java)
            startActivity(intent)
            msgShow("Add Insurance Company Page")
        }

        //Get data from firebase
        val listView = findViewById<ListView>(R.id.InsuranceCompanySuperAdminListView)
        var dataList = arrayListOf<DbInsCompany>()
        var ref = instance.getReference("Insurance_Company").orderByChild("company_name").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    dataList.clear()
                    for(item in snapshot.children){
                        var insCompany = item.getValue(DbInsCompany::class.java)
                        //if(insCompany !=null && insCompany.company_status == "Available"){
                        dataList.add(insCompany!!)
                    }
                    if(dataList.size > 0){
                        if (progressBar.isShowing) progressBar.dismiss()
                        val adapter = InsCompanyListAdapter(applicationContext,dataList)
                        listView.adapter = adapter
                    }else{
                        if (progressBar.isShowing) progressBar.dismiss()
                        Toast.makeText(applicationContext,"No Insurance Company Record.",Toast.LENGTH_SHORT).show()
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
                val intent = Intent(this, ViewInsCompanySuperAdmin::class.java)
                val insCompany = dataList.get(position)
                intent.putExtra("companyId",insCompany.company_uid)
                intent.putExtra("companyName",insCompany.company_name)
                intent.putExtra("companyStatus",insCompany.company_status)
                intent.putExtra("companyPic",insCompany.company_logo)

                startActivity(intent)
            }
    }

    private fun loadTotalInsCompany() {
        //database = Firebase.database("https://dbpmt-699fe-default-rtdb.firebaseio.com/")
        database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Insurance_Company")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var noOfInsCompany = 0
                if(snapshot.exists()){
                    for(item in snapshot.children){
                        noOfInsCompany += 1
                    }
                    totalInsuranceCompany.text = noOfInsCompany.toString()
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

    //Actions on Click Menu Items(Delete)
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.delete_menuItem -> {
            val intent = Intent(this, DeleteInsCompanySuperAdmin::class.java)
            startActivity(intent)
            msgShow("Delete Insurance Company Page")
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


