package com.example.mybeecorp.superadmin.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Spin_Wheel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class EditPrizesActivity : AppCompatActivity() {

    private lateinit var editTextPrize: EditText
    private lateinit var buttonUpdate: Button
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressDialog
    private var uid: String = ""
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_prizes)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setTitle("Edit Spin Wheel Prizes")
        val intent = intent
        uid = intent.getStringExtra("prize_uid").toString()
        name = intent.getStringExtra("prize_name").toString()

        editTextPrize = findViewById(R.id.edit_text_prizes)
        buttonUpdate = findViewById(R.id.button_update)

        editTextPrize.setText(name)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        buttonUpdate.setOnClickListener {
            updatePrize()
        }

    }

    private fun updatePrize() {
        progressBar = ProgressDialog(this)
        progressBar.setMessage("Updating...")
        progressBar.setCancelable(false)
        progressBar.show()

        if (editTextPrize.text.toString().trim().isNullOrEmpty()){
            editTextPrize.error = "This field cannot be blank!!"
            editTextPrize.requestFocus()
            if (progressBar.isShowing) progressBar.dismiss()
            return
        }

        reference = database.getReference("Spin_Wheel")
        val updateSpinWheel = Spin_Wheel(uid, editTextPrize.text.toString())
        reference.child(uid).setValue(updateSpinWheel).addOnCompleteListener {
            if(it.isSuccessful){
                if (progressBar.isShowing) progressBar.dismiss()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Spin Wheel Prize Updated!")
                builder.setTitle("Updated Successfully!")
                builder.setCancelable(false)
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                val alert = builder.create()
                alert.show()
            }else{
                if (progressBar.isShowing) progressBar.dismiss()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Failed")
                builder.setTitle("Failed to update...")
                builder.setCancelable(false)
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}