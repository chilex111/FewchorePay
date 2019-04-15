package fewchore.com.exolve.fewchore.apis

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.helper.AppUtils
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.listener.CardListListener
import fewchore.com.exolve.fewchore.model.CardListModel
import java.lang.IllegalStateException

class CardListAsync(private var context: Context): AsyncTask<Void, Int, String>(){
    companion object {
        var cardListener: CardListListener ?= null
    }
    override fun doInBackground(vararg p0: Void?): String? {
        val userId = AppUtils.getMyUserId(context)
        val url = Const.FEWCHORE_URL + "usercards/" + userId
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
                val type= object :TypeToken<CardListModel>(){}.type
                val activeLoan = gson.fromJson<CardListModel>(result, type)
                if (activeLoan.status =="success"){
                    val details=  activeLoan.cardDetails

                    cardListener!!.cardDetailsListener(details, activeLoan.status!!)
                }else{
                    cardListener!!.cardDetailsListener(null, activeLoan.status!!)
                }
            } catch (e: Exception){
                if (e is IllegalStateException)
                    Log.i("ERROR MESSAGE", e.message.toString())

            }
        }
    }
}