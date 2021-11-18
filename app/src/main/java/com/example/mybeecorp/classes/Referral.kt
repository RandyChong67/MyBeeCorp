package com.example.mybeecorp.classes

class Referral(
    val referral_uid: String,
    val inviter_uid: String,
    val invited_uid: String,
    val insurance_recommended: String,
    val referral_status: String
) {
    constructor() : this("", "", "", "", "")
}