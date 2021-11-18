package my.com.customer.classes

class Voucher(
    var voucher_uid: String,
    var voucher_code: String,
    var voucher_description: String,
    var voucher_discount: Int,
    var voucher_expiry_date: String,
    var voucher_status: String
) {
    constructor():this("","","", 0,"","")
}