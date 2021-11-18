package com.example.mybeecorp.classes

class Spin_History(
    var spin_history_uid: String,
    var spin_wheel_name: String,
    var user_full_name: String,
    var spin_date_time: String,
    var spin_status: String
) {
    constructor():this("","","","","")
}