package fewchore.com.exolve.fewchore.apis

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.listener.EncryptionListener
import fewchore.com.exolve.fewchore.model.EncryptCardModel

@SuppressLint("StaticFieldLeak")
class EncryptCard (private var noText: String, private var cvvText: String, private var expText: String,
                   private var pinText: String,private var Id: String,var context: Context):AsyncTask<Void, Int,String>() {

    companion object {
        var encryptListener: EncryptionListener ?= null
    }

    override fun doInBackground(vararg p0: Void?): String? {
        //val userId = AppUtils.getMyUserId(context)

        val map = HashMap<String, Any?>()

        map["card_num"] = noText
        map["cvv"] = cvvText
        map["exp_date"] = expText
        map["card_pin"] = pinText
        try {
            val url = Const.FEWCHORE_URL+"encryptcard"
            return HttpUtility.sendPostRequest(url, map)
        }catch (e: Exception){
            Log.i("ENCRYPT", e.message)
        }
        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            try {
                val type = object : TypeToken<EncryptCardModel>() {}.type
                val responseModel = gson.fromJson<EncryptCardModel>(result, type)
                if (responseModel.status!!)
                if (!responseModel.encryptedcard.isNullOrEmpty())
                    encryptListener!!.encryptListener(responseModel.encryptedcard!!, "true", Id)
                // TransactionAsync(responseModel.msg,id).execute()


            }catch (e:Exception){
                //Log.e("ENCRYPT", e.message)
            }
        }
    }

}
