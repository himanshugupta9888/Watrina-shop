package com.watirna.shop.interfaces

interface ApiListener {
    fun success(successData: Any)
    fun fail(failData: Throwable)
}