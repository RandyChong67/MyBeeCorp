package com.example.mybeecorp.member.activities

import android.Manifest
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Referral
import com.example.mybeecorp.classes.Spin_Wheel
import com.example.mybeecorp.classes.Vehicle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.com.customer.classes.Insurance_Bought
import my.com.customer.classes.Insurance_Bought_Item_Covered
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextInt

class PaymentGatewayActivity : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var textBankCardNum: TextView
    private lateinit var textPIN: TextView
    private lateinit var textOTP: TextView
    private lateinit var buttonOTP: Button
    private lateinit var buttonConfirm: Button
    private var insuranceName: String = ""
    private var insuranceUID: String = ""
    private var insuranceClass: String = ""
    private var insuranceCompanyName: String = ""
    private var insuranceCompanyUID: String = ""
    private var plateNum: String = ""
    private var vehicleType: String = ""
    private var vehicleBrand: String = ""
    private var vehicleModel: String = ""
    private var vehicleVariant: String = ""
    private var vehicleYear: String = ""
    private var inviterUID: String? = ""
    private var variantPrice: Double = 0.0
    private var itemCoveredName: MutableList<String> = mutableListOf()
    private var itemCoveredPrice: MutableList<String> = mutableListOf()
    private var yearlyPayment: Double = 0.0
    private var phoneNum: String = ""
    private var bankCardNum: String = ""
    private var bankPIN: String = ""
    private var userName: String = ""
    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_gateway)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setTitle("Payment Gateway")

        textBankCardNum = findViewById(R.id.edit_text_bank_card_number)
        textPIN = findViewById(R.id.edit_text_bank_pin)
        textOTP = findViewById(R.id.edit_text_otp)
        buttonOTP = findViewById(R.id.button_otp)
        buttonConfirm = findViewById(R.id.button_confirm)

        plateNum = intent.getStringExtra("plate_num")!!
        vehicleType = intent.getStringExtra("vehicle_type")!!
        vehicleBrand = intent.getStringExtra("vehicle_brand")!!
        vehicleModel = intent.getStringExtra("vehicle_model")!!
        vehicleVariant = intent.getStringExtra("vehicle_variant")!!
        vehicleYear = intent.getStringExtra("vehicle_year")!!
        variantPrice = intent.getDoubleExtra("variant_price", 0.0).toString().toDouble()
        insuranceCompanyName = intent.getStringExtra("company_name")!!
        insuranceCompanyUID = intent.getStringExtra("company_uid")!!
        insuranceName = intent.getStringExtra("insurance_name")!!
        insuranceUID = intent.getStringExtra("insurance_uid")!!
        insuranceClass = intent.getStringExtra("insurance_class")!!
        inviterUID = intent.getStringExtra("inviter_uid")
        yearlyPayment = intent.getDoubleExtra("yearly_payment", 0.0)!!
        itemCoveredName = intent.getStringArrayListExtra("item_covered_name_list")!!
        itemCoveredPrice = intent.getStringArrayListExtra("item_covered_price_list")!!

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        progressBar = ProgressDialog(this)
        progressBar.setMessage("Purchasing...")
        progressBar.setCancelable(false)

        loadBankCardDetails()

        textBankCardNum.text = bankCardNum

//        buttonOTP.setOnClickListener {
//            generateOTP()
//        }
        buttonConfirm.setOnClickListener {
            Log.d("Payment 1", "Here")
            validateInput()
        }
    }

    private fun validateInput() {
        reference = database.getReference("User")

        if (textBankCardNum.text.trim().toString().isNullOrEmpty()){
            textBankCardNum.error = "Please enter your bank card number!"
            textBankCardNum.requestFocus()
            return
        }
        if (textPIN.text.trim().toString().isNullOrEmpty()){
            textPIN.error = "Please enter your PIN!"
            textPIN.requestFocus()
            return
        }

        if (bankCardNum != "" && bankPIN != ""){
            if (textBankCardNum.text.toString() != bankCardNum){
                textBankCardNum.error = "Invalid Bank Card Number!!"
                textBankCardNum.requestFocus()
                return
            }

            if (textPIN.text.toString() != bankPIN){
                textPIN.error = "Invalid Bank PIN!!"
                textPIN.requestFocus()
                return
            }
        }

        if (textBankCardNum.text.length < 16){
            textBankCardNum.error = "Bank Card Number must be 16-digits!"
            textBankCardNum.requestFocus()
            return
        }

        if (textPIN.text.length < 6){
            textPIN.error = "Bank PIN must be 6-digits!"
            textPIN.requestFocus()
            return
        }

        Log.d("Payment 1", "Here")
        if (inviterUID.isNullOrEmpty()){
            saveToVehicle()
//            saveUserBankCard()
            Log.d("Payment 2", "Here")
        }else{
//            saveUserBankCard()
            addReferralAndPoints()
            Log.d("Payment 3", "Here")
        }

//        saveToInsuranceBought()
    }

    private fun addReferralAndPoints() {
        var pointsAwarded: String = ""
        val uid = FirebaseAuth.getInstance().uid

        reference = database.getReference("User")
        var i = 0
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if (data.child("user_uid").value.toString() == uid){
                        pointsAwarded = data.child("point_awarded").value.toString()

                        if(i == 0){
                            reference = database.getReference("User")
                            val updateSpin = mapOf<String, Int>(
                                "point_awarded" to pointsAwarded.toInt() + 500
                            )
                            reference.child(uid!!).updateChildren(updateSpin)

                            reference = database.getReference("Referral")
                            val referralUID = reference.push().key?:""

                            val referral = Referral(referralUID,inviterUID!!, uid, insuranceUID, "New")
                            reference.child(referralUID).setValue(referral).addOnCompleteListener {
                                saveToVehicle()
                            }
                            i++
                        }
                    }
                }


            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }
        })
        Log.d("Test Points awarded", pointsAwarded)



    }

    private fun saveToInsuranceBought(vehicleUID: String) {
        reference = database.getReference("Insurance_Bought")
        val uid = reference.push().key?:""
        val userUID = FirebaseAuth.getInstance().uid?:""
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())

        Log.d("Payment 5", "Here")
        progressBar.show()
        val insuranceBought = Insurance_Bought(uid, vehicleUID, userUID, insuranceName, insuranceCompanyName, yearlyPayment, currentDate, "Active")
        reference.child("$userUID/$insuranceCompanyUID/$uid").setValue(insuranceBought).addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("Saved", "Successfully to Insurance Bought")
                Log.d("Payment 6", "Here")
                var i = 0
                for (data in itemCoveredName){
                    saveToInsuranceBoughtItemCovered(uid, data, itemCoveredPrice[i].toDouble())
                    i++
                }
                if(i != 0){
                    Log.d("Payment 9", "Here")
                }

                saveUserBankCard()
//                saveToVehicle()
            }
        }
    }

    private fun saveToVehicle() {
        val userUID = FirebaseAuth.getInstance().uid?:""
        reference = database.getReference("Vehicle/$userUID")
        val uid = reference.push().key?:""

        Log.d("Payment 3", "Here")
        val vehicle = Vehicle(uid, plateNum, vehicleType, vehicleBrand, vehicleModel, vehicleVariant, vehicleYear, userName, "Available")
        reference.child(uid).setValue(vehicle).addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("Payment 4", "Here")
                saveToInsuranceBought(uid)
//                saveUserBankCard()
            }
        }
    }

    private fun saveUserBankCard() {
        reference = database.getReference("User")
        val userUID = FirebaseAuth.getInstance().uid

        var updateBankCard = mapOf<String,String>(
            "user_bank_card_num" to textBankCardNum.text.toString(),
            "user_bank_pin" to textPIN.text.toString()
        )

        reference.child(userUID!!).updateChildren(updateBankCard).addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("Payment 10", "Here")
                if (progressBar.isShowing) progressBar.dismiss()
                Log.d("Saved", "Successfully to User Bank Card")
                val intent = Intent(this, PaymentSuccessfulActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    private fun saveToInsuranceBoughtItemCovered(insuranceBoughtUID: String, itemName: String, itemPrice: Double) {
        reference = database.getReference("Insurance_Bought_Item_Covered")
        val uid = reference.push().key?:""

        Log.d("Payment 7", "Here")
        val insuranceBought = Insurance_Bought_Item_Covered(uid, itemName, itemPrice, "Available")
        reference.child("$insuranceBoughtUID/$uid").setValue(insuranceBought).addOnCompleteListener {
            if (it.isSuccessful){
                Log.d("Payment 8", "Here")
                Log.d("Saved", "Successfully to Insurance Bought Item Covered")
            }
        }
    }

//    private fun generateOTP() {
//        var randomNumber = ""
//
//        for (i in 1..6) {
//            randomNumber += (0..9).random()
//        }
//
//        Log.d("TEst OTP","$randomNumber")
//        val smsManager = SmsManager.getDefault() as SmsManager
//        smsManager.sendTextMessage(
//            "+60$phoneNum",
//            null,
//            "Your OTP for MyBee Corp's Payment Gateway is $randomNumber. Please don't share this code to others.",
//            null,
//            null
//        )
//    }

    private fun loadBankCardDetails() {
        val userUID = FirebaseAuth.getInstance().uid
        reference = database.getReference("User")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        if (data.child("user_uid").value.toString() == userUID) {
                            if (data.child("user_bank_card_num").value.toString() == "0" &&
                                data.child("user_bank_pin").value.toString() == "0"){
                                bankPIN = "0"
                                bankCardNum = "0"
                                phoneNum = data.child("user_phone_num").value.toString()
                                userName = data.child("user_full_name").value.toString()
                                textBankCardNum.text = ""
                            }else{
                                bankPIN = data.child("user_bank_pin").value.toString()
                                bankCardNum = data.child("user_bank_card_num").value.toString()
                                phoneNum = data.child("user_phone_num").value.toString()
                                userName = data.child("user_full_name").value.toString()
                                textBankCardNum.text = bankCardNum
                            }

                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}