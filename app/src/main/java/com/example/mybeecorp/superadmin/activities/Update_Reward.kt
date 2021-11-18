package com.example.mybeecorp.superadmin.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.reward
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class Update_Reward : AppCompatActivity() {

        var selectedPhoto: Uri? = null
        var changedPhoto: String? = "No"

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_update_reward)
            this.supportActionBar?.title = "Update Reward"
            this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

            val rewardStatusddl = resources.getStringArray(R.array.RewardStatus)
            val rewardStatusspinner = findViewById<Spinner>(R.id.rewardStatus_spinner)

            val oldTextView = findViewById<TextView>(R.id.oldImage)
            oldTextView.setVisibility(View.GONE);
            val oldImg = findViewById<EditText>(R.id.oldRewardImgUrl)
            oldImg.setVisibility(View.GONE);

            //Reward Status
            if (rewardStatusspinner != null){
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, rewardStatusddl
                )
                rewardStatusspinner.adapter = adapter
            }

            val rewardID = intent.getStringExtra("rewardID").toString()
            //val rewardID = FirebaseAuth.getInstance().uid
            //val rewardID = "-MjF1aq6IcN74PNo2quq"

            val editRewardPic = findViewById<ImageView>(R.id.reward_editImg)
            val updateRewardbtn = findViewById<Button>(R.id.updateReward_btn)
            val backRewardbtn = findViewById<Button>(R.id.backUpdateReward_btn)

            loadReward(rewardID!!)

            editRewardPic.setOnClickListener{
                val intentPickPhoto = Intent(Intent.ACTION_PICK)
                intentPickPhoto.type = "image/*"
                startActivityForResult(intentPickPhoto, 0)
            }

            updateRewardbtn.setOnClickListener{
                if(changedPhoto == "Yes") {
                    uploadRewardImg(rewardID)
                }else{
                    val oldRewardimg = findViewById<TextView>(R.id.oldRewardImgUrl)
                    updateReward(rewardID, oldRewardimg.text.toString())
                }
            }

            backRewardbtn.setOnClickListener{
                onBackPressed()
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            val circleView = findViewById<CircleImageView>(R.id.selectedRewardNewImg)

            if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
                selectedPhoto = data.data

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhoto)
                circleView.setImageBitmap(bitmap)
                changedPhoto = "Yes"
            }
        }

        private fun uploadRewardImg(rewardUID:String){
            if(selectedPhoto == null) return
            val filename = UUID.randomUUID().toString()
            val rewardImgRef = FirebaseStorage.getInstance().getReference("/images/$filename")

            rewardImgRef.putFile(selectedPhoto!!)
                .addOnSuccessListener {
                    rewardImgRef.downloadUrl.addOnSuccessListener {
                        updateReward(rewardUID, it.toString())
                    }
                }
        }


        private fun loadReward(rewardID:String){
            val rewardNameEt : EditText = findViewById(R.id.rewardName_editText)
            val rewardPointEt : EditText = findViewById(R.id.rewardPoint_editText)
            val rewardStatusddl = resources.getStringArray(R.array.RewardStatus)
            val rewardStatusspinner = findViewById<Spinner>(R.id.rewardStatus_spinner)
            val rewardNewImg = findViewById<CircleImageView>(R.id.selectedRewardNewImg)
            val oldRewardImg = findViewById<TextView>(R.id.oldRewardImgUrl)

            //Reward Status
            if (rewardStatusspinner != null){
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, rewardStatusddl
                )
                rewardStatusspinner.adapter = adapter
            }

            var rewardRef = Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Reward")

            rewardRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(i in snapshot.children){
                            if(i.child("reward_uid").value.toString() == rewardID){
                                if(!this@Update_Reward.isFinishing) {
                                    Glide.with(rewardNewImg.context)
                                        .load(i.child("reward_image").value).into(rewardNewImg)
                                }
                                rewardNameEt.setText(i.child("reward_Name").value.toString())
                                rewardPointEt.setText(i.child("reward_Point").value.toString())
                                if(i.child("reward_Status").value != "Available"){
                                    rewardStatusspinner.setSelection(1)
                                }
                                oldRewardImg.text = i.child("reward_image").value.toString()
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

        private fun updateReward(rewardID:String, photoUrl:String) {
            val rewardNameEt: EditText = findViewById(R.id.rewardName_editText)
            val rewardPointEt: EditText = findViewById(R.id.rewardPoint_editText)
            //val rewardStatusddl = resources.getStringArray(R.array.RewardStatus)
            val rewardStatusspinner = findViewById<Spinner>(R.id.rewardStatus_spinner)



            val rewardRef =
                Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Reward")

            if (rewardNameEt.text.toString().trim().isNotEmpty() && rewardNameEt.text.toString().trim().length < 50) {
                if (rewardPointEt.text.toString().trim().isNotEmpty() && rewardPointEt.text.toString().length < 4) {2

                    val updateReward = reward(rewardID, photoUrl, rewardNameEt.text.toString(), rewardPointEt.text.toString().toInt(),rewardStatusspinner.selectedItem.toString())

                    rewardRef.child(rewardID).setValue(updateReward)
                        .addOnCompleteListener {
                            Toast.makeText(
                                applicationContext,
                                "Reward updated successfully",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                }else if(rewardPointEt.text.toString().trim().isEmpty()){
                    Toast.makeText(
                        applicationContext,
                        "Please enter reward point",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }else if(rewardPointEt.text.toString().length > 4){
                    Toast.makeText(
                        applicationContext,
                        "Reward Point cannot more than 4 digit",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }else if(rewardNameEt.text.toString().trim().length > 50){
                Toast.makeText(
                    applicationContext,
                    "Please enter reward name",
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