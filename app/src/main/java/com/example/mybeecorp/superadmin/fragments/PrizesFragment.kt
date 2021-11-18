package com.example.mybeecorp.superadmin.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybeecorp.R
import com.example.mybeecorp.classes.Spin_Wheel
import com.example.mybeecorp.superadmin.adapters.SpinWheelPrizesRecyclerAdapter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_prizes.*

class PrizesFragment : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private var prizeList = ArrayList<Spin_Wheel>()
    private var prizePosition = ArrayList<String>()
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prizes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database =
            Firebase.database("https://mybeecorp-cdb46-default-rtdb.asia-southeast1.firebasedatabase.app/")
        readDatabase(view)
    }

    private fun readDatabase(view: View) {
        reference = database.getReference("Spin_Wheel")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                prizeList.clear()
                var i = 1
                for(data in snapshot.children){
                    var model = data.getValue(Spin_Wheel::class.java)
                    prizeList.add(model as Spin_Wheel)
                    prizePosition.add("${i++}")
                }


                if(prizeList.size > 0){
                    val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
                    progressBar.isVisible = false
                    linearLayoutManager = LinearLayoutManager(view.context)
                    recycleViewPrizes.layoutManager = linearLayoutManager
                    recycleViewPrizes.adapter =
                        SpinWheelPrizesRecyclerAdapter(
                            prizeList, prizePosition
                        )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString())
            }

        })
    }
}