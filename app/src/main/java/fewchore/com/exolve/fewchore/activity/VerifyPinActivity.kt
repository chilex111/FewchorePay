package fewchore.com.exolve.fewchore.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.gson.Gson
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.invest.WhoActivity
import fewchore.com.exolve.fewchore.helper.*
import fewchore.com.exolve.fewchore.helper.AppUtils.Companion.PREF_PIN
import fewchore.com.exolve.fewchore.listener.PinTextListener
import fewchore.com.exolve.fewchore.model.ResponseStringModel
import java.io.IOException


@SuppressLint("Registered")
class VerifyPinActivity : AppCompatActivity(), PinTextListener {
    private var appUtils: AppUtils? = null
    private var sharedPreference: SharedPreferenceUtil? = null
    private var pinChar1: EditText ?= null
    private var pinChar2: EditText ?= null
    private var pinChar3: EditText ?= null
    private var pinChar4: EditText ?= null
    private var code: String ?= null
    private var relativeProgress: RelativeLayout ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_pin)


        sharedPreference = SharedPreferenceUtil

        findViewById<Button>(R.id.buttonVerifyPin)


        pinChar1 = findViewById(R.id.pinChar1)
        pinChar2 = findViewById(R.id.pinChar2)
        pinChar3 = findViewById(R.id.pinChar3)
        pinChar4 = findViewById(R.id.pinChar4)
        relativeProgress = findViewById(R.id.relativeProgress)

        /*   buttonVerify.setOnClickListener({
               val pinText = editTextPin.text.toString()
               if (TextUtils.isEmpty(pinText)) {
                   editTextPin.error = "This field is required"
               } else {
                   VerifyPinAsync(pinText).execute()
               }
           })*/
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


            if (code.isNullOrEmpty()) {
                pinChar1!!.error = "This field is required"
            } else {
                relativeProgress!!.visibility = View.VISIBLE
                VerifyPinAsync(code!!).execute()
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class VerifyPinAsync (private var pinText: String) : AsyncTask<Void, Int, String>() {
        internal var userId = AppUtils.getMyUserId(this@VerifyPinActivity)

        override fun onPreExecute() {
            super.onPreExecute()
            relativeProgress!!.visibility = View.VISIBLE
            //   Toast.makeText(this@VerifyPinActivity, userId, Toast.LENGTH_SHORT).show()
        }

        override fun doInBackground(vararg voids: Void): String? {
            val url = Const.FEWCHORE_URL + "verifypin"
            val userId = AppUtils.getMyUserId(this@VerifyPinActivity)
            try {
                val map = HashMap<String, Any?>()
                map["user_pin"] = pinText
                map["user_id"] = userId
                return HttpUtility.sendPostRequest(url, map)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(s: String?) {

            super.onPostExecute(s)
            if (s != null) {
                appUtils = AppUtils(this@VerifyPinActivity)
                try {
                    val gson = Gson()
                    val responseMessage = gson.fromJson<ResponseStringModel>(s, ResponseStringModel::class.java)

                    if (responseMessage.status =="true") {
                        relativeProgress!!.visibility = View.GONE

                        sharedPreference!!.save(this@VerifyPinActivity, pinText, PREF_PIN)
                        startActivity(Intent(this@VerifyPinActivity, WhoActivity::class.java))

                    } else {
                        val msg = responseMessage.msg
                        val builder = AlertDialog.Builder(this@VerifyPinActivity)
                        builder.setMessage(msg)
                        builder.setNeutralButton(R.string.ok) { _, _ ->
                            relativeProgress!!.visibility = View.GONE }

                        builder.create().show()
                    }
                } catch (e: Exception) {
                    if (e is IllegalStateException)
                        Toast.makeText(this@VerifyPinActivity, "Please check your network connection", Toast.LENGTH_LONG).show()

                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)
                }

            }
        }
    }
}
