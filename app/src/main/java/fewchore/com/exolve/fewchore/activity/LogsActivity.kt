package fewchore.com.exolve.fewchore.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import fewchore.com.exolve.fewchore.R
import android.provider.ContactsContract
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import fewchore.com.exolve.fewchore.invest.WhoActivity
import fewchore.com.exolve.fewchore.model.CallLogModel
import kotlin.collections.ArrayList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.AppUtils.Companion.PREF_CONTACT
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.helper.SharedPreferenceUtil
import fewchore.com.exolve.fewchore.model.LogsModel
import fewchore.com.exolve.fewchore.model.PhoneModel
import kotlinx.android.synthetic.main.activity_logs.*
import org.json.JSONArray
import org.json.JSONObject


class LogsActivity : AppCompatActivity() {
    private var callLog: String? = null
    /*private var textCall : TextView ?= null
    private var textSMS : TextView ?= null*/
    private var callLogModel: CallLogModel? = null
    private var callModelList: List<CallLogModel>? = null
    private var smsList: List<String>? = null
    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    private var sharePrefernce: SharedPreferenceUtil? = null
    var aa = ArrayList<String>()
    private var lstNames: ListView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)
        sharePrefernce = SharedPreferenceUtil
     if (AppUtils.getMyContact(this) =="1"){
            startActivity(Intent(this, WhoActivity::class.java))
            Log.i("LOGS", "Already saved")
        }else{
            try {

                showContacts()
            }catch (e: Exception){
                Log.i("TAG LOGGS", e.message.toString())
            }
        }

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                //showContacts()
                getNumber(this.contentResolver)
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showContacts() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS)
        } else {
            getNumber(this.contentResolver)
            /* // Android version is lesser than 6.0 or the permission is already granted.
             val contacts = getContactNames()
             val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts)
             lstNames!!.adapter = adapter*/
        }
    }

    private fun getNumber(cr: ContentResolver)
    {
        val contactList = ArrayList<PhoneModel>()
        val phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null)
        while (phones.moveToNext())
        {
            val name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            System.out.println("..................$phoneNumber")
            aa.add("Number: $phoneNumber .Name; $name")
        }
        phones.close()// close cursor
        for (value in aa ){
            val first= PhoneModel()

            val phone1 = value.split(":",".")
            first.phone = phone1[1]
            val name = value.split(";")
            first.name = name[1]
            if (!contactList.contains(first))
                contactList.add(first)
        }
        DumbAsync(contactList).execute()

        /* db!!.insertContact(contactList)
        val adapter =  ArrayAdapter<PhoneModel>(this,
                android.R.layout.simple_list_item_1,contactList)
        list_item1.adapter = adapter
        //display contact numbers in the list*/
    }
    @SuppressLint("StaticFieldLeak")
    inner class DumbAsync(private var contactList: ArrayList<PhoneModel>) :AsyncTask<Void, Int,String>() {
        override fun doInBackground(vararg p0: Void?): String? {

            val jsonArray = JSONArray()//Gson().toJson(contactList)
            val map = JSONObject()

            for (i in contactList) {
                val contactObject = JSONObject()
                contactObject.put("name", i.name)
                contactObject.put("phone", i.phone)
                jsonArray.put(contactObject)
            }

            map.put("contact", jsonArray)
            map.put("user_id", AppUtils.getMyUserId(this@LogsActivity))
            val url = Const.FEWCHORE_URL + "uploadlog"

            return HttpUtility.postJson(url, map)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                val gson = Gson()
                try {
                    val type = object : TypeToken<LogsModel>() {}.type
                    val userModel = gson.fromJson<LogsModel>(result, type)
                    if (userModel.status) {
                        sharePrefernce!!.save(this@LogsActivity, "1", PREF_CONTACT)
                        Toast.makeText(this@LogsActivity, result, Toast.LENGTH_LONG).show()
                        val intent = Intent(this@LogsActivity, WhoActivity::class.java)
                        startActivity(intent)
                        this@LogsActivity.finish()
                    } else
                        Toast.makeText(this@LogsActivity, "Please close the app and open it again", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Log.e("LOGS", e.message)
                }

            }

        }
    }
}


