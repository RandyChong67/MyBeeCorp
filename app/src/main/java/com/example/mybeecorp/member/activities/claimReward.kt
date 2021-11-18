package com.example.mybeecorp.member.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.reward_history
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class claimReward : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim_reward)
        this.supportActionBar?.title = "Claim Reward"
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var rewardRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Reward")
        val userID = FirebaseAuth.getInstance().uid

        var userRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User/$userID")

        val rewardID = intent.getStringExtra("rewardID").toString()

        val claimRewardbtn = findViewById<Button>(R.id.claim_btn)
        val backClaimbtn = findViewById<Button>(R.id.backClaim_btn)

        loadClaimReward(rewardID)
        loadUser(userID!!)

        claimRewardbtn.setOnClickListener(){

            val pointAwarded = findViewById<TextView>(R.id.pointAwarded_claim)
            val pointAwarded_user = pointAwarded.text.toString().toInt()

            val claimPointEt : EditText = findViewById(R.id.claimPoint_editText)
            val rewardPoint = claimPointEt.text.toString().toInt()

            if(pointAwarded_user >= rewardPoint) {
                val newPoint = pointAwarded_user - rewardPoint
                userRef.child("point_awarded").setValue(newPoint).toString()

                val claimNameEt : EditText = findViewById(R.id.claimName_editText)
                val claimName = claimNameEt.text.toString().trim()
                val claimPoint = claimPointEt.text.toString()
                val claimRewardImg = findViewById<CircleImageView>(R.id.selectedRewardImg)
                val database = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
                val rewardEarnedreferences = database.getReference("Reward_Earned")
                val rewardEarneduid = rewardEarnedreferences.push().key

                val rewardEarned = reward_history(rewardEarneduid, userID, rewardID, claimName,claimPoint.toInt())

                if (rewardEarneduid != null) {
                    rewardEarnedreferences.child("$userID/$rewardEarneduid").setValue(rewardEarned)
                        .addOnCompleteListener {
                            Toast.makeText(
                                applicationContext,
                                "Claim Reward Successfully",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            finish()
                        }
                }
            }else {
                    Toast.makeText(applicationContext, "Sorry your point is not enough", Toast.LENGTH_LONG).show()
            }
        }

        backClaimbtn.setOnClickListener(){
            onBackPressed()
        }
    }



    private fun loadClaimReward(rewardID:String){
        val claimNameEt : EditText = findViewById(R.id.claimName_editText)
        val claimPointEt : EditText = findViewById(R.id.claimPoint_editText)
        val claimRewardImg = findViewById<CircleImageView>(R.id.selectedRewardImg)

        claimNameEt.setEnabled(false)
        claimPointEt.setEnabled(false)

        var rewardRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Reward")

        rewardRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        if(i.child("reward_uid").value.toString() == rewardID){
                            if(!this@claimReward.isFinishing) {
                                Glide.with(claimRewardImg.context)
                                    .load(i.child("reward_image").value).into(claimRewardImg)
                            }
                            claimNameEt.setText(i.child("reward_Name").value.toString())
                            claimPointEt.setText(i.child("reward_Point").value.toString())
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

    private fun loadUser(userID:String){

        val pointAwarded = findViewById<TextView>(R.id.pointAwarded_claim)

        var userRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        if (i.child("user_uid").value.toString() == userID) {
                            pointAwarded.setText(i.child("point_awarded").value.toString())
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}