package com.watirna.shop.views.login

interface  LoginNavigator {
  fun showError(errorMsg:String)
  fun validate():Boolean
  fun forgetPasswordFun()
}