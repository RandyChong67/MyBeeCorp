package com.example.mybeecorp.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.User
import com.example.mybeecorp.interfaces.Communicator
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Register3Fragment : Fragment() {

    private var fullName: String? = ""
    private var role: String? = ""
    private lateinit var communicator: Communicator
    private var email: String? = ""
    private var password: String? = ""
    private var company: String? = null
    private lateinit var phoneNum: TextView
    private lateinit var buttonFinish: Button
    private lateinit var buttonBack: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        communicator = activity as Communicator

        fullName = arguments?.getString("fullname")
        email = arguments?.getString("email")
        password = arguments?.getString("password")
        role = arguments?.getString("role")
        company = arguments?.getString("company")

        Log.d("Fragment3", fullName.toString())
        role?.let { Log.d("Fragment3", it) }
        phoneNum = view.findViewById(R.id.edit_text_phone)
        buttonFinish = view.findViewById(R.id.button_finish)
        buttonBack = view.findViewById(R.id.button_back)


        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        buttonFinish.setOnClickListener {
            performRegistration(view)
        }

        buttonBack.setOnClickListener {
            communicator.goBackFragment2(fullName, role, email, password, company)
        }
    }

    private fun performRegistration(view: View) {

        progressBar = ProgressDialog(view.context)
        progressBar.setMessage("Creating account...")
        progressBar.setCancelable(false)
        progressBar.show()

        if (phoneNum.text.toString().trim().isEmpty()) {
            if (progressBar.isShowing) progressBar.dismiss()
            val snackbar = Snackbar.make(
                view, "Your phone number cannot be blank.",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null)
            snackbar.show()
            return
        }

        val isValidPhone = isValidPhoneNum(phoneNum.text.toString())
        if (!isValidPhone) {
            if (progressBar.isShowing) progressBar.dismiss()
            val snackbar = Snackbar.make(
                view, "Invalid phone number format.",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null)
            snackbar.show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                saveUserToFirebaseDatabase(view)
            }.addOnFailureListener {
                if (progressBar.isShowing) progressBar.dismiss()
                val snackbar = Snackbar.make(
                    view, "Failed to register account: ${it.message}",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.show()
            }
    }

    private fun saveUserToFirebaseDatabase(view: View) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = database.getReference("/User/$uid")
        val bankCardNum: String = ""
        val bankPIN: String = ""
        val avatar: String = ""
        val companyName: String = if (company != null) company!! else ""
        val pointAwarded: Int = 0
        val spinAwarded: Int = 0
        val referralID: String = ""
        val userStatus: String = "Activate"

        val user = User(
            uid,
            fullName!!,
            60,
            phoneNum.text.toString(),
            email!!,
            bankCardNum,
            bankPIN,
            avatar,
            role!!,
            userStatus,
            companyName,
            pointAwarded,
            spinAwarded,
            referralID
        )
        ref.setValue(user).addOnSuccessListener {
            if (progressBar.isShowing) progressBar.dismiss()
            val builder = AlertDialog.Builder(view.context)
            builder.setMessage("Your account is registered.")
            builder.setTitle("Congratulations!")
            builder.setCancelable(false)
            builder.setPositiveButton("Go back to Login") { dialog, _ ->
                dialog.dismiss()
                activity?.finish()
            }
            val alert = builder.create()
            alert.show()
        }
    }

    fun isValidPhoneNum(phone: String?): Boolean {
        phone?.let {
            val phonePattern = "^(\\+?6?01)[0-46-9]-*[0-9]{7,8}\$"
            val phoneMatcher = Regex(phonePattern)

            return phoneMatcher.find(phone) != null
        } ?: return false
    }
}