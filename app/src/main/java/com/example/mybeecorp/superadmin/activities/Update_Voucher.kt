package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.com.customer.classes.Voucher
import java.text.SimpleDateFormat
import java.util.*

class Update_Voucher : AppCompatActivity() {
    private lateinit var progressBar: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_voucher)
        this.supportActionBar?.title = "Update Voucher"
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val voucherStatusddl = resources.getStringArray(R.array.VoucherStatus)
        val voucherStatusspinner = findViewById<Spinner>(R.id.voucherStatus_spinner)
        val backUpdateVoucherbtn = findViewById<Button>(R.id.backUpdateVoucher_btn)

        //Voucher Status
        if (voucherStatusspinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, voucherStatusddl
            )
            voucherStatusspinner.adapter = adapter
        }

        //val voucherID = "-Mj9r5fgeH6Rwtklm4tM"
        val voucherID = intent.getStringExtra("voucherID").toString()
        //val voucherID = FirebaseAuth.getInstance().uid
        loadVoucher(voucherID!!)

        backUpdateVoucherbtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadVoucher(voucherID: String) {
        val voucherCodeEt: EditText = findViewById(R.id.voucherCode_editText)
        val voucherDescriptionEt: EditText = findViewById(R.id.voucherDescription_editText)
        val voucherDiscountEt: EditText = findViewById(R.id.voucherDiscount_editText)
        val voucherExpiryDateEt: TextInputLayout = findViewById(R.id.expiryDate_editText)
        val voucherStatusddl = resources.getStringArray(R.array.VoucherStatus)
        val updateVoucherbtn: Button = findViewById(R.id.updateVoucher_btn)

        val voucherStatusspinner = findViewById<Spinner>(R.id.voucherStatus_spinner)

        updateVoucherbtn.setOnClickListener {
            progressBar = ProgressDialog(this)
            progressBar.setMessage("Loading...")
            progressBar.setCancelable(false)
            progressBar.show()
            updateVoucher(voucherID)
        }

        //Voucher Status
        if (voucherStatusspinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, voucherStatusddl
            )
            voucherStatusspinner.adapter = adapter
        }

        voucherExpiryDateEt.setStartIconOnClickListener {
            val dateRangePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Expiry Date")
                .build()
            dateRangePicker.addOnPositiveButtonClickListener {
                var timestamp = dateRangePicker.selection
                if (timestamp != null) {
                    var format = SimpleDateFormat("dd/MM/yyyy")
                    voucherExpiryDateEt.editText?.setText(format.format(timestamp!!))
                }
                if (Date().after(Date(timestamp!!))) {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Cannot select date before current date")
                    builder.setTitle("Error")
                    builder.setCancelable(false)
                    builder.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        voucherExpiryDateEt.editText?.setText("")
                    }
                    val alert = builder.create()
                    alert.show()
                }
            }
            dateRangePicker.show(supportFragmentManager, "DatePicker")
            Log.i("Text", "Onclick")
        }

        var voucherRef =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Voucher")

        voucherRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        if (i.child("voucher_uid").value.toString() == voucherID) {

                            voucherCodeEt.setText(i.child("voucher_code").value.toString())
                            voucherDescriptionEt.setText(i.child("voucher_description").value.toString())
                            voucherDiscountEt.setText(i.child("voucher_discount").value.toString())
                            voucherExpiryDateEt.getEditText()
                                ?.setText(i.child("voucher_expiry_date").value.toString())
                            if (i.child("voucher_status").value != "Available") {
                                voucherStatusspinner.setSelection(1)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun updateVoucher(voucherID: String) {
        val voucherCodeEt: EditText = findViewById(R.id.voucherCode_editText)
        val voucherDescriptionEt: EditText = findViewById(R.id.voucherDescription_editText)
        val voucherDiscountEt: EditText = findViewById(R.id.voucherDiscount_editText)
        val voucherExpiryDateEt: TextInputLayout = findViewById(R.id.expiryDate_editText)
        //val voucherStatusddl = resources.getStringArray(R.array.VoucherStatus)
        val voucherStatusspinner = findViewById<Spinner>(R.id.voucherStatus_spinner)

        val voucherRef =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Voucher")

        if (voucherCodeEt.text.toString().trim().isNotEmpty()) {
            if (voucherDescriptionEt.text.toString().trim().isNotEmpty()) {
                if (voucherDiscountEt.text.toString().trim().isNotEmpty()) {
                    if (voucherExpiryDateEt.editText?.text.toString().trim().isNotEmpty()) {


                        val updateVoucher = Voucher(
                            voucherID,
                            voucherCodeEt.text.toString(),
                            voucherDescriptionEt.text.toString(),
                            voucherDiscountEt.text.toString().toInt(),
                            voucherExpiryDateEt.editText?.text.toString(),
                            voucherStatusspinner.selectedItem.toString()
                        )

                        voucherRef.child(voucherID).setValue(updateVoucher)
                            .addOnCompleteListener {
                                if (progressBar.isShowing) progressBar.dismiss()
                                Toast.makeText(
                                    applicationContext,
                                    "Voucher updated successfully",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                    } else {
                        if (progressBar.isShowing) progressBar.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "Please Select Date",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        return
                    }
                } else if (voucherDiscountEt.text.toString().trim().isEmpty()) {
                    if (progressBar.isShowing) progressBar.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Please enter Voucher Discount",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (voucherDiscountEt.text.toString().trim().length > 2) {
                    if (progressBar.isShowing) progressBar.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Voucher Discount only can two digit",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                if (progressBar.isShowing) progressBar.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Please key in Discount Description",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
        } else if (voucherCodeEt.text.toString().trim().isEmpty()) {
            if (progressBar.isShowing) progressBar.dismiss()
            Toast.makeText(
                applicationContext,
                "Please enter Voucher Code",
                Toast.LENGTH_LONG
            )
                .show()
        } else if (voucherCodeEt.text.toString().trim().length > 11) {

            if (progressBar.isShowing) progressBar.dismiss()
            Toast.makeText(
                applicationContext,
                "Voucher Code is too long",
                Toast.LENGTH_LONG
            )
                .show()

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
