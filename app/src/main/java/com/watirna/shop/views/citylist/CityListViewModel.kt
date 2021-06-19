package com.watirna.shop.views.citylist

import com.watirna.shop.base.BaseViewModel

class CityListViewModel : BaseViewModel<CityListNavigator>() {
    fun closeActivity() {
        navigator.closeActivity()
    }
}