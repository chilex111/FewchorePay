package fewchore.com.exolve.fewchore.apis

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.listener.ActiveLoanListener
import fewchore.com.exolve.fewchore.model.ActiveLoanModel
import java.lang.IllegalStateException

class ActiveLoanAsync(private var context: Context): AsyncTask<Void, Int, String>(){
    companion object {
        var activeLoanListener: ActiveLoanListener ?= null
    }
    override fun doInBackground(vararg p0: Void?): String? {
        val userId = AppUtils.getMyUserId(context)
        val url = Const.FEWCHORE_URL + "activeloan/" + userId
        try {
            return HttpUtility.getRequest(url)
        } catch (e: Exception) {
        }
        return null
    }
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            try{
                val type= object :TypeToken<ActiveLoanModel>(){}.type
                val activeLoan = gson.fromJson<ActiveLoanModel>(result, type)
                if (activeLoan.status!!){
                    val details=  activeLoan.activeloan
                    activeLoanListener!!.activeLoanListener(details, null, activeLoan.status!!)
                }else{
                    activeLoanListener!!.activeLoanListener(null,activeLoan.msg, activeLoan.status!!)
                }
            }catch (e: Exception){
                if (e is IllegalStateException)
                    Log.i("ERROR MESSAGE", e.message.toString())
            }
        }
    }
}