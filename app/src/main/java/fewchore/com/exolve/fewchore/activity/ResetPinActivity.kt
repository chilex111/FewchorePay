package fewchore.com.exolve.fewchore.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.gson.Gson
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.AppUtils.Companion.PREF_PIN
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.helper.SharedPreferenceUtil
import fewchore.com.exolve.fewchore.model.ResponseStringModel
import java.io.IOException
import java.util.*


@SuppressLint("Registered")
class ResetPinActivity : AppCompatActivity() {
    private var editTextPin: EditText? = null
    private var editTextConfPin: EditText? = null
    private var passwordEditText: EditText? = null
    private var buttonPin: Button? = null
    private var userId: String? = null
    private var userPIN: String? = null
    private var appUtils: AppUtils? = null
    private var sharedPrefence: SharedPreferenceUtil? = null
    private var pinText: String ?= null
    private var relativeLayout: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pin)

        userId = AppUtils.getMyUserId(this)
        userPIN = AppUtils.getMyPin(this)


        editTextPin = findViewById(R.id.editTextPin)
        editTextConfPin = findViewById(R.id.editTextConfirmNewPin)

        relativeLayout = findViewById(R.id.relativeProgress)

        buttonPin = findViewById(R.id.buttonPin)
        buttonPin!!.setOnClickListener {
            submit()
        }
    }

    private fun submit() {

          pinText = editTextPin!!.text.toString()
        val newPinText = editTextConfPin!!.text.toString()

        if (TextUtils.isEmpty(pinText)) {
            editTextPin!!.error = "This field is required"
        }
        if (pinText!!.length != 4) {
            editTextPin!!.error = "Pin length is 4 digit"
        }
        if (pinText != newPinText) {
            editTextConfPin!!.error = "PIN do not match"
        } else {
            relativeLayout!!.visibility = View.VISIBLE
            ResetPinSetUpAsync(pinText!!).execute()
        }
    }

    private fun setupPin(url: String): String {
        val map = HashMap<String, Any?>()
        map["user_id"] = userId
        map["user_pin"] = userPIN


        return HttpUtility.sendPostRequest(url, map)
    }

    @SuppressLint("StaticFieldLeak")
    inner class ResetPinSetUpAsync (private val pinText: String) : AsyncTask<Void, Int, String>() {
        private var url: String? = null

        override fun doInBackground(vararg voids: Void): String? {
            url = Const.FEWCHORE_URL + "addpin"
            try {
                return setupPin(url!!)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            appUtils = AppUtils(this@ResetPinActivity)
            relativeLayout!!.visibility = View.GONE
            if (s != null) {
                try {
                    val gson = Gson()
                    val responseMessage = gson.fromJson<ResponseStringModel>(s, ResponseStringModel::class.java)
                    if (responseMessage.status =="true") {
                        val msg = responseMessage.msg
                        sharedPrefence = SharedPreferenceUtil
                        sharedPrefence!!.save(this@ResetPinActivity, pinText, PREF_PIN)

                        val builder = AlertDialog.Builder(this@ResetPinActivity)
                        builder.setMessage(msg)
                        builder.setNeutralButton(R.string.ok) { _, _ -> startActivity(Intent(this@ResetPinActivity, LoginActivity::class.java)) }

                        builder.create().show()
                    }
                    else {
                        val message = responseMessage.msg!!
                        appUtils!!.showAlert(message)

                    }
                } catch (e: Exception) {
                    Toast.makeText(this@ResetPinActivity, "Please check your network connection", Toast.LENGTH_LONG).show()

                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)
                }

            }
        }
    }
}
