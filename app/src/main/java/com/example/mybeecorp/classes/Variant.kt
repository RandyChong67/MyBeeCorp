package com.example.mybeecorp.classes

class Variant(
    var variant_id: String,
    var variant_name: String,
    var variant_price: Double,
    var variant_status: String,
    var model_name: String
) {
    constructor():this("","", 0.0, "", "")
}