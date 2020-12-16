package com.mykolive.retrofit.authentication.adapter

import com.mykolive.retrofit.authentication.annotation.Authenticated
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type

class AuthenticatedRequestCallAdapterFactory :
    CallAdapter.Factory() {
    override fun get(t: Type, a: Array<Annotation>, r: Retrofit): CallAdapter<*, *>? {
        val adapter = r.nextCallAdapter(this, t, a)
        a.firstOrNull { it.annotationClass == Authenticated::class }
            ?: return null
        return AuthenticatedRequestCallAdapter(adapter)
    }

}