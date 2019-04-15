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
import fewchore.com.exolve.fewchore.listener.StatusListener
import fewchore.com.exolve.fewchore.model.ResponseStringModel
import java.lang.IllegalStateException

@SuppressLint("StaticFieldLeak")
class AwaitApprovalAsync(private var context: Context): AsyncTask<Void, Int, String>() {
    companion object {
        var statusListener: StatusListener ?= null
    }
    override fun doInBackground(vararg p0: Void?): String? {
        val userId = AppUtils.getMyUserId(context)
        val url = Const.FEWCHORE_URL +"awaiting/"+userId
        Log.i("LOG_URL", url)
        try {
            return HttpUtility.getRequest(url)
        }catch (e:Exception){}
        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            try {
                val type = object : TypeToken<ResponseStringModel>(){}.type
                val response = gson.fromJson<ResponseStringModel>(result, type)
                if (response.status =="true") {

                    statusListener!!.statusListener(response.msg!!, true, response.user_status)
                } else {
                    statusListener!!.statusListener(response.msg!!, false, response.user_status)
                }


            }catch (e: Exception){
                Log.i("TGA", e.message.toString())
                if (e is IllegalStateException)
                    statusListener!!.statusListener("Please check your network connection", false, "no reponse")
                else{
                    statusListener!!.statusListener("An issue occurred please try again", false, "Error")

                }
            }
        }
    }

}
