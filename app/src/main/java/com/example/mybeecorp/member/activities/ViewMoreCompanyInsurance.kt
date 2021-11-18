package com.example.mybeecorp.member.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.mybeecorp.R
import com.example.mybeecorp.member.adapters.CompanyInsuranceListAdapter
import com.example.mybeecorp.member.fragments.SubtitleBar
import my.com.customer.classes.Insurance_Company
import my.com.customer.classes.Motor_Insurance

class ViewMoreCompanyInsurance : AppCompatActivity() {

    private var companyList = mutableListOf<Insurance_Company>()
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var insuranceList = mutableListOf<Motor_Insurance>()
    private var companyName: String = ""
    private var companyID: String = ""
    private lateinit var companyLogo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more_company_insurance)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("More Insurance")
        companyID = intent.getStringExtra("companyId")!!
        companyLogo = intent.getStringExtra("companyPic")!!
        companyName = intent.getStringExtra("companyName")!!

        val imageView = findViewById<ImageView>(R.id.companyLogo_imageView)
        val companyNameText = findViewById<TextView>(R.id.companyName_TextView)

        Glide.with(imageView.context).load(companyLogo).into(imageView)
//        val bmp = BitmapFactory.decodeByteArray(companyLogo, 0, companyLogo!!.size)
//        imageView.setImageBitmap(bmp)
        companyNameText.text = companyName


        database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        loadListView()
    }

    private fun loadListView() {
        //Fragment of Subtitle Bar
        var fragment = SubtitleBar.newInstance("Insurance List:")
        getSupportFragmentManager()!!.beginTransaction().replace(R.id.fragment_view,fragment).commit()

        var listView = findViewById<ListView>(R.id.CompanyInsuranceListView)
        val textNoInsurance = findViewById<TextView>(R.id.NoInsurance_textView)

        reference = database.getReference("Motor_Insurance")
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                insuranceList.clear()
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        if(data.child("insurance_company").value.toString() == companyName){
                            var insurance = data.getValue(Motor_Insurance::class.java)
                            insuranceList.add(insurance!!)
                            textNoInsurance.visibility = View.GONE
                        }
                    }
                    var adapter = CompanyInsuranceListAdapter(this@ViewMoreCompanyInsurance,insuranceList)
                    listView.adapter = adapter
                    listView.onItemClickListener =
                        AdapterView.OnItemClickListener { parent, view, position, id ->
                            Log.i("LOG","Item clicked")
                            val intent = Intent(this@ViewMoreCompanyInsurance, SelectItemCovered::class.java)
                            intent.putExtra("company_name", companyName)
                            intent.putExtra("company_uid", companyID)
                            intent.putExtra("insurance_name", insuranceList[position].insurance_name)
                            intent.putExtra("insurance_uid", insuranceList[position].insurance_uid)
                            intent.putExtra("insurance_class", insuranceList[position].insurance_class)
                            startActivity(intent)
                        }
                }else {
                    textNoInsurance.visibility = View.VISIBLE
                    Toast.makeText(applicationContext, "No Insurance Yet. You may add new insurance now.", Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Error","An error occurs.")
            }
        })
    }

    //Back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}