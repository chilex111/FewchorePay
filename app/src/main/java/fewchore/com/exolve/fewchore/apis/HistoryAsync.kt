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
import fewchore.com.exolve.fewchore.listener.HistoryListener
import fewchore.com.exolve.fewchore.model.HistoryModel
import java.lang.IllegalStateException


@SuppressLint("StaticFieldLeak")
class HistoryAsnc(private var activity: Context?) : AsyncTask<Void, Int, String>() {
    companion object {
        var historyListener: HistoryListener ?= null
    }

    override fun doInBackground(vararg p0: Void?): String? {
        val userId = AppUtils.getMyUserId(activity)
        val url = Const.FEWCHORE_URL+"loanhistory/"+userId
        try{
        return HttpUtility.getRequest(url)
    }catch (e:Exception){}
        return null
    }


    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            try{
                val type = object : TypeToken<HistoryModel>() {}.type
                val historyModel = gson.fromJson<HistoryModel>(result, type)
                if (historyModel != null) {
                    val historyModelList = historyModel.history
                    if (historyModel.status!!) {
                        historyListener!!.historyListener(historyModelList!!, historyModel.status!!)
                    } else {
                        historyListener!!.historyListener(null, historyModel.status!!)

                    }
                }
            } catch (e: Exception){
                if (e is IllegalStateException)
                    Log.i("ERROR MESSAGE", e.message.toString())
            }
        }
    }

}