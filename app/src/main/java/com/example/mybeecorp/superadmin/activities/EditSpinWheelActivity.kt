package com.example.mybeecorp.superadmin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Spin_Wheel
import com.example.mybeecorp.superadmin.adapters.ViewPagerAdapter
import com.example.mybeecorp.superadmin.fragments.PrizesFragment
import com.example.mybeecorp.superadmin.fragments.PrizesHistoryFragment
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit_spin_wheel.*
import java.util.*

class EditSpinWheelActivity : AppCompatActivity() {

    private var sectors = mutableListOf<Spin_Wheel>()
    private lateinit var spinWheel: ImageView
    private lateinit var buttonSpin: Button
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_spin_wheel)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.title = "Spin Wheel"
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")

        spinWheel = findViewById(R.id.imageView2)
        buttonSpin = findViewById(R.id.buttonSpin)

        setUpTabs()
        loadData()

        buttonSpin.setOnClickListener {
            spinTheWheel()
        }

    }

    private fun loadData() {
        reference = database.getReference("Spin_Wheel")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sectors.clear()
                for (data in snapshot.children) {
                    var model = data.getValue(Spin_Wheel::class.java)
                    sectors.add(model as Spin_Wheel)
                }
                sectors = sectors.asReversed()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })

    }

    private fun spinTheWheel() {
        buttonSpin.isEnabled = false
        buttonSpin.isClickable = false
        val rr = Random()
        var degree = rr.nextInt(360)
        while (degree == 45 || degree == 90 || degree == 135 || degree == 180 || degree == 225 || degree == 270 || degree == 315) {
            degree = rr.nextInt(360)
        }
        val rotateAnimation = RotateAnimation(
            0F, degree + 2160F,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = 6500
        rotateAnimation.fillAfter = true
        rotateAnimation.interpolator = DecelerateInterpolator()
        val finalDegree = degree
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                CalculatePoint(finalDegree,buttonSpin)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        spinWheel.startAnimation(rotateAnimation)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(PrizesFragment(),"Prizes")
        adapter.addFragment(PrizesHistoryFragment(), "Spin History")
        viewPagerSpinWheel.adapter = adapter
        tabs.setupWithViewPager(viewPagerSpinWheel)
    }

    fun CalculatePoint(degree: Int, buttonSpin: Button) {
//      total degree 360 || 8 segment || 45 degree each segment
        var initialPoint = 0
        var endPoint = 45
        var i = 0
        var res: String? = null
        do {
            if (degree in (initialPoint + 1) until endPoint) {
                res = sectors[i].spin_wheel_name
            }
            initialPoint += 45
            endPoint += 45
            i++
        } while (res == null)
        val builder = AlertDialog.Builder(this)
        builder.setMessage("You won $res")
        builder.setTitle("Congratulation!")
        builder.setCancelable(false)
        builder.setPositiveButton("Yay") { dialog, _-> dialog.dismiss() }
        val alert = builder.create()
        alert.show()
        buttonSpin.isEnabled = true
        buttonSpin.isClickable = true
    }

}