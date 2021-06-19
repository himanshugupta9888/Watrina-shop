package com.watirna.shop.base

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.watirna.shop.utils.CustomLoaderDialog
import es.dmoral.toasty.Toasty

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    protected var baseLiveDataLoading = MutableLiveData<Boolean>()
    private var mViewDataBinding: T? = null
    protected abstract fun initView(mViewDataBinding: ViewDataBinding?)
    private lateinit var mCustomLoaderDialog: CustomLoaderDialog

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    val loadingObservable: MutableLiveData<Boolean> get() = baseLiveDataLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        initView(mViewDataBinding)
        mCustomLoaderDialog = CustomLoaderDialog(this, true)
        baseLiveDataLoading.observe(this, Observer {
            if (it) showLoading()
            else hideLoading()
        })
    }

    private fun showLoading() {
        try {
            if (mCustomLoaderDialog.window != null) {
                mCustomLoaderDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                mCustomLoaderDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showErrorToast(msg: String) {
        Toasty.error(this, msg, 3000, false).show()
    }

    fun showSuccessToast(msg: String) {
        Toasty.success(this, msg, 3000, false).show()
    }

    private fun hideLoading() = mCustomLoaderDialog.cancel()

}