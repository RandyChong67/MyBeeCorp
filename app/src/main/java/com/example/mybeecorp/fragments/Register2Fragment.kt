package com.example.mybeecorp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.example.mybeecorp.interfaces.Communicator
import com.google.android.material.snackbar.Snackbar


class Register2Fragment : Fragment() {

    private var fullName: String? = null
    private var role: String? = null
    private var company: String? = null
    private lateinit var communicator: Communicator
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var reEnterPassword: EditText
    private lateinit var buttonNext: Button
    private lateinit var buttonBack: Button
    private lateinit var buttonInfo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullName = arguments?.getString("fullname")
        role = arguments?.getString("role")
        company = arguments?.getString("company")

        email = view.findViewById(R.id.edit_text_email)
        password = view.findViewById(R.id.edit_text_password)
        reEnterPassword = view.findViewById(R.id.edit_text_re_enter_password)
        buttonNext = view.findViewById(R.id.button_next)
        buttonBack = view.findViewById(R.id.button_back)
        buttonInfo = view.findViewById(R.id.button_info)

        email.setText(arguments?.getString("email").toString())
        password.setText(arguments?.getString("password").toString())
        reEnterPassword.setText(arguments?.getString("password").toString())

        communicator = activity as Communicator

        buttonInfo.setOnClickListener {
            val builder = AlertDialog.Builder(view.context)
            builder.setMessage("Password must contains at least 8 alpha-numeric characters")
            builder.setTitle("Information")
            builder.setCancelable(false)
            builder.setPositiveButton("Noted") { dialog, _ ->
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()
        }

        buttonNext.setOnClickListener {
            if (email.text.toString().trim().isEmpty() ||
                password.text.toString().trim().isEmpty() ||
                reEnterPassword.text.toString().trim().isEmpty()
            ) {
                val snackbar = Snackbar.make(
                    view, "Input fields cannot be blank!",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
                email.error = "Please enter valid email address"
                return@setOnClickListener
            }
            val passStatus = isValidPassword(password.text.toString())
            if (!passStatus || password.text.toString().length < 8) {
                password.error = "Password must contains at least 8 alpha-numeric characters!"
                return@setOnClickListener
            }
            if (reEnterPassword.text.toString() != password.text.toString()) {
                reEnterPassword.error = "Re-entered password must be same with above!"
                return@setOnClickListener
            }
            communicator.passDataCom2(
                fullName,
                role,
                email.text.toString(),
                password.text.toString(),
                company
            )
        }

        buttonBack.setOnClickListener {
            communicator.goBackFragment1(
                fullName,
                role,
                email.text.toString(),
                password.text.toString(),
                company
            )
        }
    }

    fun isValidPassword(password: String?): Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }
}