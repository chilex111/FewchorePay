package fewchore.com.exolve.fewchore.apis

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.model.TransactionModel
import org.json.JSONObject

@SuppressLint("StaticFieldLeak")
class OTPAsync(private var otpText: String, private var transactionRef: String, private var api_key: String?,
               private var secretKey: String, private var encryptCard: String, private var identifier: String,
          private var context: Context) :AsyncTask<Void, Int, String>() {
    override fun doInBackground(vararg p0: Void?): String {
        val map = JSONObject()

            map.put("transaction_ref",transactionRef)
            map.put("secure", otpText)

        val url = Const.ONE_PIPE+"charge/otp"
        return HttpUtility.postJsonAuth(url, map, api_key!!)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){
            val gson = Gson()
            try{
                val type = object : TypeToken<TransactionModel>() {}.type
                val userModel = gson.fromJson<TransactionModel>(result, type)
               SaveLookUpAsync(encryptCard,identifier,userModel.chargeToken,secretKey,context).execute()
            }catch (e:Exception){
            Log.i("TAGG", e.message.toString())
            }
        }
    }

}
