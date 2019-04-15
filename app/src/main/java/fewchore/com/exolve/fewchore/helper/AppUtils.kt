package fewchore.com.exolve.fewchore.helper

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.Log
import fewchore.com.exolve.fewchore.R
import java.io.IOException
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL
import java.net.UnknownHostException
import java.util.concurrent.*
import android.net.ConnectivityManager
import android.os.NetworkOnMainThreadException


class AppUtils(private val context: Context) {

    fun showAlert(paramString: String) {
        val localBuilder = AlertDialog.Builder(this.context)
        localBuilder.setMessage(paramString)
        localBuilder.setNeutralButton(R.string.ok) { _, _ -> }
        localBuilder.create().show()
    }

    companion object {
        var PREF_EMAIL: String = "user_email"
        var PREF_FIRSTNAME: String
        var PREF_IS_LOGGEDIN: String = "isloggedin"
        var PREF_LASTNAME: String
        var PREF_PASSWORD = "password"
        var PREF_PHONENUMBER: String ="phone"
        var PREF_PROFILE: String ="profile"
        var PREF_PIN = "four_digit_pin"
        var PREF_USERID: String = "user_id"
        var PREF_BVN: String = "bvn"
        var PREF_CONTACT: String = "contact"
        var PREF_LOAN_STATUS : String = "loan_status"
        var PREF_INVESTMENT_STATUS : String = "invest_status"
        var PREF_USER_STATUS : String = "user_status"
        var LOG_TAG: String="Network check:"

        private var sharedPreferenceUtil: SharedPreferenceUtil? = null

        init {
            PREF_FIRSTNAME = "first_name"
            PREF_LASTNAME = "last_name"
            PREF_PHONENUMBER = "phone_number"
        }

        fun getMyEmail(context: Context?): String {
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(context!!.applicationContext, PREF_EMAIL)!!
            } catch (ex: Exception) {
                return ""
            }

        }

        fun getMyFirstName(paramContext: Context?): String? {
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(paramContext!!.applicationContext, PREF_FIRSTNAME)
            } catch (e: Exception) {
            }

            return ""
        }
        fun getMyProfile(paramContext: Context?): String? {
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(paramContext!!.applicationContext, PREF_PROFILE)
            } catch (e: Exception) {
            }

            return ""
        }


        fun getMyLoanStatus(paramContext: Context?): String? {
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(paramContext!!.applicationContext, PREF_LOAN_STATUS)
            } catch (e: Exception) {
            }

            return ""
        }

        fun getMyInvestStatus(paramContext: Context?): String? {
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(paramContext!!.applicationContext, PREF_INVESTMENT_STATUS)
            } catch (e: Exception) {
            }

            return ""
        }

        fun getMyUserStatus(paramContext: Context?): String? {
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(paramContext!!.applicationContext, PREF_USER_STATUS)
            } catch (e: Exception) {
            }

            return ""
        }

        fun getMyLastName(paramContext: Context?): String? {
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(paramContext!!.applicationContext, PREF_LASTNAME)

            } catch (e: Exception) {
            }

            return ""
        }

        fun getMyPassword(paramContext: Context?): String? {
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(paramContext!!.applicationContext, PREF_PASSWORD)

            } catch (e: Exception) {
            }

            return ""
        }

        fun getMyBVN(paramContext: Context?): String? {
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(paramContext!!.applicationContext, PREF_BVN)

            } catch (e: Exception) {
            }

            return ""
        }
        fun getMyContact(paramContext: Context?): String? {
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(paramContext!!.applicationContext, PREF_CONTACT)

            } catch (e: Exception) {
            }

            return ""
        }

        fun getMyPhoneNumber(paramContext: Context?): String? {
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(paramContext!!.applicationContext, PREF_PHONENUMBER)

            } catch (e: Exception) {
            }

            return ""
        }



        fun getMyUserId(paramContext: Context?): String ?{
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(paramContext!!.applicationContext, PREF_USERID)

            } catch (e: Exception) {
            }

            return ""
        }

        fun getMyPin(context: Context?): String? {
            sharedPreferenceUtil = SharedPreferenceUtil
            try {
                return sharedPreferenceUtil!!.getValue(context!!.applicationContext, PREF_PIN)
            } catch (e: Exception){

            }
            return ""
        }

    }

    fun internetConnectionAvailable( timeOut: Long): Boolean {
        var inetAddress: InetAddress? = null
        try {
            val future = Executors.newSingleThreadExecutor().submit(Callable<InetAddress> {
                try {
                    return@Callable InetAddress.getByName("google.com")
                } catch (e: UnknownHostException) {
                    return@Callable null
                }
            })
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS)
            future.cancel(true)
        } catch (e: InterruptedException) {
        } catch (e: ExecutionException) {
        } catch (e: TimeoutException) {
        }

        return inetAddress != null && !inetAddress.equals("")
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = this.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null
    }
    fun hasActiveInternetConnection(context: Context): Boolean {
        if (isNetworkAvailable()) {
            try {
                val urlc = URL("http://www.google.com").openConnection() as HttpURLConnection
                urlc.setRequestProperty("User-Agent", "Test")
                urlc.setRequestProperty("Connection", "close")
                urlc.connectTimeout = 1500
                urlc.connect()
                return urlc.responseCode == 200
            } catch (e: Exception) {
                if (e is IOException)
                    Log.e(LOG_TAG, "Error checking internet connection", e)
                if (e is NetworkOnMainThreadException)
                    Log.e(LOG_TAG, " connection", e)

            }

        } else {
            Log.d(LOG_TAG, "No network available!")
        }
        return false
    }
}
