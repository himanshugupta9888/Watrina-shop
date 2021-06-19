package com.watirna.app.ui.changepasswordactivity

interface ChangePasswordNavigator{

    fun saveNewPassword()
    fun checkValidation() : Boolean
    fun showError(error:String)
}