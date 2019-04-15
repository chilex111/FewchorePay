package fewchore.com.exolve.fewchore.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
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
import fewchore.com.exolve.fewchore.helper.*
import fewchore.com.exolve.fewchore.helper.AppUtils.Companion.PREF_PIN
import fewchore.com.exolve.fewchore.listener.PinTextListener
import fewchore.com.exolve.fewchore.model.ResponseStringModel
import java.io.IOException
import java.lang.IllegalStateException
import java.util.*

@SuppressLint("Registered")
class SetUpPinActivity : AppCompatActivity(), PinTextListener {
    private var editTextPin: EditText? = null
    private var buttonPin: Button? = null
    private var userId: String? = null
    private var appUtils: AppUtils? = null
    private var sharedPrefence: SharedPreferenceUtil? = null
    private var pinChar1: EditText ?= null
    private var pinChar2: EditText ?= null
    private var pinChar3: EditText ?= null
    private var pinChar4: EditText ?= null
    private var relativeProgress: RelativeLayout ?= null
    private var code : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_up_pin)


        userId = AppUtils.getMyUserId(this)


        editTextPin = findViewById(R.id.editTextPin)
        buttonPin = findViewById(R.id.buttonPin)
        pinChar1 = findViewById(R.id.pinChar1)
        pinChar2 = findViewById(R.id.pinChar2)
        pinChar3 = findViewById(R.id.pinChar3)
        pinChar4 = findViewById(R.id.pinChar4)
        relativeProgress = findViewById(R.id.relativeProgress)

        buttonPin!!.setOnClickListener {
            val pinText = editTextPin!!.text.toString()

            if (TextUtils.isEmpty(pinText)) {
                editTextPin!!.error = "This field is required"
            } else {
                PinSetUpAsync(pinText).execute()
            }
        }
        pinChar1!!.addTextChangedListener(PinFormatter(pinChar2, this))
        pinChar2!!.addTextChangedListener(PinFormatter(pinChar3, this))
        pinChar3!!.addTextChangedListener(PinFormatter(pinChar4, this))
        pinChar4!!.addTextChangedListener(PinFormatter(pinChar1, this))
    }



    override fun onTextChanged() {

        val str1 = pinChar1!!.text.toString()
        val str2 = pinChar2!!.text.toString()
        val str3 = pinChar3!!.text.toString()
        val str4 = pinChar4!!.text.toString()

        if (str1.isNotEmpty() && str2.isNotEmpty() && str3.isNotEmpty() && str4.isNotEmpty() ) {
             code = str1 + str2 + str3 + str4


            if (code!!.isEmpty()) {
                pinChar1!!.error = "This field is required"
            } else {
                relativeProgress!!.visibility = View.VISIBLE
                PinSetUpAsync(code!!).execute()
            }
        }
    }
    @Throws(IOException::class)
    private fun setupPin(url: String, pinText: String): String {
        val map = HashMap<String, Any?>()
        map["user_id"] = userId
        map["user_pin"] = pinText

        return HttpUtility.sendPostRequest(url, map)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class PinSetUpAsync (private val pinText: String) : AsyncTask<Void, Int, String>() {
        private var url: String? = null

        override fun doInBackground(vararg voids: Void): String? {
            url = Const.FEWCHORE_URL + "addpin"
            try {
                return setupPin(url!!, pinText)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)

            appUtils = AppUtils(this@SetUpPinActivity)

            if (s != null) {
                try {
                    val gson = Gson()
                    val responseMessage = gson.fromJson<ResponseStringModel>(s, ResponseStringModel::class.java)
                    if (responseMessage.status =="true") {
                        sharedPrefence = SharedPreferenceUtil
                        sharedPrefence!!.save(this@SetUpPinActivity, pinText, PREF_PIN)
                        startActivity(Intent(this@SetUpPinActivity, LoginActivity::class.java))

                    } else {
                        val message = responseMessage.msg!!
                        appUtils!!.showAlert(message)
                    }
                } catch (e: Exception) {
                    if (e is IllegalStateException)
                        Toast.makeText(this@SetUpPinActivity, "Please check your network connection", Toast.LENGTH_LONG).show()

                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)

                }

            }
        }
    }
}
