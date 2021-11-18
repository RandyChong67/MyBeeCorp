package com.example.mybeecorp.member.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.User
import com.example.mybeecorp.member.adapters.ItemCoveredListAdapter
import com.google.firebase.auth.FirebaseAuth
import my.com.customer.classes.Insurance_Bought
import my.com.customer.classes.Insurance_Bought_Item_Covered
import my.com.customer.classes.Item_Covered
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ValidateInsuranceDetailActivity : AppCompatActivity() {

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
    private var plateNum: String = ""
    private var vehicleType: String = ""
    private var vehicleBrand: String = ""
    private var vehicleModel: String = ""
    private var vehicleVariant: String = ""
    private var vehicleYear: String = ""
    private var variantPrice: Double = 0.0
    private lateinit var textCompanyName: TextView
    private lateinit var textInsuranceName: TextView
    private lateinit var textInsuranceClass: TextView
    private lateinit var recyclerItemCovered: RecyclerView
    private lateinit var spinnerVoucher: Spinner
    private lateinit var textDiscount: TextView
    private lateinit var textSubPrice: TextView
    private lateinit var textPlateNum: TextView
    private lateinit var textType: TextView
    private lateinit var textBrand: TextView
    private lateinit var textModel: TextView
    private lateinit var textVariant: TextView
    private lateinit var textYear: TextView
    private lateinit var textYearlyPayment: TextView
    private lateinit var textInviterUID: EditText
    private lateinit var buttonInfo: ImageView
    private lateinit var buttonPurchase: Button
    private lateinit var textInviterIDReplacer: TextView
    private var voucherNameList = mutableListOf<String>()
    private var voucherPriceList = mutableListOf<Int>()
    private var itemCoveredName: MutableList<String> = mutableListOf()
    private var itemCoveredPrice: MutableList<String> = mutableListOf()
    private var totalItemCoveredPrice: Double = 0.0
    private var totalDiscountedPrice: Double = 0.0
    private var subTotalPrice: Double = 0.0
    private var yearlyPayment: Double = 0.0
    private var count: Int = 0
    private var userList = mutableListOf<User>()
    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validate_insurance_detail)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Validate Insurance Details"

        plateNum = intent.getStringExtra("plate_num")!!
        vehicleType = intent.getStringExtra("vehicle_type")!!
        vehicleBrand = intent.getStringExtra("vehicle_brand")!!
        vehicleModel = intent.getStringExtra("vehicle_model")!!
        vehicleVariant = intent.getStringExtra("vehicle_variant")!!
        vehicleYear = intent.getStringExtra("vehicle_year")!!
        variantPrice = intent.getStringExtra("variant_price").toString().toDouble()
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

        textCompanyName = findViewById(R.id.text_company_name)
        textInsuranceName = findViewById(R.id.text_insurance_name)
        textInsuranceClass = findViewById(R.id.text_insurance_class)
        recyclerItemCovered = findViewById(R.id.recycler_item_covered)
        spinnerVoucher = findViewById(R.id.spinner_voucher)
        textDiscount = findViewById(R.id.text_discount)
        textSubPrice = findViewById(R.id.text_sub_price)

        textPlateNum = findViewById(R.id.text_plate_num)
        textType = findViewById(R.id.text_type)
        textBrand = findViewById(R.id.text_brand)
        textModel = findViewById(R.id.text_model)
        textVariant = findViewById(R.id.text_variant)
        textYear = findViewById(R.id.text_year)
        textInviterUID = findViewById(R.id.edit_text_inviter_id)
        textInviterIDReplacer = findViewById(R.id.text_inviter_id_replacer)
        buttonInfo = findViewById(R.id.image_info)
        textYearlyPayment = findViewById(R.id.text_yearly_payment)
        buttonPurchase = findViewById(R.id.button_purchase)

        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        textCompanyName.text = insuranceCompanyName
        textInsuranceName.text = insuranceName
        textInsuranceClass.text = insuranceClass
        textType.text = vehicleType
        textBrand.text = vehicleBrand
        textModel.text = vehicleModel
        textVariant.text = vehicleVariant
        textYear.text = vehicleYear
        textPlateNum.text = plateNum
//        textInviterUID.setText("j8myokg6afOpWLyJmgnCG4f96uN2") //--------------------------------------- Delete this

        loadItemCovered()
        loadVoucher()

        buttonInfo.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("\nEnter Inviter UID to get MyBee Points!\nYou can copy it from the link given by your inviter.")
            builder.setTitle("Inviter UID")
            builder.setCancelable(false)
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val alert = builder.create()
            alert.show()
        }

        spinnerVoucher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!!.getItemAtPosition(position).toString() == "No Voucher Selected") {
                    totalDiscountedPrice = 0.00
                    val df = DecimalFormat("#.00")
                    df.roundingMode = RoundingMode.CEILING
                    totalDiscountedPrice = df.format(totalDiscountedPrice).toDouble()
                    subTotalPrice = df.format(totalItemCoveredPrice).toDouble()
                    yearlyPayment = subTotalPrice + variantPrice

                    textDiscount.text = String.format("- RM %.2f", totalDiscountedPrice)
                    textSubPrice.text = String.format("RM %.2f", totalItemCoveredPrice)
                    textYearlyPayment.text = String.format("RM %.2f", yearlyPayment)
                } else {

                    totalDiscountedPrice = totalItemCoveredPrice * voucherPriceList[position] / 100
                    subTotalPrice = totalItemCoveredPrice - totalDiscountedPrice

                    val df = DecimalFormat("#.00")
                    df.roundingMode = RoundingMode.CEILING
                    totalDiscountedPrice = df.format(totalDiscountedPrice).toDouble()
                    subTotalPrice = df.format(subTotalPrice).toDouble()
                    yearlyPayment = subTotalPrice + variantPrice

                    textDiscount.text = String.format("- RM %.2f", totalDiscountedPrice)
                    textSubPrice.text = String.format("RM %.2f", subTotalPrice)
                    textYearlyPayment.text = String.format("RM %.2f", yearlyPayment)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        buttonPurchase.setOnClickListener {

            if (textInviterUID.text.trim().toString().isNotEmpty()) {
                val uid = FirebaseAuth.getInstance().uid
                var i = 0
                reference = database.getReference("User")
                reference.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        userList.clear()
                        if (snapshot.exists()) {
                            for (data in snapshot.children) {
                                if (data.child("user_uid").value.toString() == textInviterUID.text.toString()) {
                                    textInviterIDReplacer.text =
                                        data.child("user_uid").value.toString()
                                    Log.d("Test Voucher 1", "Hello")
                                    Log.d("Test Voucher 2", textInviterIDReplacer.text.toString())
                                }
                            }
                        }
                        if (i == 0) {
                            if (textInviterUID.text.toString() != uid) {
                                if (textInviterUID.text.toString() == textInviterIDReplacer.text.toString()) {
                                    val intent = Intent(
                                        this@ValidateInsuranceDetailActivity,
                                        PaymentGatewayActivity::class.java
                                    )
                                    intent.putExtra(
                                        "plate_num",
                                        plateNum.uppercase(Locale.getDefault())
                                    )
                                    intent.putExtra("vehicle_type", vehicleType)
                                    intent.putExtra("vehicle_brand", vehicleBrand)
                                    intent.putExtra("vehicle_model", vehicleModel)
                                    intent.putExtra("vehicle_variant", vehicleVariant)
                                    intent.putExtra("vehicle_year", vehicleYear)
                                    intent.putExtra("variant_price", variantPrice)
                                    //////////////////////////////////////////////////////////////////////////
                                    intent.putExtra("company_name", insuranceCompanyName)
                                    intent.putExtra("company_uid", insuranceCompanyUID)
                                    intent.putExtra("insurance_name", insuranceName)
                                    intent.putExtra("insurance_uid", insuranceUID)
                                    intent.putExtra("insurance_class", insuranceClass)
                                    intent.putExtra("yearly_payment", yearlyPayment)
//                                intent.putExtra("inviter_uid", "j8myokg6afOpWLyJmgnCG4f96uN2")
                                    intent.putExtra("inviter_uid", textInviterUID.text.toString())
                                    intent.putStringArrayListExtra(
                                        "item_covered_name_list",
                                        ArrayList(itemCoveredName)
                                    )
                                    intent.putStringArrayListExtra(
                                        "item_covered_price_list",
                                        ArrayList(itemCoveredPrice)
                                    )
                                    startActivity(intent)
                                } else {
                                    textInviterUID.error = "Invalid Inviter UID."
                                }
                            } else {
                                textInviterUID.error = "Cannot put your own UID."
                            }

                        }
                        i++
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
                Log.d("Test Voucher 3", "Hello")
                Log.d("Test Voucher 4", textInviterIDReplacer.text.toString())


            } else {
                val intent = Intent(this, PaymentGatewayActivity::class.java)
                intent.putExtra("plate_num", plateNum.uppercase(Locale.getDefault()))
                intent.putExtra("vehicle_type", vehicleType)
                intent.putExtra("vehicle_brand", vehicleBrand)
                intent.putExtra("vehicle_model", vehicleModel)
                intent.putExtra("vehicle_variant", vehicleVariant)
                intent.putExtra("vehicle_year", vehicleYear)
                intent.putExtra("variant_price", variantPrice)
                //////////////////////////////////////////////////////////////////////////
                intent.putExtra("company_name", insuranceCompanyName)
                intent.putExtra("company_uid", insuranceCompanyUID)
                intent.putExtra("insurance_name", insuranceName)
                intent.putExtra("insurance_uid", insuranceUID)
                intent.putExtra("insurance_class", insuranceClass)
                intent.putExtra("yearly_payment", yearlyPayment)
                intent.putExtra("inviter_uid", "")
                intent.putStringArrayListExtra("item_covered_name_list", ArrayList(itemCoveredName))
                intent.putStringArrayListExtra(
                    "item_covered_price_list",
                    ArrayList(itemCoveredPrice)
                )
                startActivity(intent)
            }

        }
    }

    private fun validateCounter() {
        Log.d("Test Voucher 3", "Hello")
        Log.d("Test Voucher 4", count.toString())

    }

    private fun validateInviterUID() {

    }

    private fun loadVoucher() {
        reference = database.getReference("Voucher")
        reference.orderByChild("voucher_code").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                voucherNameList.clear()
                voucherPriceList.clear()

                if (snapshot.exists()) {
                    voucherNameList.add("No Voucher Selected")
                    voucherPriceList.add(0)
                    for (data in snapshot.children) {
                        if (data.child("voucher_status").value.toString() == "Available") {
                            voucherNameList.add(data.child("voucher_code").value.toString())
                            voucherPriceList.add(
                                data.child("voucher_discount").value.toString().toInt()
                            )
                        }
                    }
                    val adapter = ArrayAdapter(
                        this@ValidateInsuranceDetailActivity,
                        android.R.layout.simple_list_item_1, voucherNameList
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
                    spinnerVoucher.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun loadItemCovered() {
        reference = database.getReference("Item_Covered/$insuranceUID")
        var itemCoveredSelected = mutableListOf<Item_Covered>()

        reference.orderByChild("item_name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                itemCoveredName.clear()
                itemCoveredPrice.clear()
                itemCoveredSelected.clear()
                for (data in snapshot.children) {
                    if (itemCovered1 != "No") {
                        if (data.child("item_name").value.toString() == itemCovered1) {
                            totalItemCoveredPrice += data.child("item_price").value.toString()
                                .toDouble()
                            val model = data.getValue(Item_Covered::class.java)
                            itemCoveredSelected.add(model!!)
                            itemCoveredName.add(data.child("item_name").value.toString())
                            itemCoveredPrice.add(data.child("item_price").value.toString())
                        }
                    }
                    if (itemCovered2 != "No") {
                        if (data.child("item_name").value.toString() == itemCovered2) {
                            totalItemCoveredPrice += data.child("item_price").value.toString()
                                .toDouble()
                            val model = data.getValue(Item_Covered::class.java)
                            itemCoveredSelected.add(model!!)
                            itemCoveredName.add(data.child("item_name").value.toString())
                            itemCoveredPrice.add(data.child("item_price").value.toString())
                        }
                    }
                    if (itemCovered3 != "No") {
                        if (data.child("item_name").value.toString() == itemCovered3) {
                            totalItemCoveredPrice += data.child("item_price").value.toString()
                                .toDouble()
                            val model = data.getValue(Item_Covered::class.java)
                            itemCoveredSelected.add(model!!)
                            itemCoveredName.add(data.child("item_name").value.toString())
                            itemCoveredPrice.add(data.child("item_price").value.toString())
                        }
                    }
                    if (itemCovered4 != "No") {
                        if (data.child("item_name").value.toString() == itemCovered4) {
                            totalItemCoveredPrice += data.child("item_price").value.toString()
                                .toDouble()
                            val model = data.getValue(Item_Covered::class.java)
                            itemCoveredSelected.add(model!!)
                            itemCoveredName.add(data.child("item_name").value.toString())
                            itemCoveredPrice.add(data.child("item_price").value.toString())
                        }
                    }
                    if (itemCovered5 != "No") {
                        if (data.child("item_name").value.toString() == itemCovered5) {
                            totalItemCoveredPrice += data.child("item_price").value.toString()
                                .toDouble()
                            val model = data.getValue(Item_Covered::class.java)
                            itemCoveredSelected.add(model!!)
                            itemCoveredName.add(data.child("item_name").value.toString())
                            itemCoveredPrice.add(data.child("item_price").value.toString())
                        }
                    }
                    if (itemCovered6 != "No") {
                        if (data.child("item_name").value.toString() == itemCovered6) {
                            totalItemCoveredPrice += data.child("item_price").value.toString()
                                .toDouble()
                            val model = data.getValue(Item_Covered::class.java)
                            itemCoveredSelected.add(model!!)
                            itemCoveredName.add(data.child("item_name").value.toString())
                            itemCoveredPrice.add(data.child("item_price").value.toString())
                        }
                    }
                    if (itemCovered7 != "No") {
                        if (data.child("item_name").value.toString() == itemCovered7) {
                            totalItemCoveredPrice += data.child("item_price").value.toString()
                                .toDouble()
                            val model = data.getValue(Item_Covered::class.java)
                            itemCoveredSelected.add(model!!)
                            itemCoveredName.add(data.child("item_name").value.toString())
                            itemCoveredPrice.add(data.child("item_price").value.toString())
                        }
                    }
                    if (itemCovered8 != "No") {
                        if (data.child("item_name").value.toString() == itemCovered8) {
                            totalItemCoveredPrice += data.child("item_price").value.toString()
                                .toDouble()
                            val model = data.getValue(Item_Covered::class.java)
                            itemCoveredSelected.add(model!!)
                            itemCoveredName.add(data.child("item_name").value.toString())
                            itemCoveredPrice.add(data.child("item_price").value.toString())
                        }
                    }
                }
                if (itemCoveredSelected.size > 0) {
                    recyclerItemCovered.layoutManager =
                        LinearLayoutManager(this@ValidateInsuranceDetailActivity)
                    recyclerItemCovered.adapter =
                        ItemCoveredListAdapter(itemCoveredSelected)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}