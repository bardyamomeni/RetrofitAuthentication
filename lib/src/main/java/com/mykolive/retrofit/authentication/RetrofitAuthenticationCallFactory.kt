package com.mykolive.retrofit.authentication

import com.mykolive.retrofit.authentication.annotation.Authenticated
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Invocation

class RetrofitAuthenticationCallFactory(private val client: OkHttpClient) : okhttp3.Call.Factory {

    override fun newCall(request: Request): okhttp3.Call {
        val invocation = request.tag(Invocation::class.java)
        return if (invocation == null)
            client.newCall(request)
        else {
            val authenticated = invocation.method().annotations.firstOrNull { ann ->
                ann.annotationClass == Authenticated::class
            }
            if (authenticated != null)
                client.newCall(request.newBuilder().tag(authenticated).build())
            else client.newCall(request)
        }
    }

}