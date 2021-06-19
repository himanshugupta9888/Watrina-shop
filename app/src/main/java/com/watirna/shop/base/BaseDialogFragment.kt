package com.watirna.base.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData


abstract class BaseDialogFragment<T : ViewDataBinding> : DialogFragment() {

    private lateinit var mViewDataBinding: T
    private lateinit var rootView: View
    private var mActivity: FragmentActivity? = null
    private val loadingLiveData = MutableLiveData<Boolean>()


    val loadingObservable: MutableLiveData<*> get() = loadingLiveData
    abstract fun initView(viewDataBinding: ViewDataBinding, view: View)



    override fun onAttach(context: Context) {
        super.onAttach(context)
        //LocaleUtils.setLocale(context)
        mActivity = activity
    }

    @LayoutRes
    protected abstract fun getLayout(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        rootView = mViewDataBinding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(mViewDataBinding, rootView)

    }

    protected fun showLoading() {

    }

    private fun hideLoading() {
    }
}
