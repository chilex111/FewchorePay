package fewchore.com.exolve.fewchore.helper

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceUtil {
    internal var editor: SharedPreferences.Editor? = null
    private  var PREFS_NAME = "FEW_CHORE"
    fun getValue(context: Context, PREFS_KEY: String): String? {
        val settings: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val text: String?

        //settings = PreferenceManager.getDefaultSharedPreferences(contest);
        text = settings.getString(PREFS_KEY, null)
        return text
    }

    fun clearSharedPreference(context: Context) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor

        //settings = PreferenceManager.getDefaultSharedPreferences(contest);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = settings.edit()

        editor.clear()
        editor.apply()
    }

    fun removeValue(context: Context, PREFS_KEY: String) {
        val settings: SharedPreferences
        val editor: SharedPreferences.Editor

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = settings.edit()

        editor.remove(PREFS_KEY)
        editor.apply()
    }

    fun save(context: Context, text: String, PREFS_KEY: String) {
        val settings: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor

        //settings = PreferenceManager.getDefaultSharedPreferences(contest);
        //1
        editor = settings.edit() //2

        editor.putString(PREFS_KEY, text) //3

        editor.apply() //4
    }

}
