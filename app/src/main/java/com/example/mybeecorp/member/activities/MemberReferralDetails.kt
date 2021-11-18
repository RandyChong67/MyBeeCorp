package com.example.mybeecorp.member.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybeecorp.R
import com.example.mybeecorp.member.adapters.ReferralMemberListAdapter
import com.example.yangloosuperadmin.classes.ReferralMemberList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MemberReferralDetails : AppCompatActivity() {

    private lateinit var referralListRef : DatabaseReference
    private lateinit var referralListRecyclerView: RecyclerView
    private lateinit var referralListArrayList : ArrayList<ReferralMemberList>
    var memberIDList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_referral_details)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.title = "My Referral List"

        referralListRecyclerView = findViewById(R.id.referralDetails_recycle)
        referralListRecyclerView.layoutManager = LinearLayoutManager(this)
        referralListRecyclerView.setHasFixedSize(true)

        referralListArrayList = arrayListOf<ReferralMemberList>()

        val userID = FirebaseAuth.getInstance().uid
        //val userID = "usr10007"

        val insID = intent.getStringExtra("insUID").toString()
        val insTitle = intent.getStringExtra("insName").toString()
        val sendReferral = findViewById<ImageView>(R.id.sendReferralMessage)
        val referralMsg = "MyBee Corp App: " +
                "Please use Referral ID (" + userID + ") while purchasing Insurance (" + insTitle + "). Thank you."

        loadInsuranceDetails(insID)
        getMemberList(insID)
        sendReferral.setOnClickListener{
            val referralIntent = Intent(Intent.ACTION_SEND)
            referralIntent.putExtra(Intent.EXTRA_TEXT, referralMsg)
            referralIntent.type = "text/plain"
            startActivity(referralIntent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadInsuranceDetails(insID:String){
        val insTitle = findViewById<TextView>(R.id.referralDetails_insTitle)
        val insCompany = findViewById<TextView>(R.id.referralDetails_insCompany)

        /*var insuranceRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Motor_Insurance")*/
        var insuranceRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Motor_Insurance")

        insuranceRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        if(i.child("insurance_uid").value.toString() == insID){
                            insTitle.text = i.child("insurance_name").value.toString()
                            insCompany.text = i.child("insurance_company").value.toString()
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

    private fun getMemberList(insID:String){
        /*val referralRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Referral")*/
        var referralRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Referral")

        val userID = FirebaseAuth.getInstance().uid
        //val userID = "usr10007"

        referralRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                memberIDList.clear()
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        if(i.child("inviter_uid").value.toString() == userID && i.child("insurance_recommended").value.toString() == insID){
                            memberIDList.add(i.child("invited_uid").value.toString())
                        }
                    }
                    if(memberIDList.isEmpty()){
                        Toast.makeText(applicationContext, "There is no referral record yet.", Toast.LENGTH_LONG)
                            .show()
                        return
                    }else{
                        loadReferralList(memberIDList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun loadReferralList(memberIDList:ArrayList<String>){
        /*referralListRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")*/
        referralListRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        val referralList = HashSet<String>(memberIDList)

        referralListRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        for(j in referralList){
                            if(i.child("user_uid").value.toString() == j && i.child("user_status").value.toString() == "Activate"){
                                var member = i.getValue(ReferralMemberList::class.java)
                                referralListArrayList.add(member!!)
                            }
                        }
                    }
//                    referralListRecyclerView.adapter = ReferralMemberListAdapter(referralListArrayList)
                }
                referralListRecyclerView.adapter = ReferralMemberListAdapter(referralListArrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}