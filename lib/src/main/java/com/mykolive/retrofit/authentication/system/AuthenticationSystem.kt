package com.mykolive.retrofit.authentication.system

import com.mykolive.retrofit.authentication.storage.Token
import okhttp3.Request

interface AuthenticationSystem {

    fun initialize(token: Token): Boolean

    fun isEmpty(): Boolean

    fun injectAuthorization(request: Request): Request

    fun reauthorizeRequest(request: Request): Request?

}