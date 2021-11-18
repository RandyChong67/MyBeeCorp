package my.com.customer.classes

class Item_Covered(
    val item_uid: String,
    val item_name: String,
    val insurance_uid: String,
    val item_price: String,
    val item_status: String
) {
    constructor() : this("", "", "", "", "")
}