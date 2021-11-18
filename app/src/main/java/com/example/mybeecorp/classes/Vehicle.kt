package com.example.mybeecorp.classes

class Vehicle(
    var vehicle_uid: String,
    var vehicle_plate_num: String,
    var vehicle_type: String,
    var vehicle_brand: String,
    var vehicle_model: String,
    var vehicle_variant: String,
    var vehicle_year: String,
    var vehicle_owner: String,
    var vehicle_status: String
) {
    constructor():this("","","","","","","", "","")
}