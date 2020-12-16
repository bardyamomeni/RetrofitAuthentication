package com.mykolive.retrofit.authentication.adapter

import com.mykolive.retrofit.authentication.annotation.Authenticated
import okhttp3.Request
import retrofit2.Call


class TaggedCall<R>(private val delegate: Call<R>) : Call<R> by delegate {

    private var request = delegate.request().newBuilder().tag(Authenticated::class.java).build()

    override fun request(): Request {
        return request
    }

}