package com.mykolive.retrofit.authentication.storage.impl

import android.content.Context
import android.content.SharedPreferences
import com.mykolive.retrofit.authentication.storage.Token
import com.mykolive.retrofit.authentication.storage.TokenStorage

class SharedPreferencesAuthorizationStorage(
    context: Context, sharedPrefName: String = "authorization_system"
) : TokenStorage {

    private val sp: SharedPreferences =
        context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)

    private var _token = Token(
        sp.getString(
            "token",
            ""
        )!!, sp.getLong("issue_time", Long.MIN_VALUE)
    )

    override fun isEmpty(): Boolean {
        synchronized(this) {
            return _token.token == "" || _token.issueTime == Long.MIN_VALUE
        }
    }

    override fun save(token: Token): Boolean {
        synchronized(this) {
            if (token.issueTime > _token.issueTime) {
                sp.edit()
                    .putString("token", token.token)
                    .putLong("issue_time", token.issueTime)
                    .apply()
                _token = token
                return true
            }
            return false
        }
    }

    override fun read(): Token {
        return _token
    }


}