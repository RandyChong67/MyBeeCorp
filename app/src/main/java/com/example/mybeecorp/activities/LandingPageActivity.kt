package com.example.mybeecorp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Referral
import com.example.mybeecorp.insurancecompanyadmin.activities.InsuranceCompanyAdminMainActivity
import com.example.mybeecorp.member.activities.MemberMainActivity
import com.example.mybeecorp.superadmin.activities.SuperAdminMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.sentry.Sentry
import my.com.customer.classes.Voucher
import java.text.SimpleDateFormat
import java.util.*

class LandingPageActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var referenceReferral: DatabaseReference
    private lateinit var progressBar: ProgressBar
    private var voucherList = mutableListOf<Voucher>()
    private var referralList = mutableListOf<Referral>()
//    private lateinit var userRole: String
//    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Sentry.captureMessage("testing SDK setup")
        setContentView(R.layout.activity_landing_page)
        progressBar = findViewById(R.id.progressBar)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val transaction = Sentry.startTransaction("test-transaction-name", "test-transaction-operation")
        val span = transaction.startChild("test-child-operation")

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

//        userRole = ""
//        userName = ""

        verifyVoucherExpiredDate()

//        verifyReferralStatus()
        verifyUser()
        span.finish() // Mark the span as finished
        transaction.finish() // Mark the tzransaction as finished and send it to Sentry
    }

    private fun verifyReferralStatus(uid: String, spin: Int) {
        var referralCount = 0
        var count = 0
        reference = database.getReference("Referral")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (count == 0){
                    referralList.clear()
                    if (snapshot.exists()){
                        for (data in snapshot.children){
                            if (data.child("referral_status").value.toString() == "New" &&
                                data.child("inviter_uid").value.toString() == uid){
                                var ref = data.getValue(Referral::class.java)
                                referralList.add(ref!!)
                                referralCount++
                                count++
                                Log.d("Referral Count", referralCount.toString())
                                if (referralCount == 5){
                                    validateReferral(spin)
                                }
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

    private fun validateReferral(spin: Int) {
        val uid = FirebaseAuth.getInstance().uid
        for (i in 0 until referralList.size){
            val updateReferralStatus = mapOf<String, String>(
                "referral_status" to "Old"
            )
            reference = database.getReference("Referral")
            reference.child(referralList[i].referral_uid).updateChildren(updateReferralStatus)
        }

        val updateSpinAwarded = mapOf<String, Int>(
            "spin_awarded" to spin +1
        )
        reference = database.getReference("User")
        reference.child(uid!!).updateChildren(updateSpinAwarded)
    }

    private fun verifyVoucherExpiredDate() {
        reference = database.getReference("Voucher")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                voucherList.clear()
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        var voucher = data.getValue(Voucher::class.java)
                        voucherList.add(voucher!!)
                    }
                }

                compareDate()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun compareDate() {

        for (voucher in voucherList){
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val voucherDate: Date = sdf.parse(voucher.voucher_expiry_date)
            if (Date().after(voucherDate)){
                reference = database.getReference("Voucher")
                val updateVoucherStatus = mapOf<String,String>(
                    "voucher_status" to "Expired"
                )
                reference.child(voucher.voucher_uid).updateChildren(updateVoucherStatus).addOnCompleteListener{
                    Log.d("Test Voucher1","Updated Voucher")
                    Log.d("Test Voucher2",voucher.voucher_status)
                }
            }
        }
    }

    private fun verifyUser() {
        progressBar.isVisible = true

        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }else{

            var counter: Int = 1
            reference = database.getReference("User")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (i in snapshot.children){
                            if(i.child("user_uid").value.toString() == uid){
                                if (i.child("user_status").value.toString() == "Activate"){
                                    verifyReferralStatus(uid, i.child("spin_awarded").value.toString().toInt())
                                    if (counter == 1){
                                        if (i.child("user_role").value.toString() == "Member"){
                                            counter++
                                            val intent = Intent(this@LandingPageActivity, MemberMainActivity::class.java)
                                            intent.putExtra("user_full_name", i.child("user_full_name").value.toString())
                                            progressBar.isVisible = false
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
                                        }else if(i.child("user_role").value.toString() == "Insurance Company Admin"){
                                            counter++
                                            val intent = Intent(this@LandingPageActivity, InsuranceCompanyAdminMainActivity::class.java)
                                            intent.putExtra("user_full_name", i.child("user_full_name").value.toString())
                                            progressBar.isVisible = false
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
                                        }else{
                                            counter++
                                            val intent = Intent(this@LandingPageActivity, SuperAdminMainActivity::class.java)
                                            intent.putExtra("user_full_name", i.child("user_full_name").value.toString())
                                            progressBar.isVisible = false
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
                                        }
                                    }
                                }else{
                                    val intent = Intent(this@LandingPageActivity, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("cancel", error.toString())
                }
            })

        }
    }
}