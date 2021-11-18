package com.example.mybeecorp.interfaces

import android.widget.EditText

interface Communicator {
    fun passDataCom2(fullname: String?, role: String?, email: String?, password: String?, insuranceCompany: String?)

    fun goBackFragment1(fullname: String?, role: String?, email: String?, password: String?, insuranceCompany: String?)
    fun goBackFragment2(fullname: String?, role: String?, email: String?, password: String?, insuranceCompany: String?)
}