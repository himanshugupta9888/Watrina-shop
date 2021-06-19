package com.watirna.shop.views.profile

interface ProfileNavigator {

    fun validateProfileInputs():Boolean
    fun showError(errorMsg:String)
    fun openCountryPicker()
    fun selectProfileImage()


}