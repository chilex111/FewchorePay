package fewchore.com.exolve.fewchore.apis

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.DESedeEncryption
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.listener.StatusListener
import fewchore.com.exolve.fewchore.model.BooleanModel

@SuppressLint("StaticFieldLeak")
class SaveLookUpAsync(private var encryptCard: String, private var identifier: String,
                      private var chargeToken: String?,
                     private var secretKey: String, private var context: Context):AsyncTask<Void, Int, String>() {

    companion object {
        var listener : StatusListener ?= null

    }
    override fun doInBackground(vararg p0: Void?): String {


       /* val ency = DESedeEncryption()
        val value  =ency.encrypt(chargeToken,secretKey)*/
        val map = HashMap<String, Any?>()
        map["identifier"] = identifier
        map["encrypted_value"]= encryptCard
        map["charge_token"] = chargeToken
        val userId = AppUtils.getMyUserId(context)

        val url = Const.FEWCHORE_URL+"addlookupcards/"+userId
        return HttpUtility.sendPostRequest(url, map)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            try{
                val type = object : TypeToken<BooleanModel>() {}.type
                val userModel = gson.fromJson<BooleanModel>(result, type)
                if (userModel != null) {
                    listener!!.statusListener(userModel.msg!!, userModel.status, null)
                }
            }catch (e: Exception){
                Log.i("TAGGG", e.message.toString())
            }
        }
    }

}
