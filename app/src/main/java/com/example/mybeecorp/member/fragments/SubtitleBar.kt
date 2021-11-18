package com.example.mybeecorp.member.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mybeecorp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [SubtitleBar.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubtitleBar : Fragment() {
    // TODO: Rename and change types of parameters
    private var subTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subTitle = it.getString(ARG_PARAM1)
        }
    }

    //Coding Here !!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_subtitle_bar, container, false)
        view.findViewById<TextView>(R.id.subTitle).text = subTitle

        //Onclick Filter or Sort Function !!

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param //param1 Parameter 1.
         * @return A new instance of fragment SubtitleBar.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(subTitle: String) =
            SubtitleBar().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, subTitle)
                }
            }
    }
}