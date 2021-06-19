package com.watirna.shop.views.product

import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseViewModel

class ProductMainViewModel : BaseViewModel<ProductMainNavigator>() {

    var mainViewModel = ArrayList<ProductMainModel.ProductMain>()

    init {
        AppController.appComponent.inject(this)
    }


}

data class ProductMainModel(var items: List<ProductMain>) {
    data class ProductMain(var id: Int, var menuName: String, var imageViewDrawable: Int)
}