package com.watirna.shop.views.countrylist

import com.watirna.shop.base.BaseViewModel

class CountryViewModel : BaseViewModel<CountryNavigator>() {
    fun closeActivity() {
        navigator.closeActivity()
    }
}