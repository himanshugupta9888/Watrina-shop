package com.watirna.shop.views.dashboard

interface  DashboardNavigator {
    fun setTitle(title: String)
    fun showLogo(isNeedShow: Boolean)
    fun setRightIcon(rightIcon: Int)
    fun hideRightIcon(isNeedHide: Boolean)
    fun hideLeftIcon(isNeedHide:Boolean)
}