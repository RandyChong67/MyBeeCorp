package com.example.mybeecorp.classes

class User(
    var user_uid: String,
    var user_full_name: String,
    var user_dial_code: Int,
    var user_phone_num: String,
    var user_email: String,
    var user_bank_card_num: String?,
    var user_bank_pin: String?,
    var user_avatar: String?,
    var user_role: String,
    var user_status: String,
    var ins_company_name: String?,
    var point_awarded: Int?,
    var spin_awarded: Int?,
    var referral_id: String?
) {
}