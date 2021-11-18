package my.com.customer.classes

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.StrictMode
import java.net.URL

class Insurance_Company(
    val company_uid: String,
    val company_name: String,
    val company_logo: String,
    val company_status: String
) {
    var bitmap: Bitmap? = null

    fun createBitmap() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val input = URL(company_logo).openStream()
        bitmap = BitmapFactory.decodeStream(input)
    }

    constructor() : this("", "", "", "")
}