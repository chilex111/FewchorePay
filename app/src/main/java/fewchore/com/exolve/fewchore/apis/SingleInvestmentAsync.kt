package fewchore.com.exolve.fewchore.apis

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.helper.SharedPreferenceUtil
import fewchore.com.exolve.fewchore.listener.SingleInvestmentListener
import fewchore.com.exolve.fewchore.model.SingleinvesteDetail
import java.lang.IllegalStateException

class SingleInvestmentAsync(private var id: String, private var context: Context): AsyncTask<Void, Int, String>(){
    companion object {
        var listener
                : SingleInvestmentListener?= null
    }
    override fun doInBackground(vararg p0: Void?): String? {
        val url= Const.FEWCHORE_URL+"investment_details/"+id
        try {
            return HttpUtility.getRequest(url)
        }catch (e: Exception){}
        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            AcctListAsync.sharedPrefernce = SharedPreferenceUtil
            try{
                val type= object : TypeToken<SingleinvesteDetail>(){}.type
                val singleinvesteDetail = gson.fromJson<SingleinvesteDetail>(result, type)
                if (singleinvesteDetail.status){

                    val invest = singleinvesteDetail.msg

                    listener!!.singleInvestmentListener(invest,singleinvesteDetail.status, null)


                }else{
                    listener!!.singleInvestmentListener( null,false,"there is an error from the server")
                }
            }catch (e: Exception){
                if (e is IllegalStateException)
                    listener!!.singleInvestmentListener( null,false,"Please check your network connection")

            }
        }
    }

}