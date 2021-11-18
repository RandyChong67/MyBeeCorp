package my.com.customer.classes

class Insurance_Bought(
    val ins_bought_uid: String,
    val vehicle_uid: String,
    val user_uid: String,
    val insurance_name: String,
    val insurance_company: String,
    val yearly_payment: Double,
    val date_of_bought: String,
    val insurance_subscription_status: String
) {
    constructor() : this("", "", "","","", 0.0, "", "")
}