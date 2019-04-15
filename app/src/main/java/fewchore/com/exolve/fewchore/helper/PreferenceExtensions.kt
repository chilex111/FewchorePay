package fewchore.com.exolve.fewchore.helper

import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.StringRes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fewchore.com.exolve.fewchore.R
import fewchore.com.exolve.fewchore.model.LoanTimModel

//add data
fun Context.saveDurationToSharedPrefs(@StringRes res: Int, complaintModel: List<LoanTimModel>) {
    val editor = getPrefs(this).edit()
    val key = getString(res)
    val gson = Gson()
    val jsonCart = gson.toJson(complaintModel)
    editor.putString(key, jsonCart)
    editor.apply()
}

//get data
fun Context.getDurationToSharedPrefs(@StringRes key: Int): ArrayList<LoanTimModel>? {
    val prefs = getPrefs(this)
    val cartData: List<LoanTimModel>
    if (prefs.contains(getString(key))) {
        val jsonCart = prefs.getString(getString(key), null)
        val gson = Gson()
        val collectionType = object : TypeToken<List<LoanTimModel>>() {}.type
        val cartItems = gson.fromJson<List<LoanTimModel>>(jsonCart, collectionType)
       // val cartItems = gson.fromJson<EduPlanTypeModel>(jsonCart, EduPlanTypeModel::class.java)

      //  cartData = Arrays.asList(cartItems)
        cartData = ArrayList<LoanTimModel>(cartItems)
    } else {
        return try {
            ArrayList()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }
    return cartData
}
private fun getPrefs(context: Context): SharedPreferences {
    val prefsName = context.getString(R.string.app_name)
    return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
}