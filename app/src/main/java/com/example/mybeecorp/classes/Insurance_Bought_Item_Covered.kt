package my.com.customer.classes

class Insurance_Bought_Item_Covered(
    val ins_bought_item_uid: String,
    val item_name: String,
    val item_price: Double,
    val item_status: String
    ) {
    constructor() : this("", "", 0.0, "")
}