package fewchore.com.exolve.fewchore.apis

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.listener.SaveEncryptionListener
import fewchore.com.exolve.fewchore.model.TransactionModel
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StaticFieldLeak")
class TransactionAsync (private var encryptedCard: String,var id: String,private var secretKey: String,
                        private var api_key : String,private var phone: String,
                        private var phoneCon: String, private var email: String, private var context: Context) : AsyncTask<Void, Int, String>(){

    companion object {
       var  saveEncryptionListener: SaveEncryptionListener?= null
    }
    @SuppressLint("SimpleDateFormat")

    override fun doInBackground(vararg p0: Void?): String {
        val nowAsString = SimpleDateFormat("yy").format(Date())
        val transactionRef = nowAsString + SimpleDateFormat("yyyyMMddHHmmss").format(Date())



        val jsonMap = JSONObject()
        val auth = JSONObject()
        val customer = JSONObject()
        val transaction = JSONObject()

        auth.put("type", "card")
        auth.put( "secure",encryptedCard)

        customer.put( "email",email)
        customer.put(   "mobile_no",phone)
        customer.put( "id", phoneCon)

        transaction.put("amount", "500")
        transaction.put("transaction_ref", transactionRef)
        transaction.put("transaction_desc", "Test charge")
        transaction.put("payment_customer_id",phoneCon)

        jsonMap.put("request_ref","REQ$transactionRef")
        jsonMap.put("request_type","charge")
        jsonMap.put( "auth",auth)
        jsonMap.put("customer", customer)
        jsonMap.put("transaction", transaction)



        val url = Const.ONE_PIPE_CUSTOMER+"transact"
        return HttpUtility.postJsonAuth(url, jsonMap, api_key)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!result.isNullOrEmpty()){

          //  relativeLayout!!.visibility = View.GONE
            val gson = Gson()
            try{
                val type = object : TypeToken<TransactionModel>() {}.type
                val userModel = gson.fromJson<TransactionModel>(result, type)

                if (userModel != null) {
                 /*   val concat = "$noText;$cvvText;$expText;$pinText"
                    val ency =DESedeEncryption()
                    val value  =ency.encrypt(concat,secretKey)
*/
                    if(userModel.description != null) {
                        if (userModel.description.contains("Approved by Financial", true)) {
                            //proceed()
                            saveEncryptionListener!!.saveEncryptListener(encryptedCard,userModel.chargeToken,id,userModel.transactionRef,userModel.description)
                           // SaveLookUpAsync(encryptedCard, id, userModel.chargeToken, secretKey, context).execute()
                        }

                        if (userModel.description.contains("Kindly enter the OTP", true)) {
                            saveEncryptionListener!!.saveEncryptListener(encryptedCard, null, id, userModel.transactionRef, userModel.description)

                        }
                    }else
                        SaveLookUpAsync(encryptedCard, id, userModel.chargeToken, secretKey, context).execute()

                }
            }catch (e: Exception){
                Log.i("LOOK_UP", e.message.toString())
            }
        }
    }

}
