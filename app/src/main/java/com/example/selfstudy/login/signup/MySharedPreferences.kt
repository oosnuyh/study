package com.example.selfstudy.login.signup

import android.content.Context
import android.content.SharedPreferences

object MySharedPreferences {
    private val myAccount : String = "account"

    fun setUserId(context: Context, input: String ) {
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.putString("my_Id", input)
        editors.commit()
    }
    fun getUserId(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        return prefs.getString("my_Id","").toString()
    }
    fun setUserPw(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.putString("my_Pw", input)
        editors.commit()
    }
    fun getUserPw(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        return prefs.getString("my_Pw","").toString()
    }

    fun clearUser(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.clear()
        editors.commit()
    }
    fun clearInfo(context: Context){
        val pref : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = pref.edit()
        editor.remove("my_Id")
        editor.remove("my_Pw")
        editor.commit()
    }
}
class Manager(val context: Context){
    companion object{
        lateinit var userInfo : UserInfo
        fun setUser(context: Context){
            userInfo = UserInfo(MySharedPreferences.getUserId(context),
                MySharedPreferences.getUserPw(context),
                null)
        }
    }
}