package com.example.mybeecorp.member.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.mybeecorp.R
import com.example.mybeecorp.member.adapters.CompanyListRecyclerAdapter
import com.example.mybeecorp.member.fragments.SubtitleBar
import my.com.customer.classes.Insurance_Company

class InsuranceCompany_MainPage : AppCompatActivity() {

    private lateinit var progressBar: ProgressDialog
    var instance =
        FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_company_main_page)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.title = "Buy Insurance"

        var fragment = SubtitleBar.newInstance("List of Insurance Company")
        getSupportFragmentManager()!!.beginTransaction().replace(R.id.fragment_view, fragment)
            .commit()

        progressBar = ProgressDialog(this)
        progressBar.setMessage("Loading...")
        progressBar.setCancelable(false)
        progressBar.show()

        //Add another Fragment inside
        var recyclerView = findViewById<RecyclerView>(R.id.recycler_view_company)
        var dataList = arrayListOf<Insurance_Company>()
        var ref = instance.getReference("Insurance_Company").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { //Snapshot - return 回来的东西
                if (snapshot.exists()) {
                    dataList.clear()
                    for (item in snapshot.children) {
                        if (item.child("company_status").value.toString() == "Available"){
                            var insCompany = item.getValue(Insurance_Company::class.java)
                            if (insCompany != null) {
                                dataList.add(insCompany)
                            }
                        }
                    }
                    if (dataList.size > 0) {
                        if (progressBar.isShowing) progressBar.dismiss()
                        recyclerView.layoutManager = LinearLayoutManager(this@InsuranceCompany_MainPage)
                        recyclerView.adapter = CompanyListRecyclerAdapter(dataList)
                    } else {
                        if (progressBar.isShowing) progressBar.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "No Insurance Company Record.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (progressBar.isShowing) progressBar.dismiss()
                    Toast.makeText(applicationContext, "No record.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (progressBar.isShowing) progressBar.dismiss()
                onBackPressed()
            }
        })

//        listView.onItemClickListener =
//            AdapterView.OnItemClickListener { parent, view, position, id ->
//                Log.i("LOG", "Item clicked")
//                val intent = Intent(this, ViewMoreCompanyInsurance::class.java)
//                var insCompany = dataList.get(position)
//
//                val stream = ByteArrayOutputStream()
//                insCompany.bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
//                val byteArray: ByteArray = stream.toByteArray()
//                intent.putExtra("companyId", insCompany.company_uid)
//                intent.putExtra("companyName", insCompany.company_name)
//                intent.putExtra("companyPic", byteArray)
//
//                startActivity(intent)
//            }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}