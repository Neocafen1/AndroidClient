package com.example.neocafeteae1prototype.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.neocafeteae1prototype.data.Consts
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalDatabase @Inject constructor (@ApplicationContext context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("TestApp", Context.MODE_PRIVATE)

    private val editor = prefs.edit()

    fun saveUserName(name: String?){
        if (name != null){
            editor.putString(Consts.USER_NAME,name).apply()
        }
    }

    fun saveAccessToken(token: String?) {
        editor.putString(Consts.ACCESS, token)
        editor.apply()
    }

    fun saveRefreshToken(token: String?) {
        editor.putString(Consts.REFRESH, token)
        editor.apply()
    }

    fun saveUserNumber(number: Int?) {
        if (number != null) {
            editor.putInt(Consts.USER_NUMBER, number)
            editor.apply()
        }
    }

    fun fetchAccessToken(): String? {
        return prefs.getString(Consts.ACCESS, null)
    }

    fun fetchUserName():String? {
        return prefs.getString(Consts.USER_NAME, null)
    }

    fun fetchRefreshToken(): String? {
        return prefs.getString(Consts.REFRESH, null)
    }

    fun fetchUserNumber(): Int {
        return prefs.getInt(Consts.USER_NUMBER, 0)
    }

    fun clearData() {
        prefs.edit().remove(Consts.ACCESS).remove(Consts.REFRESH).remove(Consts.USER_NUMBER).remove(Consts.USER_NAME).apply()
    }
}