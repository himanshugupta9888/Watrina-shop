package com.watirna.shop.views.accept

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.watirna.base.base.BaseDialogFragment
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.databinding.FragmentPrepareTimeBinding
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.views.requestdetail.RequestDetailViewModel
import javax.inject.Inject

class  AcceptOrderFragment:BaseDialogFragment<FragmentPrepareTimeBinding>(),AcceptOrderNavigator{

    private  lateinit var  fragmentPrepareTimeBinding: FragmentPrepareTimeBinding
    private  lateinit var  acceptOrderViewModel: AcceptOrderViewModel
    private  lateinit var   orderDetailViewModel: RequestDetailViewModel


    @Inject
    lateinit var  commanMethods: CommanMethods

    init {
        AppController.appComponent.inject(this)
    }
    override fun getLayout(): Int {
        return R.layout.fragment_prepare_time
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        fragmentPrepareTimeBinding =viewDataBinding as FragmentPrepareTimeBinding
        acceptOrderViewModel=ViewModelProviders.of(this).get(AcceptOrderViewModel::class.java)
        fragmentPrepareTimeBinding.acceptOrderViewModel=acceptOrderViewModel
        orderDetailViewModel=ViewModelProviders.of(activity!!).get(RequestDetailViewModel::class.java)
        fragmentPrepareTimeBinding.setLifecycleOwner(this)
        acceptOrderViewModel.navigator=this


    }

    override fun closeDialogWithPreparetime() {
        if(acceptOrderViewModel.prePareTime.value != null) {
            if (acceptOrderViewModel.prePareTime.value!!.toInt() > 0) {
                orderDetailViewModel.preparationTime.value = acceptOrderViewModel.prePareTime.value
                dialog!!.dismiss()
            } else
                commanMethods.showToast(activity as Context, getString(R.string.please_enter_cooking_time), false)

        } else {
            commanMethods.showToast(activity as Context, getString(R.string.please_enter_cooking_time), false)
        }

    }

    override fun closeDialogWithoutTime() {
        dialog!!.dismiss()
   }

}