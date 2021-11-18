package com.example.mybeecorp.insurancecompanyadmin.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.mybeecorp.R
import com.example.mybeecorp.insurancecompanyadmin.adapters.ViewDetailInsItemCoveredCAListAdapter
import com.example.mybeecorp.classes.DbInsurance
import com.example.mybeecorp.classes.DbItemCovered

class ViewInsuranceInformationCA : AppCompatActivity() {

    var instance = FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_insurance_information_ca)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("Detail Insurance Information")

        val insId = intent.getStringExtra("insuranceId")
        val insCompany = intent.getStringExtra("insuranceCompany")

        val companyLogo = findViewById<ImageView>(R.id.companyLogo_ImageView)
        val insuranceName = findViewById<TextView>(R.id.insuranceName_TextView)
        val insuranceCompany = findViewById<TextView>(R.id.insCompanyName_TextView)
        val insuranceClass = findViewById<TextView>(R.id.insuranceClass_TextView)
        val insuranceStatus = findViewById<TextView>(R.id.insuranceStatus_TextView)

        var myRef = instance.getReference("Motor_Insurance").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(item in snapshot.children){
                        var insurance = item.getValue(DbInsurance::class.java)
                        if(insurance  !=null && insurance.insurance_uid.equals(insId)){
                            insuranceName.text = insurance.insurance_name
                            insuranceCompany.text = insurance.insurance_company
                            insuranceClass.text = insurance.insurance_class
                            insuranceStatus.text = insurance.insurance_status
                            insuranceStatus.setTextColor(Color.parseColor( if(insurance.insurance_status == "Available") "#27AE60" else "#FF0000"))
                        }
                    }
                }else{
                    Toast.makeText(applicationContext,"No record(s).",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "An error has occurred.")
            }

        })
        //Get Item Covered Data & Company Logo from firebase
        val listView = findViewById<ListView>(R.id.ViewDetailItemCoveredCAListView)
        val textNoItemCovered = findViewById<TextView>(R.id.NoItemCovered_TextView)
        var dataList = arrayListOf<DbItemCovered>()
        var ref = instance.getReference("Item_Covered").child(insId!!).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    dataList.clear()
                    for(item in snapshot.children){
                        var itemCovered = item.getValue(DbItemCovered::class.java)
                        if(itemCovered !=null){
                            dataList.add(itemCovered)
                        }
                    }
                    if(dataList.size > 0){
                        textNoItemCovered.visibility = View.GONE
                        val adapter = ViewDetailInsItemCoveredCAListAdapter(applicationContext,dataList)
                        listView.adapter = adapter
                    }else{
                        textNoItemCovered.visibility = View.VISIBLE
                        //Toast.makeText(applicationContext,"No Item Covered(s).",Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext,"You might choose to EDIT in order to add some item covered accordingly.",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "An error has occurred.")
            }
        })

        val companyRef = instance.getReference("/Insurance_Company")
            .orderByChild("company_name").equalTo(insCompany).limitToFirst(1)
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(item in snapshot.children){
                            Glide.with(companyLogo.context).load(item.child("company_logo").value.toString()).into(companyLogo)
                        }
                    }else{
                        Log.e("Error","Unable to retrieve company information.")
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

    //Top Bar - Menu Option(Edit)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.edit_menu, menu)
        return true
    }
    //Actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.edit_menuItem -> {
            val insId = intent.getStringExtra("insuranceId")
            val insName = intent.getStringExtra("insuranceName")
            val insCompany = intent.getStringExtra("insuranceCompany")
            val insClass = intent.getStringExtra("insuranceClass")
            val insStatus = intent.getStringExtra("insuranceStatus")
            val intent = Intent(this, EditInsuranceCA::class.java)
            intent.putExtra("insuranceId",insId)
            intent.putExtra("insuranceName",insName)
            intent.putExtra("insuranceCompany", insCompany)
            intent.putExtra("insuranceClass",insClass)
            intent.putExtra("insuranceStatus",insStatus)
            startActivity(intent)
            msgShow("Edit Insurance Information Page.")
            true
        }
        else -> { super.onOptionsItemSelected(item)
        }
    }

    private fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}