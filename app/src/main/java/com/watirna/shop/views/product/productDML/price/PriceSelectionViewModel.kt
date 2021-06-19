package com.watirna.shop.views.product.productDML.price

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.watirna.shop.base.BaseViewModel
import com.watirna.shop.datamodel.addon.KeyValuePair

class PriceSelectionViewModel : BaseViewModel<PriceSelectionNavigator>() {
    var price = ObservableField<String>("")
    var discount = ObservableField<String>("")
    var percentage=ObservableArrayList<KeyValuePair>()

}