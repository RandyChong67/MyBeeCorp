package com.example.mybeecorp.member.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.mybeecorp.R
import java.text.SimpleDateFormat
import java.util.*

class PaymentSuccessfulActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_successful)

        val buttonGoHome = findViewById<Button>(R.id.button_go_home)
        val textSubscription = findViewById<TextView>(R.id.text_subscription)

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())

        textSubscription.text = "Your subscription will start on $currentDate"

        buttonGoHome.setOnClickListener {
            val intent = Intent(this, MemberMainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}