package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase
import my.com.customer.classes.Voucher
import java.text.SimpleDateFormat
import java.util.*


class Create_Voucher : AppCompatActivity() {


    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_voucher)
        this.supportActionBar?.title = "Create Voucher"
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val voucherCodeEt: EditText = findViewById(R.id.voucherCode_editText)
        val voucherDescriptionEt: EditText = findViewById(R.id.voucherDescription_editText)
        val voucherDiscountEt: EditText = findViewById(R.id.voucherDiscount_editText)
        val voucherExpiryDateEt: TextInputLayout = findViewById(R.id.expiryDate_editText)
        val voucherStatusddl = resources.getStringArray(R.array.VoucherStatus)
        val createVoucherbtn: Button = findViewById(R.id.updateVoucher_btn)
        val backCreateVoucherbtn: Button = findViewById(R.id.backUpdateVoucher_btn)

        val voucherStatusspinner = findViewById<Spinner>(R.id.voucherStatus_spinner)
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        //Voucher Status
        if (voucherStatusspinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, voucherStatusddl
            )
            voucherStatusspinner.adapter = adapter
        }
        //Calendar
        voucherExpiryDateEt.setStartIconOnClickListener {
            val dateRangePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Expiry Date")
                .build()
            dateRangePicker.addOnPositiveButtonClickListener {
                var timestamp = dateRangePicker.selection
                var format = SimpleDateFormat("dd/MM/yyyy")
                if (timestamp != null) {

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

        createVoucherbtn.setOnClickListener {
            progressBar = ProgressDialog(this)
            progressBar.setMessage("Loading...")
            progressBar.setCancelable(false)
            progressBar.show()

            var dateFormat = SimpleDateFormat("dd/MM/yyyy")
            var currentDate = dateFormat.format(Calendar.getInstance().time)
            val voucherCode = voucherCodeEt.text.toString().trim()
            val voucherDescription = voucherDescriptionEt.text.toString().trim()
            val voucherDiscount = voucherDiscountEt.text.toString()
            val voucherExpiryDate = voucherExpiryDateEt.editText?.text.toString()
            var voucherStatus = voucherStatusspinner.getSelectedItem().toString()

            if (voucherCode.isNotEmpty() && voucherCode.length <= 10) {
                if (voucherDescription.isNotEmpty()) {
                    if (voucherDiscount.isNotEmpty() && voucherDiscount.length < 3) {
                        if (voucherExpiryDate.isNotEmpty()) {

                            val voucherRef = database.getReference("Voucher")
                            val voucheruid = voucherRef.push().key

                            val voucherDetails = Voucher(
                                voucheruid!!,
                                voucherCode,
                                voucherDescription,
                                voucherDiscount.toInt(),
                                voucherExpiryDate,
                                voucherStatus.toString()
                            )

                            if (voucheruid != null) {
                                voucherRef.child(voucheruid).setValue(voucherDetails)
                                    .addOnCompleteListener {
                                        if (progressBar.isShowing) progressBar.dismiss()
                                        Toast.makeText(
                                            applicationContext,
                                            "Voucher insert Successfully",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                    }
                            }
                        } else {
                            if (progressBar.isShowing) progressBar.dismiss()
                            Toast.makeText(
                                applicationContext,
                                "Please select Expiry Date",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    } else if (voucherDiscount.isEmpty()) {
                        if (progressBar.isShowing) progressBar.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "Please enter Voucher Discount",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (voucherDiscount.length > 2) {
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
                        "Please enter Voucher Description",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else if (voucherCode.isEmpty()) {
                if (progressBar.isShowing) progressBar.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Please enter Voucher Code",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else if (voucherCode.length > 11) {
                if (progressBar.isShowing) progressBar.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Voucher Code is too long",
                    Toast.LENGTH_LONG
                )
                    .show()

            }
        }

        backCreateVoucherbtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}