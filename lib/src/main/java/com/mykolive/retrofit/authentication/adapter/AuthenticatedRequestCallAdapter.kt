package com.mykolive.retrofit.authentication.adapter

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class AuthenticatedRequestCallAdapter<R, T>(
    private val adapter: CallAdapter<R, T>
) : CallAdapter<R, T> {

    override fun adapt(call: Call<R>): T {
        return adapter.adapt(TaggedCall(call))
    }

    override fun responseType(): Type {
        return adapter.responseType()
    }
}