package com.example.mybeecorp.superadmin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.superadmin.adapters.voucherListAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DatabaseReference
import my.com.customer.classes.Voucher

class View_Voucher : AppCompatActivity() {

    private lateinit var voucherListRef : DatabaseReference
    private lateinit var voucherListRecyclerView: RecyclerView
    private lateinit var voucherListArrayList : ArrayList<Voucher>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_voucher)
        this.supportActionBar?.title = "View Voucher"
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val voucherSearchET = findViewById<EditText>(R.id.searchVoucher_editText)
        val voucherSearch = voucherSearchET.text.toString()

        voucherListRecyclerView = findViewById(R.id.voucherList_rv)
        voucherListRecyclerView.layoutManager = LinearLayoutManager(this)
        voucherListRecyclerView.setHasFixedSize(true)

        voucherListArrayList = arrayListOf<Voucher>()
        getVoucherListData(voucherSearch)

        voucherSearchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val voucherSearch = voucherSearchET.text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val voucherSearch = voucherSearchET.text.toString()
                getVoucherListData(voucherSearch)

            }

            override fun afterTextChanged(s: Editable?) {
                val voucherSearch = voucherSearchET.text.toString()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getVoucherListData(voucherSearch:String){

        voucherListRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Voucher")

        if(voucherSearch == "") {
            voucherListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    voucherListArrayList.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                                val voucherList = i.getValue(Voucher::class.java)
                                voucherListArrayList.add(voucherList!!)
                        }
                        voucherListRecyclerView.adapter = voucherListAdapter(voucherListArrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
        else{
            voucherListRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    voucherListArrayList.clear()
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            var searchName = i.child("voucher_code").value.toString()
                            if(searchName.contains(voucherSearch, ignoreCase = true)) {
                                val voucherList = i.getValue(Voucher::class.java)
                                voucherListArrayList.add(voucherList!!)
                            }
                        }

                        voucherListRecyclerView.adapter = voucherListAdapter(voucherListArrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }


    }
}