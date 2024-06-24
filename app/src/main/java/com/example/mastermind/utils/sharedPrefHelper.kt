package com.example.mastermind.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {
    private const val SHARED_PREFS_NAME = "MasterMindPrefs"
    private const val KEY_USERNAME = "username"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUsername(context: Context, username: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    fun getUsername(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_USERNAME, null)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getUsername(context) != null && getUsername(context)?.isNotEmpty() ?: false
    }

    fun clearUsername(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(KEY_USERNAME)
        editor.apply()
    }
}
