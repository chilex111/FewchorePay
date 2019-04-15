package fewchore.com.exolve.fewchore.apis

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.listener.CardLoanListener
import fewchore.com.exolve.fewchore.model.AccessCodeModel
import java.lang.IllegalStateException

@SuppressLint("StaticFieldLeak")
class AddCardAsync( private val context: Context) : AsyncTask<Void, Int, String>() {
    companion object {
        var cardListener: CardLoanListener ?= null
    }
    override fun doInBackground(vararg p0: Void?): String? {
        val url = Const.FEWCHORE_URL+"addcard"
        val userId = AppUtils.getMyUserId(context)
        val map = HashMap<String, Any?>()

        map["card_user"] = userId!!
        try {
            return HttpUtility.sendPostRequest(url,map)
        }catch (e:Exception){}
        return null

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null){
            val gson = Gson()
            try{
                val codeModel = gson.fromJson<AccessCodeModel>(result, AccessCodeModel::class.java)
                if (codeModel != null){
                    if (codeModel.status!!){
                        cardListener!!.accessCodeListener(codeModel.msg,codeModel.status!!)
                    }else{
                        cardListener!!.accessCodeListener(null, codeModel.status!!)
                    }
                }
            } catch (e: Exception){
                if (e is IllegalStateException)
                    Log.i("ERROR MESSAGE", e.message.toString())
            }
        }
    }
}