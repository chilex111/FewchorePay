package fewchore.com.exolve.fewchore.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.helper.*
import fewchore.com.exolve.fewchore.helper.AppUtils.Companion.PREF_IS_LOGGEDIN
import fewchore.com.exolve.fewchore.helper.AppUtils.Companion.PREF_PIN
import fewchore.com.exolve.fewchore.listener.PinTextListener
import fewchore.com.exolve.fewchore.model.LoginModel
import java.util.*

class LoginActivity : AppCompatActivity(), PinTextListener,View.OnClickListener  {

    override fun onClick(view: View?) {
        val currentFocus = dialog!!.currentFocus
        if (currentFocus != null) {
            if (currentFocus is EditText) {
                if (view is Button) {
                    currentFocus.setText(view.text.toString())
                }
            }
        }
    }

    private var editEmail: EditText? = null
    private var editPassword: EditText? = null
    private var relativeProgress: RelativeLayout?= null
    private var appUtils: AppUtils ? = null
    private var sharedPreferenceUtil : SharedPreferenceUtil ?= null
    private var dialog : Dialog ?= null
    private var pinChar1: EditText? = null
    private var pinChar2:EditText? = null
    private var pinChar3:EditText? = null
    private var pinChar4:EditText? = null
    private var emailText : String ?= null
    private var passwordText : String ?= null
    private var overlay: LinearLayout ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        appUtils = AppUtils(this)


        sharedPreferenceUtil = SharedPreferenceUtil
        relativeProgress = findViewById(R.id.relativeProgress)
        overlay = findViewById(R.id.overlay)
        val login = findViewById<Button>(R.id.buttonLogin)
        editEmail = findViewById(R.id.editEmail)
        editPassword = findViewById(R.id.editPassword)


        findViewById<Button>(R.id.buttonMPin).setOnClickListener {
            startActivity(Intent(this@LoginActivity, VerifyPinActivity::class.java))

        }

        val myPassword = AppUtils.getMyPassword(this)
        val pin = AppUtils.getMyPin(this)
        // Log.i("PASSWORD", "" + myPassword)
        if (!myPassword.isNullOrEmpty()) {

            if (pin != null && !pin.isEmpty()) {
                val isLoggedIn = sharedPreferenceUtil!!.getValue(this, PREF_IS_LOGGEDIN)
                if (isLoggedIn != null && isLoggedIn == "1") {
                    pinDialog()
                }
            }

        }
        val resetPassword =  findViewById<TextView>(R.id.buttonReset)
        resetPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))

        }
        findViewById<TextView>(R.id.buttonSignUp).setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))

        }

        val buttonVerify = findViewById<Button>(R.id.buttonVerify)
        buttonVerify.setOnClickListener {
            verifyPhone()
        }

        login.setOnClickListener {
            if (login())
                relativeProgress!!.visibility = View.VISIBLE

        }


    }

    private fun verifyPhone() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.template_phone)

        val phone = dialog.findViewById(R.id.editPhone)as EditText
        val buttonVerfy = dialog.findViewById(R.id.buttonVerify)as Button
        buttonVerfy.setOnClickListener {
            val phoneText =  phone.text.toString()
            if (phoneText.isEmpty()){
                phone.error = "Please enter a valid Phone Number"
            }else {
                val intent = Intent(this@LoginActivity, VerifyPhoneActivity::class.java)
                intent.putExtra("PHONE", phoneText)
                this@LoginActivity.startActivity(intent)
            } }
        dialog.show()
    }


    private fun pinDialog() {

        dialog = Dialog(this, R.style.Dialog)
        dialog!!.setContentView(R.layout.dialog_pin)
        dialog!!.setTitle("Password request...")
        dialog!!.setCanceledOnTouchOutside(false)

        val lp = dialog!!.window!!.attributes
        lp.dimAmount = 0.98f
        dialog!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)


        // set the custom dialog components - text, image and button


        pinChar1 = dialog!!.findViewById(R.id.pinChar1)
        pinChar2 = dialog!!.findViewById(R.id.pinChar2)
        pinChar3 = dialog!!.findViewById(R.id.pinChar3)
        pinChar4 = dialog!!.findViewById(R.id.pinChar4)

        val button1 = dialog!!.findViewById<Button>(R.id.button1)
        val button2 = dialog!!.findViewById<Button>(R.id.button2)
        val button3 = dialog!!.findViewById<Button>(R.id.button3)
        val button4 = dialog!!.findViewById<Button>(R.id.button4)
        val button5 = dialog!!.findViewById<Button>(R.id.button5)
        val button6 = dialog!!.findViewById<Button>(R.id.button6)
        val button7 = dialog!!.findViewById<Button>(R.id.button7)
        val button8 = dialog!!.findViewById<Button>(R.id.button8)
        val button9 = dialog!!.findViewById<Button>(R.id.button9)
        val button0 = dialog!!.findViewById<Button>(R.id.button0)

        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        button0.setOnClickListener(this)

        pinChar1!!.addTextChangedListener(PinFormatter(pinChar2, this@LoginActivity))
        pinChar2!!.addTextChangedListener(PinFormatter(pinChar3, this@LoginActivity))
        pinChar3!!.addTextChangedListener(PinFormatter(pinChar4, this@LoginActivity))
        pinChar4!!.addTextChangedListener(PinFormatter(pinChar1, this@LoginActivity))

        val resetButton = dialog!!.findViewById<Button>(R.id.buttonPinReset)
        resetButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ResetPinActivity::class.java))
            if (dialog!!.isShowing)
                dialog!!.dismiss()
        }


        dialog!!.setOnCancelListener {
            startActivity(Intent(this@LoginActivity, LandingActivity::class.java))
            if (dialog!!.isShowing)
                dialog!!.dismiss()
        }
        if (dialog!= null)
            dialog!!.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
    override fun onTextChanged() {
        val str1 = pinChar1!!.text.toString()
        val str2 = pinChar2!!.text.toString()
        val str3 = pinChar3!!.text.toString()
        val str4 = pinChar4!!.text.toString()

        if (str1.isNotEmpty() && str2.isNotEmpty() && str3.isNotEmpty() && str4.isNotEmpty()) {
            val pin = str1 + str2 + str3 + str4

            val sharePass = sharedPreferenceUtil!!.getValue(this@LoginActivity, PREF_PIN)

            if (pin == sharePass) {
                // dialog.dismiss();
                overlay!!.visibility = View.VISIBLE
                startActivity(Intent(this, HomeActivity::class.java))
                clearPin()

            } else {
                Toast.makeText(this@LoginActivity, "The PIN entered is not correct", Toast.LENGTH_LONG).show()
                clearPin()
            }
        }
    }

    private fun clearPin() {
        pinChar1!!.setText("")
        pinChar2!!.setText("")
        pinChar3!!.setText("")
        pinChar4!!.setText("")

        pinChar1!!.requestFocus()
    }


    private fun login():Boolean {
        emailText = editEmail!!.text.toString()
        passwordText = editPassword!!.text.toString()
        if (emailText!!.isEmpty()) {
            editEmail!!.error = "This field is required"
            return false
        }
        return if (passwordText!!.isEmpty()) {
            editPassword!!.error = "This field is required"
            false
        }
        else {
            LoginAsync(emailText!!, passwordText!!, this).execute()
            true

        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class LoginAsync(private val emailText: String, private val passwordText: String, private val context: Context) : AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String? {
            val url = Const.FEWCHORE_URL+"login"
            try {
                val map = HashMap<String, Any?>()
                map["user_email"] = emailText
                map["user_password"] = passwordText
                return HttpUtility.sendPostRequest(url, map)
            }catch (e:Exception){
                Log.i("TAG_LOGIN", e.message)
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                relativeProgress!!.visibility = View.GONE
                val gson = Gson()
                try{
                    val type = object : TypeToken<LoginModel>() {}.type
                    val userModel = gson.fromJson<LoginModel>(result, type)
                    if (userModel != null) {
                        if (userModel.status == "success") {
                            val loginModel = userModel.userDetails
                            val email = loginModel!!.userEmail
                            val firstname = loginModel.userFirstname
                            val id = loginModel.userId
                            val phone = loginModel.userPhone
                            val lastname = loginModel.userLastname
                            val status = loginModel.userLoanstatus
                            val profile = userModel.userImage

                            val sharedPreferenceUtil = SharedPreferenceUtil

                            sharedPreferenceUtil.save(context, firstname!!, AppUtils.PREF_FIRSTNAME)
                            sharedPreferenceUtil.save(context, email!!, AppUtils.PREF_EMAIL)
                            sharedPreferenceUtil.save(context, id!!, AppUtils.PREF_USERID)
                            sharedPreferenceUtil.save(context, status!!, AppUtils.PREF_LOAN_STATUS)
                            sharedPreferenceUtil.save(context, passwordText, AppUtils.PREF_PASSWORD)
                            sharedPreferenceUtil.save(context, "1", AppUtils.PREF_IS_LOGGEDIN)
                            sharedPreferenceUtil.save(context, phone!!, AppUtils.PREF_PHONENUMBER)
                            sharedPreferenceUtil.save(context, lastname!!, AppUtils.PREF_LASTNAME)
                            sharedPreferenceUtil.save(context, profile!!, AppUtils.PREF_PROFILE)

                            pinSetDialog()
                        } else {
                            val localBuilder = AlertDialog.Builder(this@LoginActivity)
                            localBuilder.setMessage(userModel.msg)
                            localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                                verifyPhone()
                            }
                            localBuilder.create().show()
                        }
                    }
                }catch (e: Exception){
                //    Toast.makeText(this@LoginActivity, "Please check your network connection", Toast.LENGTH_LONG).show()
                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)
                }
            }
        }
    }

    private fun pinSetDialog() {
        dialog = Dialog(this@LoginActivity, R.style.Dialog)
        dialog!!.setContentView(R.layout.template_pin_set)
        dialog!!.setTitle("Set Up PIN...")
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.findViewById<Button>(R.id.buttonVerifyPin).setOnClickListener {
            startActivity(Intent(this@LoginActivity, VerifyPinActivity::class.java))

        }
        dialog!!.findViewById<Button>(R.id.buttonSetupPin).setOnClickListener {
            startActivity(Intent(this@LoginActivity, SetUpPinActivity::class.java))
        }
        dialog!!.show()

        return
    }
}

