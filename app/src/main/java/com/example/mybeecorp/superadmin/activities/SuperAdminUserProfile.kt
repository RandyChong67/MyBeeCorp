package com.example.mybeecorp.superadmin.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class SuperAdminUserProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_super_admin_user_profile)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.title = "User Details"

        val userID = intent.getStringExtra("userID").toString()
        val saveBtn = findViewById<Button>(R.id.superAdminUserProfile_Save)

        loadProfile(userID)

        saveBtn.setOnClickListener{
            updateProfile(userID)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadProfile(userID:String){
        var userNameET = findViewById<EditText>(R.id.superAdminUserProfile_userName)
        var phoneNumET = findViewById<EditText>(R.id.superAdminUserProfile_PhoneNum)
        var emailET = findViewById<EditText>(R.id.superAdminUserProfile_email)
        var userProfileCIV = findViewById<CircleImageView>(R.id.superAdminUserProfile_outputPicture)
        var pointsET = findViewById<EditText>(R.id.superAdminUserProfile_pointsAwarded)
        var spinsET = findViewById<EditText>(R.id.superAdminUserProfile_spins)
        var userRoleET = findViewById<EditText>(R.id.superAdminUserProfile_userRole)
        var userStatusSpinner = findViewById<Spinner>(R.id.superAdminUserProfile_userStatus)

        val userStatus = resources.getStringArray(R.array.userStatus)

        if(userStatusSpinner != null){
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, userStatus
            )
            userStatusSpinner.adapter = adapter
        }

        /*var profileRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")*/
        var profileRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        profileRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(i in snapshot.children){
                        if(i.child("user_uid").value.toString() == userID){
                            if(!this@SuperAdminUserProfile.isFinishing) {
                                Glide.with(userProfileCIV.context)
                                    .load(i.child("user_avatar").value)
                                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                                    .circleCrop().error(
                                    R.drawable.ic_baseline_account_circle_24
                                ).into(userProfileCIV)
                            }
                            userNameET.setText(i.child("user_full_name").value.toString())
                            phoneNumET.setText(i.child("user_phone_num").value.toString())
                            emailET.setText(i.child("user_email").value.toString())
                            pointsET.setText(i.child("point_awarded").value.toString())
                            spinsET.setText(i.child("spin_awarded").value.toString())
                            userRoleET.setText(i.child("user_role").value.toString())
                            if(i.child("user_status").value != "Activate"){
                                userStatusSpinner.setSelection(1)
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

    private fun updateProfile(userID:String){
        var userStatusSpinner = findViewById<Spinner>(R.id.superAdminUserProfile_userStatus)

        //var profileRef = Firebase.database("https://yangloomadtestdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User")
        var profileRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("User")

        profileRef.child(userID).child("user_status").setValue(userStatusSpinner.selectedItem.toString()).addOnCompleteListener {
            Toast.makeText(applicationContext, "User updated successfully", Toast.LENGTH_LONG)
                .show()
        }
    }
}