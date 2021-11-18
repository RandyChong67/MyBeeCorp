package com.example.mybeecorp.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.example.mybeecorp.insurancecompanyadmin.activities.InsuranceCompanyAdminMainActivity
import com.example.mybeecorp.member.activities.MemberMainActivity
import com.example.mybeecorp.superadmin.activities.SuperAdminMainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


private lateinit var email: EditText
private lateinit var password: EditText
private lateinit var buttonLogin: Button
private lateinit var buttonSignUp: Button
private lateinit var buttonForgotPassword: Button
private lateinit var database: FirebaseDatabase
private lateinit var reference: DatabaseReference
private lateinit var progressBar: ProgressDialog

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        email = findViewById(R.id.edit_text_email)
        password = findViewById(R.id.edit_text_password)
        buttonLogin = findViewById(R.id.button_login)
        buttonSignUp = findViewById(R.id.button_sign_up)
        buttonForgotPassword = findViewById(R.id.button_forgot_password)

        buttonLogin.setOnClickListener {
            if (email.text.toString().trim().isNullOrEmpty()){
                email.error = "Email cannot be blank"
                email.requestFocus()
                return@setOnClickListener
            }
            if (password.text.toString().trim().isNullOrEmpty()){
                password.error = "Password cannot be blank"
                password.requestFocus()
                return@setOnClickListener
            }
            performLogin()
        }

        buttonSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {

        progressBar = ProgressDialog(this)
        progressBar.setMessage("Logging in...")
        progressBar.setCancelable(false)
        progressBar.show()

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener {
                if(it.isSuccessful){
                    var counter: Int = 1
                    reference = database.getReference("User")
                    reference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()){
                                for (i in snapshot.children){
                                    if(i.child("user_email").value.toString() == email.text.toString()){
                                        if (i.child("user_status").value.toString() == "Activate"){
                                            if (counter == 1){
                                                if (progressBar.isShowing) progressBar.dismiss()
                                                if(i.child("user_role").value.toString() == "Member"){
                                                    counter++
                                                    val intent = Intent(this@LoginActivity, MemberMainActivity::class.java)
                                                    intent.putExtra("user_full_name", i.child("user_full_name").value.toString())
                                                    startActivity(intent)
                                                    finish()
                                                }else if(i.child("user_role").value.toString() == "Insurance Company Admin"){
                                                    counter++
                                                    val intent = Intent(this@LoginActivity, InsuranceCompanyAdminMainActivity::class.java)
                                                    intent.putExtra("user_full_name", i.child("user_full_name").value.toString())
                                                    startActivity(intent)
                                                    finish()
                                                }else{
                                                    counter++
                                                    val intent = Intent(this@LoginActivity, SuperAdminMainActivity::class.java)
                                                    intent.putExtra("user_full_name", i.child("user_full_name").value.toString())
                                                    startActivity(intent)
                                                    finish()
                                                }
                                            }
                                        }else{
                                            val builder = AlertDialog.Builder(this@LoginActivity)
                                            builder.setTitle("Warning")
                                            builder.setMessage("Sorry, your account has been DEACTIVATED.")
                                            builder.setCancelable(true)
                                            builder.setPositiveButton("OK") { dialog, _ ->
                                                dialog.dismiss()
                                                progressBar.dismiss()
                                            }
                                            val alert = builder.create()
                                            alert.show()
                                        }
                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            if (progressBar.isShowing) progressBar.dismiss()
                            Log.e("cancel", error.toString())
                        }

                    })
                }
            }.addOnFailureListener{
                if (progressBar.isShowing) progressBar.dismiss()
                val builder = AlertDialog.Builder(this)
                builder.setMessage("${it.message}")
                builder.setTitle("Error")
                builder.setCancelable(false)
                builder.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                val alert = builder.create()
                alert.show()
            }
    }
}