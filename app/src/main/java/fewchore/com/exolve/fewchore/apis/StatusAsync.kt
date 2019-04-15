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
import fewchore.com.exolve.fewchore.helper.SharedPreferenceUtil
import fewchore.com.exolve.fewchore.listener.StatusListener
import fewchore.com.exolve.fewchore.model.LoginModel
import java.lang.IllegalStateException

@SuppressLint("StaticFieldLeak")
class StatusAsync(private var activity: Context?) : AsyncTask<Void, Int, String>() {
    companion object {
        var statusListener: StatusListener ?= null
    }

    override fun doInBackground(vararg p0: Void?): String? {
        val url = Const.FEWCHORE_URL + "userstatus"
        val userId = AppUtils.getMyUserId(activity)
        val map = HashMap<String, Any?>()
        map["user_id"] = userId
        try {
            return HttpUtility.sendPostRequest(url, map)
        }catch (e:Exception){}
        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()) {
            val gson = Gson()
            try{
                val type = object : TypeToken<LoginModel>() {}.type
                val userModel = gson.fromJson<LoginModel>(result, type)
                if (userModel.status == "true") {
                    val loginModel = userModel.userDetails
                    val loan_status = loginModel!!.userLoanstatus
                    val investment_status = loginModel.userInveststatus
                    val sharedPreferenceUtil = SharedPreferenceUtil

                    sharedPreferenceUtil.save(activity!!, loan_status!!, AppUtils.PREF_LOAN_STATUS)
                    sharedPreferenceUtil.save(activity!!, investment_status!!, AppUtils.PREF_INVESTMENT_STATUS)
                    statusListener!!.statusListener(loan_status, null, investment_status)

                }
            } catch (ex: Exception){
                if (ex is IllegalStateException)
                    Log.i("ERROR MESSAGE", ex.message.toString())

            }
        }
    }
}