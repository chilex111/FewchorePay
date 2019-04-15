package fewchore.com.exolve.fewchore.apis

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.listener.InvestmentHistoryListener
import fewchore.com.exolve.fewchore.model.AllInvestmentHistoryModel
import java.lang.IllegalStateException


@SuppressLint("StaticFieldLeak")
class InvestmentHistoryAsync(private var activity: Context?) : AsyncTask<Void, Int, String>() {
    companion object {
        var historyListener: InvestmentHistoryListener ?= null
    }

    override fun doInBackground(vararg p0: Void?): String? {
        val userId = AppUtils.getMyUserId(activity)
        val url = Const.FEWCHORE_URL + "user_investments/" + userId
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
            try {
                val type = object : TypeToken<AllInvestmentHistoryModel>() {}.type
                val historyModel = gson.fromJson<AllInvestmentHistoryModel>(result, type)
                if (historyModel != null) {
                    val historyModelList = historyModel.msg
                    if (historyModel.status!!) {
                        historyListener!!.historyListener(historyModelList!!, historyModel.status, null)
                    } else {
                        historyListener!!.historyListener(null, historyModel.status, null)
                    }
                }
            }catch (e: Exception){
                if (e is IllegalStateException)
                    historyListener!!.historyListener(null, false, "Please check your Internet connection")

            }

        }
    }

}