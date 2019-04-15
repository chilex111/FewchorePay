package fewchore.com.exolve.fewchore.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.helper.PinFormatter
import fewchore.com.exolve.fewchore.listener.PinTextListener
import fewchore.com.exolve.fewchore.model.SignUpModel

class VerifyPhoneActivity : AppCompatActivity(), PinTextListener {


    private var textPhone: TextView? = null
    private var appUtil: AppUtils? = null
    private var pinChar1: EditText ?= null
    private var pinChar2: EditText ?= null
    private var pinChar3: EditText ?= null
    private var pinChar4: EditText ?= null
    private var pinChar5: EditText ?= null
    private var pinChar6: EditText ?= null
    private var code: String ?= null
    private var relativeProgress : RelativeLayout ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone)



        val bundle = intent.extras
        val phoneText = bundle.getString("PHONE")

        relativeProgress = findViewById(R.id.relativeProgress)

        pinChar1 = findViewById(R.id.pinChar1)
        pinChar2 = findViewById(R.id.pinChar2)
        pinChar3 = findViewById(R.id.pinChar3)
        pinChar4 = findViewById(R.id.pinChar4)
        pinChar5 = findViewById(R.id.pinChar5)
        pinChar6 = findViewById(R.id.pinChar6)
        textPhone = findViewById(R.id.textViewPhone)

        textPhone!!.text = phoneText
        appUtil = AppUtils(this)

        pinChar1!!.addTextChangedListener(PinFormatter(pinChar2, this))
        pinChar2!!.addTextChangedListener(PinFormatter(pinChar3, this))
        pinChar3!!.addTextChangedListener(PinFormatter(pinChar4, this))
        pinChar4!!.addTextChangedListener(PinFormatter(pinChar5, this))
        pinChar5!!.addTextChangedListener(PinFormatter(pinChar6, this))
        pinChar6!!.addTextChangedListener(PinFormatter(pinChar1, this))
    }

    override fun onTextChanged() {

        val str1 = pinChar1!!.text.toString()
        val str2 = pinChar2!!.text.toString()
        val str3 = pinChar3!!.text.toString()
        val str4 = pinChar4!!.text.toString()
        val str5 = pinChar5!!.text.toString()
        val str6 = pinChar6!!.text.toString()

        if (str1.isNotEmpty() && str2.isNotEmpty() && str3.isNotEmpty() && str4.isNotEmpty() && str5.isNotEmpty() && str6.isNotEmpty()) {
            code = str1 + str2 + str3 + str4 + str5 + str6

            val phone = textPhone!!.text.toString()
            if (code.isNullOrEmpty()) {
                pinChar1!!.error = "This field is required"
            } else {
                relativeProgress!!.visibility = View.VISIBLE
                VerifyPhoneAsync(code!!).execute()
            }
        }
    }



    @SuppressLint("StaticFieldLeak")
    private inner  class VerifyPhoneAsync(private val code: String) : AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            val url = Const.FEWCHORE_URL + "confirmphone"
            val map = HashMap<String, Any?>()
            map["hash_value"] = code
            return HttpUtility.sendPostRequest(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                relativeProgress!!.visibility = View.GONE
                try{
                    val responseType = object : TypeToken<SignUpModel>() {}.type
                    val response = Gson().fromJson<SignUpModel>(result, responseType)
                    if (response.status =="true") {
                        val localBuilder = AlertDialog.Builder(this@VerifyPhoneActivity)
                        localBuilder.setMessage(response.msg)
                        localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                            startActivity(Intent(this@VerifyPhoneActivity, LoginActivity::class.java))
                        }
                        localBuilder.create().show()
                    }else{
                        appUtil!!.showAlert(response.msg!!)
                    }

                }catch (e: Exception){
                    if (e is IllegalStateException)
                        Toast.makeText(this@VerifyPhoneActivity, "Please check your network connection", Toast.LENGTH_LONG).show()

                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)

                }
            }
            else{
                appUtil!!.showAlert("Our server is experiencing some Technical Difficulty." +
                        "Please try again later.")
            }
        }
    }

}
