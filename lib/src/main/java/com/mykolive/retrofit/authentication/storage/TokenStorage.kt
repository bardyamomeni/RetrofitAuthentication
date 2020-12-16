package com.mykolive.retrofit.authentication.storage

interface TokenStorage {

    fun isEmpty(): Boolean

    fun save(token: Token): Boolean

    fun read(): Token

}
