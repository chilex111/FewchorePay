package fewchore.com.exolve.fewchore.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.helper.SharedPreferenceUtil
import fewchore.com.exolve.fewchore.model.ResponseStringModel
import fewchore.com.exolve.fewchore.model.SignUpModel
import fewchore.com.exolve.fewchore.views.CircleImageView
import kotlinx.android.synthetic.main.dialog_withdraw_investment.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.IllegalStateException


class SignUpActivity : AppCompatActivity() {
    private var TAKE_PHOTO_REQUEST = 14
    private var editEmail: EditText? = null
    private var editOfficeAddress: EditText? = null
    private var editHomeAddress: EditText? = null
    private var editPlaceOfWork: EditText? = null
    private var editPhone: EditText? = null
    private var editPassword: EditText? = null
    private var editBVN: EditText? = null
    private var editFirstName: EditText? = null
    private var editLastName: EditText? = null
    private var checkBox: CheckBox ?= null
    private var appUtil: AppUtils?= null
    private var relativeProgress: RelativeLayout ?= null
    private var profile: CircleImageView? = null
    private var photo1: String? = null
    private var emailText: String ?= null
    private var officeAddressText: String ?= null
    private var officeText: String ?= null
    private var homeAddressText: String ?= null
    private var passwordText: String ?= null
    private var phoneText: String ?= null
    private var firstNameText: String ?= null
    private var lastNameText: String ?= null
    private var enteredBVN : String ?= null
    private var progressBVN: ProgressBar ?= null
    private var bvnStatus: Boolean ? = null
    private var sharedPreference: SharedPreferenceUtil?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        sharedPreference = SharedPreferenceUtil

        val signUp = findViewById<Button>(R.id.buttonCreate)


        relativeProgress = findViewById(R.id.relativeProgress)
        editEmail = findViewById(R.id.editEmail)
        editOfficeAddress = findViewById(R.id.editOfficialAddress)
        editHomeAddress = findViewById(R.id.editHomeAddress)
        editPlaceOfWork = findViewById(R.id.editPlaceOfWork)
        editPassword = findViewById(R.id.editPassword)
        editPhone = findViewById(R.id.editPhone)
        editFirstName = findViewById(R.id.editFirstName)
        editLastName = findViewById(R.id.editLastName)
        editBVN = findViewById(R.id.editBVN)
        progressBVN = findViewById(R.id.progressBVN)

        profile = findViewById(R.id.imageAgentPhoto)
        checkBox = findViewById(R.id.checkbox)
        appUtil = AppUtils(this)

        editEmail!!.setOnFocusChangeListener { _, b ->
            if (b)
                editEmail!!.error ="Only valid official email will be attended to"
            else
                Log.i("TAG", "not in focus")
        }

        findViewById<ImageButton>(R.id.photoButton).setOnClickListener {
            //showDialog()
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA),
                        TAKE_PHOTO_REQUEST)
            }else{
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)
            }



        }
        signUp.setOnClickListener {
            if (signUp())
                relativeProgress!!.visibility= View.VISIBLE
        }

        findViewById<TextView>(R.id.buttonLogin).setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }
        editBVN!!.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length ==11){
                    enteredBVN = p0.toString()
                    progressBVN!!.visibility = View.VISIBLE
                    VerifyBVNAsync(enteredBVN!!, progressBVN!!, editBVN!!).execute()

                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        findViewById<TextView>(R.id.terms).setOnClickListener {
            termsBox()
        }
    }

    private fun termsBox() {
        val dialog = Dialog(this, R.style.Dialog)
        dialog.setContentView(R.layout.custom_terms)
        dialog.show()
        dialog.findViewById<ImageButton>(R.id.buttonClose).setOnClickListener {
            dialog.close
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            TAKE_PHOTO_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST)

                } else {
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.CAMERA),
                            TAKE_PHOTO_REQUEST) }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            val profileImg = data.extras.get("data") as Bitmap

            profile!!.setImageBitmap(profileImg)

            val bao = ByteArrayOutputStream()
            profileImg.compress(Bitmap.CompressFormat.PNG, 90, bao)
            val ba = bao.toByteArray()

            photo1 = Base64.encodeToString(ba, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun signUp(): Boolean{
        emailText = editEmail!!.text.toString()
        passwordText = editPassword!!.text.toString()
        phoneText = editPhone!!.text.toString()
        firstNameText = editFirstName!!.text.toString()
        lastNameText = editLastName!!.text.toString()
        officeAddressText = editOfficeAddress!!.text.toString()
        homeAddressText = editHomeAddress!!.text.toString()
        officeText = editPlaceOfWork!!.text.toString()

        val bvnText = editBVN!!.text.toString()

        if (officeAddressText!!.isEmpty()) {
            editOfficeAddress!!.error = "This field is required"
            return false
        }
        if (officeText!!.isEmpty()) {
            editPlaceOfWork!!.error = "This field is required"
            return false
        }
        if (homeAddressText!!.isEmpty()) {
            editHomeAddress!!.error = "This field is required"
            return false
        }
        if (!checkBox!!.isChecked){
            appUtil!!.showAlert("Please check the terms and condition to proceed")
            return false
        }
        if (emailText!!.isEmpty()) {
            editEmail!!.error = "This field is required"
            return false
        }
        if (lastNameText!!.isEmpty()) {
            editLastName!!.error = "This field is required"
            return false
        }
        if (passwordText!!.isEmpty()) {
            editPassword!!.error = "This field is required"
            return false
        }
        if (phoneText!!.isEmpty()) {
            editPhone!!.error = "This field is required"
            return false
        }
        if (firstNameText!!.isEmpty()) {
            editFirstName!!.error = "This field is required"
            return false
        }
        return if (photo1.isNullOrEmpty()){
            appUtil!!.showAlert("Please take a photo for your profile")
            false
        }
        else{
            SignUpAsync(firstNameText!!,passwordText!!,emailText!!,phoneText!!,1, lastNameText!!,bvnText).execute()

            true
        }
    }


    @SuppressLint("StaticFieldLeak")
    inner class VerifyBVNAsync(private var bvnText: String, private var progress: ProgressBar, private var bvn: EditText):
            AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            val url = Const.FEWCHORE_URL +"bvn"
            val userId = AppUtils.getMyUserId(this@SignUpActivity)
            val map = HashMap<String, Any?>()
            map["bvn"] = bvnText
            map["user_id"] = userId
            return HttpUtility.sendPostRequest(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                progress.visibility = View.GONE
                val gson = Gson()
                try{
                    val type = object : TypeToken<ResponseStringModel>() {}.type
                    val userModel = gson.fromJson<ResponseStringModel>(result, type)
                    if (userModel.status =="true"){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            bvn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,R.drawable.ic_check, 0)
                        }
                        bvnStatus= true
                        sharedPreference!!.save(this@SignUpActivity,"verified", AppUtils.PREF_BVN)
                    }
                    else{
                        //bvn.setCompoundDrawables(null,null,activity!!.resources.getDrawable(R.drawable.ic_close),null)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            bvn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,R.drawable.ic_close, 0)
                        }
                        bvn.error =  "Your BVN could not be verified. Please try again"
                        bvnStatus= false
                    }

                }catch (e: Exception){
                    if (e is IllegalStateException)
                        Toast.makeText(this@SignUpActivity, "Please check your network connection", Toast.LENGTH_LONG).show()
                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)

                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class BVNAsync(private var bvnText: String, private var userId: String):
            AsyncTask<Void, Int, String>() {
        override fun doInBackground(vararg p0: Void?): String {
            val url = Const.FEWCHORE_URL +"bvn"
            val map = HashMap<String, Any?>()
            map["bvn"] = bvnText
            map["user_id"] = userId
            return HttpUtility.sendPostRequest(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()){
                val gson = Gson()
                try{
                    val type = object : TypeToken<ResponseStringModel>() {}.type
                    val userModel = gson.fromJson<ResponseStringModel>(result, type)
                    if (userModel.status =="true"){
                        /*     bvnStatus= true
                             sharedPreference!!.save(this@SignUpActivity,"verified", AppUtils.PREF_BVN)
     */
                        val param  = "A verification code has been sent via SMS/Email. Please check spam box for your verification code"

                        val localBuilder = AlertDialog.Builder(this@SignUpActivity)
                        localBuilder.setMessage(param)
                        localBuilder.setNeutralButton(R.string.ok) { _, _ ->
                            val intent = Intent(this@SignUpActivity, VerifyPhoneActivity::class.java)
                            intent.putExtra("PHONE", phoneText)
                            this@SignUpActivity.startActivity(intent)
                        }
                        localBuilder.create().show()
                    }
                    else{
                        //bvn.setCompoundDrawables(null,null,activity!!.resources.getDrawable(R.drawable.ic_close),null)
                        bvnStatus= false
                    }

                }catch (e: Exception){
                    if (e is IllegalStateException)
                        Toast.makeText(this@SignUpActivity, "Please check your network connection", Toast.LENGTH_LONG).show()
                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)

                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class SignUpAsync
    internal constructor(private val name: String, private val password: String,
                         private val email: String, private val phoneText: String,
                         private val policy: Int, private val lastNameText: String, private val bvnText: String)
        : AsyncTask<Void, Int, String>() {

        override fun doInBackground(vararg voids: Void): String? {
            return try {
                val map = HashMap<String, Any?>()
                map["user_firstname"] = name
                map["user_lastname"] = lastNameText
                map["user_email"] = email
                map["user_phone"] = phoneText
                map["user_password"] = password
                map["user_policy"] = policy
                map["user_image"] = photo1
                map["user_address"] = homeAddressText
                map["place_of_work"] = officeText
                map["place_of_work_address"] = officeAddressText
                HttpUtility.sendPostRequest(Const.FEWCHORE_URL + "signup", map)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }

        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            if (s != null && !s.isEmpty()) {
                this@SignUpActivity.relativeProgress!!.visibility = View.GONE
                try {
                    val responseType = object : TypeToken<SignUpModel>() {}.type
                    val response = Gson().fromJson<SignUpModel>(s, responseType)
                    if (response.status =="true") {

                        BVNAsync(bvnText,response.userId).execute()
                        return
                    }else{
                        appUtil!!.showAlert(response.msg!!)
                    }
                } catch (e: Exception) {
                    if (e is IllegalStateException) {
                        Log.i("LOGIN", "Please check your network connection")
                    }
                    Log.i("ERROR MESSAGE", e.javaClass.simpleName)

                }

            }
        }
    }
}
