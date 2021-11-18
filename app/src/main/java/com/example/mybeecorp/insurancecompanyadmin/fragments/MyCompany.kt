package com.example.mybeecorp.insurancecompanyadmin.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.mybeecorp.R
import com.example.mybeecorp.insurancecompanyadmin.activities.EditMyCompanyCA
import java.io.ByteArrayOutputStream

class MyCompany : Fragment() {

    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    var instance =
        FirebaseDatabase.getInstance("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

    private lateinit var progressBar: ProgressDialog
    private var company_uid = ""
    private var imageUri: Uri? = null
    private var company_name = ""
    private var company_status = ""
    private lateinit var InsuranceCompanyLogo: ImageView
    private lateinit var insCompanyUID: TextView
    private lateinit var insCompanyName: TextView
    private lateinit var insCompanyStatus: TextView
    private lateinit var totalCompanyInsurance: TextView
    private lateinit var totalCompanyAdmin: TextView
    private lateinit var editTextView: TextView
    private lateinit var editIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_company, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        insCompanyUID = view.findViewById(R.id.companyId_TextView)
        InsuranceCompanyLogo = view.findViewById(R.id.insuranceCompanyLogo_imageView)
        insCompanyName = view.findViewById(R.id.InsuranceCompanyName_TextView)
        insCompanyStatus = view.findViewById(R.id.insuranceCompanyStatus_TextView)
        totalCompanyInsurance = view.findViewById(R.id.totalInsurance_TextView)
        totalCompanyAdmin = view.findViewById(R.id.totalCompanyAdmin_TextView)

        editTextView = view.findViewById(R.id.edit_TextView)
        editIcon = view.findViewById(R.id.editMyCompanyInformation_Icon)

        //先call load data function，把那些data store进variable
        loadData()
        loadTotalInsurance(company_name)
        loadTotalCompanyAdmin(company_name)

        editTextView.setOnClickListener {
            val intent = Intent(view.context, EditMyCompanyCA::class.java)
            intent.putExtra("companyId", company_uid)
            intent.putExtra("companyName", company_name)
            intent.putExtra("companyStatus", company_status)

            view.context.startActivity(intent)
        }

        // Loading indicator
        progressBar = ProgressDialog(view.context)
        progressBar.setMessage("Loading...")
        progressBar.setCancelable(false)
        progressBar.show()

        editIcon.setOnClickListener {
            val intent = Intent(view.context, EditMyCompanyCA::class.java)
            val stream = ByteArrayOutputStream()

            intent.putExtra("companyId", company_uid)
            intent.putExtra("companyName", company_name)
            intent.putExtra("companyStatus", company_status)
            view.context.startActivity(intent)
        }
    }

    private fun loadData() {
        val user_Id =
            FirebaseAuth.getInstance().uid //--------------------------------------- HardCoded
        //database = Firebase.database("https://dbinscompanyadmin-default-rtdb.firebaseio.com/")
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("User")
        var i = 0
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        //if (i == 0){
                        if (item.child("user_uid").value.toString() == user_Id) {
                            company_name = item.child("ins_company_name").value.toString()
                        } else {
                            Log.e("Error", "Not found.")
                        }
                        //}
                    }
                } else {
                    Log.i("Informations", "No record(s).")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "An error occurs.")
            }
        })
        reference = database.getReference("Insurance_Company")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        if (data.child("company_name").value.toString() == company_name) {
                            company_uid =
                                data.child("company_uid").value.toString()
                            company_status =
                                data.child("company_status").value.toString()
                            Glide.with(InsuranceCompanyLogo.context)
                                .load(data.child("company_logo").value.toString())
                                .placeholder(R.drawable.ic_launcher_background)
                                .circleCrop()
                                .error(R.drawable.ic_launcher_background)
                                .into(InsuranceCompanyLogo)
                        }
                        //Dismiss loading indicator
                        if (progressBar.isShowing) progressBar.dismiss()
                    }
                    insCompanyUID.text = company_uid
                    insCompanyStatus.text = company_status
                    insCompanyName.text = company_name
                    insCompanyStatus.setTextColor(
                        Color.parseColor(
                            if (company_status == "Available") "#27AE60" else "#FF0000"
                        )
                    )
                    totalCompanyInsurance.text
                    totalCompanyAdmin.text

                    loadTotalInsurance(company_name)
                    loadTotalCompanyAdmin(company_name)
                } else {
                    Log.i("Informations", "No record(s).")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "An error occurs.")
            }
        })
    }


    private fun loadTotalInsurance(company_name: String) {
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("Motor_Insurance")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var noOfInsurance = 0
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        if (item.child("insurance_company").value.toString() == company_name) {
                            noOfInsurance += 1
                        }
                        totalCompanyInsurance.text = noOfInsurance.toString()
                    }
                    //Dismiss loading indicator
                    if (progressBar.isShowing) progressBar.dismiss()
                } else {
                    Log.i("Informations", "No record(s).")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "An error occurs.")
            }
        })
    }

    private fun loadTotalCompanyAdmin(company_name: String) {
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        reference = database.getReference("User")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var noOfCompanyAdmin = 0
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        if (item.child("ins_company_name").value.toString() == company_name) {
                            noOfCompanyAdmin += 1
                        }
                        totalCompanyAdmin.text = noOfCompanyAdmin.toString()
                    }
                    //Dismiss loading indicator
                    if (progressBar.isShowing) progressBar.dismiss()
                } else {
                    Log.i("Informations", "No record(s).")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "An error occurs.")
            }
        })
    }

}


