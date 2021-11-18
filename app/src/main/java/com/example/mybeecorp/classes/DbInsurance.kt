package com.example.mybeecorp.classes

class DbInsurance (val insurance_uid:String, val insurance_name:String, val insurance_company:String
                   , val insurance_class:String, val insurance_status:String){

    constructor():this("","","","","")
}