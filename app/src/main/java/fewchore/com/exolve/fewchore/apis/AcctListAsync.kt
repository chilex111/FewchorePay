package fewchore.com.exolve.fewchore.apis

import android.content.Context
import android.os.AsyncTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.helper.SharedPreferenceUtil
import fewchore.com.exolve.fewchore.listener.AcctListListener
import fewchore.com.exolve.fewchore.model.AcctListModel
import java.lang.IllegalStateException

class AcctListAsync(private var context: Context): AsyncTask<Void, Int, String>(){
    companion object {
        var listener
                : AcctListListener ?= null
        var sharedPrefernce : SharedPreferenceUtil?= null
    }
    override fun doInBackground(vararg p0: Void?): String? {
        val userId = AppUtils.getMyUserId(context)
        val url = Const.FEWCHORE_URL+"userbanks/"+userId
        try {
            return HttpUtility.getRequest(url)
        }catch (e:Exception){}
        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            sharedPrefernce = SharedPreferenceUtil
            try{
                val type= object :TypeToken<AcctListModel>(){}.type
                val activeLoan = gson.fromJson<AcctListModel>(result, type)
                if (activeLoan.status =="success"){
                    val details=  activeLoan.bankDetails
                    sharedPrefernce!!.save(context,"verified", AppUtils.PREF_BVN)
                    listener!!.acctDetailsListener(details, activeLoan.status!!)
                }else{
                    listener!!.acctDetailsListener(null, activeLoan.msg!!)
                }
            }catch (e: Exception){
                if (e is IllegalStateException)
                    listener!!.acctDetailsListener(null, "Please check your internet connection")

            }
        }
    }
}