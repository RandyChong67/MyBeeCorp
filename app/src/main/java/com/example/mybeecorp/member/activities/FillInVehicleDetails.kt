package com.example.mybeecorp.member.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.mybeecorp.classes.Variant
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import com.example.mybeecorp.R

class FillInVehicleDetails : AppCompatActivity() {

    private lateinit var spinnerVehicleType: Spinner
    private lateinit var spinnerVehicleBrand: Spinner
    private lateinit var spinnerVehicleModel: Spinner
    private lateinit var spinnerVehicleVariant: Spinner
    private lateinit var spinnerVehicleYear: Spinner
    private lateinit var editTextVehiclePlateNum: EditText
    private lateinit var buttonProceed: Button
    private lateinit var buttonBack: Button
    private var variantPrice: Double = 0.0
    private var vehicleModelList = mutableListOf<String>()
    private var vehicleVariantList = mutableListOf<Variant>()
    private var variantNameList = mutableListOf<String>()
    private var variantPriceList = mutableListOf<Double>()
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var itemCovered1: String = ""
    private var itemCovered2: String = ""
    private var itemCovered3: String = ""
    private var itemCovered4: String = ""
    private var itemCovered5: String = ""
    private var itemCovered6: String = ""
    private var itemCovered7: String = ""
    private var itemCovered8: String = ""
    private var insuranceName: String = ""
    private var insuranceUID: String = ""
    private var insuranceClass: String = ""
    private var insuranceCompanyName: String = ""
    private var insuranceCompanyUID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_in_vehicle_details)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Vehicle Details")

        spinnerVehicleType = findViewById(R.id.vehicleType_spinner)
        spinnerVehicleBrand = findViewById(R.id.vehicleBrand_spinner)
        spinnerVehicleModel = findViewById(R.id.vehicleModel_spinner)
        spinnerVehicleVariant = findViewById(R.id.vehicleVariant_spinner)
        editTextVehiclePlateNum = findViewById(R.id.vehiclePlateNumber_InputText)
        spinnerVehicleYear = findViewById(R.id.vehicleYear_spinner)
        buttonProceed = findViewById(R.id.ConfirmAndProceed_button)
        buttonBack = findViewById(R.id.back_button)

        insuranceCompanyName = intent.getStringExtra("company_name")!!
        insuranceCompanyUID = intent.getStringExtra("company_uid")!!
        insuranceName = intent.getStringExtra("insurance_name")!!
        insuranceUID = intent.getStringExtra("insurance_uid")!!
        insuranceClass = intent.getStringExtra("insurance_class")!!
        itemCovered1 = intent.getStringExtra("item_covered_1")!!
        itemCovered2 = intent.getStringExtra("item_covered_2")!!
        itemCovered3 = intent.getStringExtra("item_covered_3")!!
        itemCovered4 = intent.getStringExtra("item_covered_4")!!
        itemCovered5 = intent.getStringExtra("item_covered_5")!!
        itemCovered6 = intent.getStringExtra("item_covered_6")!!
        itemCovered7 = intent.getStringExtra("item_covered_7")!!
        itemCovered8 = intent.getStringExtra("item_covered_8")!!

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        loadSpinner()

        spinnerVehicleBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                reference = database.getReference("Model")
                reference.orderByChild("model_name")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            vehicleModelList.clear()
                            if (snapshot.exists()) {
                                for (data in snapshot.children) {
                                    if (data.child("model_status").value.toString() == "Available"){
                                        if (data.child("brand_name").value.toString() == parent!!.getItemAtPosition(
                                                position
                                            ).toString()
                                        )
                                            vehicleModelList.add(data.child("model_name").value.toString())
                                    }

                                }
                            }

                            val adapter = ArrayAdapter(
                                this@FillInVehicleDetails,
                                android.R.layout.simple_list_item_1, vehicleModelList
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                            spinnerVehicleModel.adapter = adapter
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vehicleModelList.clear()
            }

        }

        spinnerVehicleModel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                reference = database.getReference("Variant")
                reference.orderByChild("variant_name")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            vehicleVariantList.clear()
                            variantNameList.clear()
                            variantPriceList.clear()
                            for (data in snapshot.children) {
                                if (data.child("variant_status").value.toString() == "Available"){
                                    if (data.child("model_name").value.toString() == parent!!.getItemAtPosition(
                                            position
                                        ).toString()
                                    ){
                                        variantNameList.add(data.child("variant_name").value.toString())
                                        variantPriceList.add(data.child("variant_price").value.toString().toDouble())
                                    }
                                }
                            }
                            val adapter = ArrayAdapter(
                                this@FillInVehicleDetails,
                                android.R.layout.simple_list_item_1, variantNameList
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                            spinnerVehicleVariant.adapter = adapter
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("cancel", error.toString())
                        }

                    })
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                vehicleVariantList.clear()
            }

        }

        spinnerVehicleVariant.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                variantPrice = variantPriceList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        buttonProceed.setOnClickListener {
            if (spinnerVehicleModel.selectedItemPosition == -1){
                Toast.makeText(this, "Model cannot be blank", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (spinnerVehicleVariant.selectedItemPosition == -1){
                Toast.makeText(this, "Variant cannot be blank", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (spinnerVehicleType.selectedItemPosition == -1){
                Toast.makeText(this, "Type cannot be blank", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (spinnerVehicleBrand.selectedItemPosition == -1){
                Toast.makeText(this, "Brand cannot be blank", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (spinnerVehicleYear.selectedItemPosition == -1){
                Toast.makeText(this, "Year cannot be blank", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            proceedToPayment(editTextVehiclePlateNum.text.trim().toString())
        }
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun proceedToPayment(plateNum: String?) {
        if (plateNum.isNullOrEmpty()) {
            editTextVehiclePlateNum.error = "This field cannot be blank!"
            editTextVehiclePlateNum.requestFocus()
            return
        }
        val plateNumStatus = isValidPlateNum(plateNum)
        if (!plateNumStatus) {
            editTextVehiclePlateNum.error = "Invalid Plate Number!"
            editTextVehiclePlateNum.requestFocus()
            return
        }

        val intent = Intent(this, ValidateInsuranceDetailActivity::class.java)
        intent.putExtra("plate_num", plateNum.uppercase(Locale.getDefault()))
        intent.putExtra("vehicle_type", spinnerVehicleType.selectedItem.toString())
        intent.putExtra("vehicle_brand", spinnerVehicleBrand.selectedItem.toString())
        intent.putExtra("vehicle_model", spinnerVehicleModel.selectedItem.toString())
        intent.putExtra("vehicle_variant", spinnerVehicleVariant.selectedItem.toString())
        intent.putExtra("vehicle_year", spinnerVehicleYear.selectedItem.toString())
        intent.putExtra("variant_price", variantPrice.toString())
        //////////////////////////////////////////////////////////////////////////
        intent.putExtra("company_name", insuranceCompanyName)
        intent.putExtra("company_uid", insuranceCompanyUID)
        intent.putExtra("insurance_name", insuranceName)
        intent.putExtra("insurance_uid", insuranceUID)
        intent.putExtra("insurance_class", insuranceClass)
        intent.putExtra("item_covered_1", itemCovered1)
        intent.putExtra("item_covered_2", itemCovered2)
        intent.putExtra("item_covered_3", itemCovered3)
        intent.putExtra("item_covered_4", itemCovered4)
        intent.putExtra("item_covered_5", itemCovered5)
        intent.putExtra("item_covered_6", itemCovered6)
        intent.putExtra("item_covered_7", itemCovered7)
        intent.putExtra("item_covered_8", itemCovered8)
        startActivity(intent)
    }

    fun isValidPlateNum(plateNum: String?): Boolean {
        plateNum?.let {
            val plateNumPattern = "^[a-zA-Z][a-zA-Z][a-zA-Z]\\d\\d\\d\\d\$"
            val plateNumMatcher = Regex(plateNumPattern)

            return plateNumMatcher.find(plateNum) != null
        } ?: return false
    }

    private fun loadSpinner() {
        //------------------------------------------------------Get data(Vehicle Type) from firebase
        val type = ArrayList<String>()
        val typeRef = database.getReference("Type")

        typeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        if (i.child("type_status").value.toString() == "Available"){
                            type.add(i.child("type_name").value.toString())
                        }
                    }
                    val typeAdapter = ArrayAdapter<String>(
                        this@FillInVehicleDetails,
                        android.R.layout.simple_list_item_1,
                        type
                    )
                    typeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                    spinnerVehicleType.adapter = typeAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "An error has occurred.", Toast.LENGTH_LONG)
                    .show()
            }
        })
        //------------------------------------------------------Get data(Vehicle Brand) from firebase
        val brand = ArrayList<String>()
        val brandRef = database.getReference("Brand")

        brandRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        if (i.child("brand_status").value.toString() == "Available"){
                            brand.add(i.child("brand_name").value.toString())
                        }
                    }
                    val brandAdapter = ArrayAdapter<String>(
                        this@FillInVehicleDetails,
                        android.R.layout.simple_list_item_1,
                        brand
                    )
                    brandAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                    spinnerVehicleBrand.adapter = brandAdapter
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