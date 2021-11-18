package com.example.mybeecorp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mybeecorp.R
import com.example.mybeecorp.interfaces.Communicator
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Register1Fragment : Fragment() {

    private var email: String? = null
    private var password: String? = null
    private var role: String? = null
    private var company: String? = null
    private lateinit var communicator: Communicator
    private lateinit var fullName: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButtonMember: RadioButton
    private lateinit var radioButtonInsuranceAdmin: RadioButton
    private lateinit var buttonNext: Button
    private lateinit var buttonBack: Button
    private lateinit var textChooseCompany: TextView
    private lateinit var spinnerCompany: Spinner
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var companyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = arguments?.getString("email")
        password = arguments?.getString("password")
        role = arguments?.getString("role")
        company = arguments?.getString("company")

        fullName = view.findViewById(R.id.edit_text_fullname)
        radioGroup = view.findViewById(R.id.radio_button_group)
        radioButtonMember = view.findViewById(R.id.radio_button_member)
        radioButtonInsuranceAdmin = view.findViewById(R.id.radio_button_insurance_admin)
        textChooseCompany = view.findViewById(R.id.text_choose_company)
        spinnerCompany = view.findViewById(R.id.spinner_company)
        buttonNext = view.findViewById(R.id.button_next)
        buttonBack = view.findViewById(R.id.button_back)
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        fullName.setText(arguments?.getString("fullname"))

        if (role == "Member") {
            radioButtonMember.isChecked = true
            detectRadioButton()
        } else if (role == "Insurance Company Admin") {
            radioButtonInsuranceAdmin.isChecked = true
            detectRadioButton()
        }

        loadCompanySpinner(view)
        communicator = activity as Communicator

        detectRadioButton()

        spinnerCompany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                company = parent!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                company = null
            }
        }

        buttonNext.setOnClickListener {
            nextPage(view)
        }

        buttonBack.setOnClickListener {
            activity?.finish()
        }

    }

    private fun detectRadioButton() {
        radioGroup.setOnCheckedChangeListener { _, i ->
            if (i == R.id.radio_button_insurance_admin) {
                textChooseCompany.visibility = View.VISIBLE
                spinnerCompany.visibility = View.VISIBLE
            } else {
                textChooseCompany.visibility = View.GONE
                spinnerCompany.visibility = View.GONE
            }
        }
    }

    private fun loadCompanySpinner(view: View) {
        reference = database.getReference("Insurance_Company")
        reference.orderByChild("company_name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                companyList.clear()
                for (data in snapshot.children) {
                    companyList.add(data.child("company_name").value.toString())
                }

                val adapter = ArrayAdapter(
                    view.context,
                    android.R.layout.simple_list_item_1, companyList
                )
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                spinnerCompany.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun nextPage(view: View) {
        if (fullName.text.toString().trim().isNullOrEmpty()) {
            fullName.error = "We want to know your name please..."
            fullName.requestFocus()
            return
        }
        if (!radioButtonMember.isChecked && !radioButtonInsuranceAdmin.isChecked) {
            Toast.makeText(view.context, "Please select one of the roles!", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val intSelectButton: Int = radioGroup.checkedRadioButtonId
        val radioButton = view.findViewById<RadioButton>(intSelectButton)
        role = radioButton.text.toString()
        if (role == "Member") company = null

        if (email.isNullOrEmpty() && password.isNullOrEmpty()) {
            communicator.goBackFragment2(fullName.text.toString(), role, "", "", company)
        } else if (email.isNullOrEmpty()) {
            communicator.goBackFragment2(fullName.text.toString(), role, "", password, company)
        } else if (password.isNullOrEmpty()) {
            communicator.goBackFragment2(fullName.text.toString(), role, email, "", company)
        } else {
            communicator.goBackFragment2(fullName.text.toString(), role, email, password, company)
        }
    }

}