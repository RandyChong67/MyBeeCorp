package com.example.mybeecorp.superadmin.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.reward
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class Create_Reward : AppCompatActivity() {

    var selectedRewardPhoto: Uri? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_reward)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Create Reward")

        val rewardStatusddl = resources.getStringArray(R.array.RewardStatus)
        val rewardStatusspinner = findViewById<Spinner>(R.id.rewardStatus_spinner)

        //Reward Status
        if (rewardStatusspinner != null){
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, rewardStatusddl
            )
            rewardStatusspinner.adapter = adapter
        }

        val createRewardbtn : Button = findViewById(R.id.updateReward_btn)
        val backButton: Button = findViewById(R.id.backUpdateReward_btn)

        var rewardImg : ImageView = findViewById(R.id.rewardImage)

        rewardImg.setOnClickListener{
            val intentPickPhoto = Intent(Intent.ACTION_PICK)
            intentPickPhoto.type = "image/*"
            startActivityForResult(intentPickPhoto, 0)
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        createRewardbtn.setOnClickListener {
            progressBar = ProgressDialog(this)
            progressBar.setMessage("Loading...")
            progressBar.setCancelable(false)
            progressBar.show()
            insertRewardImage()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val circleView = findViewById<CircleImageView>(R.id.selectedRewardImg)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedRewardPhoto = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedRewardPhoto)
            circleView.setImageBitmap(bitmap)
        }
    }
    private fun insertRewardImage(){
        if(selectedRewardPhoto == null) {
            if (progressBar.isShowing) progressBar.dismiss()
            Toast.makeText(applicationContext, "Please Select Image", Toast.LENGTH_LONG)
                .show()
            return
        }
        val filename = UUID.randomUUID().toString()
        val photoRef = FirebaseStorage.getInstance().getReference("/images/$filename")

        photoRef.putFile(selectedRewardPhoto!!)
            .addOnSuccessListener {
            photoRef.downloadUrl.addOnSuccessListener {
                insertReward(it.toString())
            }
        }
    }

    private fun insertReward(rewardImage: String) {

        val rewardNameEt : EditText = findViewById(R.id.rewardName_editText)
        val rewardPointEt : EditText = findViewById(R.id.rewardPoint_editText)
        val rewardStatusspinner = findViewById<Spinner>(R.id.rewardStatus_spinner)

        val rewardName = rewardNameEt.text.toString().trim()
        val rewardPoint = rewardPointEt.text.toString()
        var rewardStatus = rewardStatusspinner.getSelectedItem().toString()

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val rewardRef = database.getReference("Reward")
        val rewarduid = rewardRef.push().key

        if (rewardName.isNotEmpty() && rewardName.length < 50){
            if(rewardPoint.isNotEmpty() && rewardPoint.length < 4){

                val rewardDetails = reward(rewarduid, rewardImage, rewardName, rewardPoint.toInt(), rewardStatus.toString())

                if (rewarduid != null) {
                    rewardRef.child(rewarduid).setValue(rewardDetails).addOnCompleteListener {
                        if (progressBar.isShowing) progressBar.dismiss()
                        Toast.makeText(applicationContext, "Reward insert Successfully", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }else if(rewardPoint.isEmpty()){
                if (progressBar.isShowing) progressBar.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Please key in Reward Point",
                    Toast.LENGTH_LONG
                )
                    .show()
            }else if(rewardPoint.length > 4){
                if (progressBar.isShowing) progressBar.dismiss()
                Toast.makeText(
                    applicationContext,
                    "Reward Point cannot more than 4 digit",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }else if(rewardName.isEmpty()){
            if (progressBar.isShowing) progressBar.dismiss()
            Toast.makeText(
                applicationContext,
                "Please key in Reward Name",
                Toast.LENGTH_LONG
            )
                .show()
        }else if(rewardName.length > 50){
            if (progressBar.isShowing) progressBar.dismiss()
            Toast.makeText(
                applicationContext,
                "Sorry, Reward Name is too long",
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
