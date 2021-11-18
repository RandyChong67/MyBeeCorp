package com.example.mybeecorp.superadmin.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.mybeecorp.classes.DbInsCompany

class ViewInsCompanySuperAdmin : AppCompatActivity() {

    var instance = FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_ins_company_super_admin)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("View Insurance Company Information")

        val companyId = intent.getStringExtra("companyId")

        val imageView = findViewById<ImageView>(R.id.insuranceCompanyLogo_imageView)
        val companyName = findViewById<TextView>(R.id.InsuranceCompanyName_TextView)
        val companyStatus = findViewById<TextView>(R.id.insuranceCompanyStatus_TextView)

        var ref = instance.getReference("Insurance_Company").addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(item in snapshot.children){
                        var insCompany = item.getValue(DbInsCompany::class.java)
                        if(insCompany  !=null && insCompany.company_uid.equals(companyId)){
                            companyName.text = insCompany.company_name
                            companyStatus.text = insCompany.company_status

                            companyStatus.setTextColor(Color.parseColor(
                                if(insCompany.company_status == "Available") "#27AE60" else "#FF0000")
                            )
                            Glide.with(imageView.context).load(insCompany.company_logo).into(imageView)
                        }
                    }
                }else{
                    Toast.makeText(applicationContext,"No record(s).",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "An error has occurred.", error.toException())
            }
        })

    }

    //Back
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
            val picture = intent.getStringExtra("companyPic")
            val name = intent.getStringExtra("companyName")
            val status = intent.getStringExtra("companyStatus")
            val companyId = intent.getStringExtra("companyId")
            val intent = Intent(this, EditInsCompanySuperAdmin::class.java)
            intent.putExtra("companyPic",picture)
            intent.putExtra("companyName",name )
            intent.putExtra("companyStatus", status)
            intent.putExtra("companyId",companyId)
            startActivity(intent)
            msgShow("Edit Insurance Company Page")
            true
        }
        else -> { super.onOptionsItemSelected(item)
        }
    }
    private fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}