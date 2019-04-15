package fewchore.com.exolve.fewchore.apis

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.helper.saveDurationToSharedPrefs
import fewchore.com.exolve.fewchore.model.LoanTimModel


@SuppressLint("StaticFieldLeak")
class LoanDurationAsync( private var context: Context) :AsyncTask<Void, Int, String>() {
    override fun doInBackground(vararg p0: Void?): String {
        val url = Const.FEWCHORE_URL+"get/loanfrequency"
        return HttpUtility.getRequest(url)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            try{
                val type= object : TypeToken<List<LoanTimModel>>(){}.type
                val activeLoan = gson.fromJson<List<LoanTimModel>>(result, type)
                if (activeLoan != null){
                    context.saveDurationToSharedPrefs(R.string.loan_duration, activeLoan)
                }
        }catch (e: Exception){
            Log.e("TAG", e.message.toString())
            }
        }
    }

}
