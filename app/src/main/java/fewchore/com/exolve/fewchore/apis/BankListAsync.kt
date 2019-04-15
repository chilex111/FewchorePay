package fewchore.com.exolve.fewchore.apis

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import android.widget.Toast.makeText
import com.google.gson.Gson
import fewchore.com.exolve.fewchore.helper.Const
import fewchore.com.exolve.fewchore.helper.HttpUtility
import fewchore.com.exolve.fewchore.local_database.LocalDatabase
import fewchore.com.exolve.fewchore.model.BankListModel
import java.lang.IllegalStateException

@SuppressLint("StaticFieldLeak")
class BankListAsync(private val context: Context) : AsyncTask<Void, Int, String>() {

    override fun doInBackground(vararg p0: Void?): String? {
        val url = Const.FEWCHORE_URL+"allbanks"
        try {
            return HttpUtility.getRequest(url)
        }catch (e: Exception){}
        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null){
            try {
                val gson = Gson()
                val bankList = gson.fromJson<BankListModel>( result, BankListModel::class.java)
                if (bankList!= null){
                    val banks = bankList.result
                    val localDatabase = LocalDatabase(context)
                    localDatabase.insertBank(banks)
                }
            }catch (e: Exception){
                if (e is IllegalStateException)
                    Log.i("ERROR MESSAGE", e.message.toString())
            }


        }
    }
}