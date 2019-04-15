package fewchore.com.exolve.fewchore.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.model.ResetPassModel
import fewchore.com.exolve.fewchore.model.ResponseStringModel
import fewchore.com.exolve.fewchore.views.PoppinsButton
import fewchore.com.exolve.fewchore.views.PoppinsEditText
import java.lang.IllegalStateException
import java.util.*

class ForgotPasswordActivity : AppCompatActivity() {

    private var editPassword : EditText?= null
    private var editConPassword : EditText?= null
    private var editCode : EditText?= null
    private var resetButton : PoppinsButton?= null
    private var relativeProgress : RelativeLayout?= null
    private var appUtils: AppUtils?= null
    private var valueEntered : String ?= null
    private var dialog : Dialog?= null
    private var passText : String ?= null
    private var codeTExt : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        appUtils = AppUtils(this)


        relativeProgress = findViewById(R.id.relativeProgress)
        editCode = findViewById(R.id.editCode)
        editPassword = findViewById(R.id.editPassword)
        editConPassword = findViewById(R.id.editConfirmPassword)
        resetButton = findViewById(R.id.buttonReset)

        forgotPassword()

        resetButton!!.setOnClickListener {
             codeTExt = editCode!!.text.toString()
             passText = editPassword!!.text.toString()
            val conPassText = editConPassword!!.text.toString()

            if (codeTExt!!.isEmpty()) {
                editCode!!.error = "This field is required"

            }
            if (passText!!.isEmpty()){
                editPassword!!.error = "This field is required"
            }
            if(conPassText.isEmpty()){
                editConPassword!!.error = "This field is required"
            }
            if (passText != conPassText){
                editConPassword!!.error = "Password does not match"
            }
            else{
                relativeProgress!!.visibility =View.VISIBLE
                ResetAsync (codeTExt!!, passText!!).execute()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }



    private fun forgotPassword() {
         dialog = Dialog(this)
        dialog!!.setContentView(R.layout.dialog_reset_password)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.onBackPressed()
        val phone = dialog!!.findViewById<PoppinsEditText>(R.id.editPhone)
        val buttonSubmit =dialog!!.findViewById<PoppinsButton>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
             valueEntered = phone.text.toString()
            if (valueEntered.isNullOrEmpty()){
                phone.error = "This field is required"
            }else{
                relativeProgress!!.visibility= View.VISIBLE
                ForgotAsync(valueEntered!!).execute()

            }
        }
        dialog!!.show()
    }


  @SuppressLint("StaticFieldLeak")
  inner  class ForgotAsync(private val valueEntered: String): AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            val url = Const.FEWCHORE_URL + "forgotpassword"
            val map = HashMap<String, Any?>()
            map["user_email"] = valueEntered


            return HttpUtility.sendPostRequest(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                relativeProgress!!.visibility = View.GONE
                val gson = Gson()
                try{
                val type = object : TypeToken<ResetPassModel>() {}.type
                val resetPassModel = gson.fromJson<ResetPassModel>(result, type)
                if (resetPassModel.status!!){
                    dialog!!.dismiss()
                    appUtils!!.showAlert("Check your email account for your verification code to reset your password. Please check your spam box for your verification code")
                }
                else{
                    appUtils!!.showAlert(resetPassModel.msg!!)

                }
            }catch (e: Exception) {
                    if (e is IllegalStateException) {
                        Toast.makeText(this@ForgotPasswordActivity, "Please check your network connection", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    inner class ResetAsync(private val codeTExt: String, private val passText:
    String): AsyncTask<Void, Int, String>() {


        override fun doInBackground(vararg p0: Void?): String {
            val url = Const.FEWCHORE_URL + "resetpassword"
            val map = HashMap<String, Any?>()
            map["new_password"] = passText
            map["reset_code"] = codeTExt

            return HttpUtility.sendPostRequest(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                relativeProgress!!.visibility = View.GONE
                val gson = Gson()
                try {
                val type = object : TypeToken<ResponseStringModel>() {}.type
                val responseModel = gson.fromJson<ResponseStringModel>(result, type)
                if (responseModel.status =="true"){
                    appUtils!!.showAlert(responseModel.msg!!)
                    startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                }else{
                    appUtils!!.showAlert(responseModel.msg!!)
                }
            }catch (e: Exception){
                    if (e is IllegalStateException) {
                        Log.i("LOGIN", "Please check your network connection")
                    }
                }
            }
        }

    }
}