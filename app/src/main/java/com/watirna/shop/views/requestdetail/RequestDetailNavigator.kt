package com.watirna.shop.views.requestdetail

interface  RequestDetailNavigator {
   fun getErrorMessage(message:String)
    fun acceptOrder()
    fun cancelOrder()
}