package com.watirna.shop.views.accept

import androidx.lifecycle.MutableLiveData
import com.watirna.shop.base.BaseViewModel

class  AcceptOrderViewModel:BaseViewModel<AcceptOrderNavigator>(){
    val prePareTime=MutableLiveData<String>()

    fun acceptorder(){
        navigator.closeDialogWithPreparetime()
    }

    fun cancelOrder(){
        navigator.closeDialogWithoutTime()
    }


}