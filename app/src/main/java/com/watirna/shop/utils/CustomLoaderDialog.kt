package com.watirna.shop.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.watirna.shop.R
import com.watirna.shop.databinding.LayoutCustomLoaderDialogBinding

class CustomLoaderDialog(context: Context) : Dialog(context) {
    private lateinit var mCustomDialogBinding: LayoutCustomLoaderDialogBinding
    private var enableLottie: Boolean = false

    constructor(context: Context, enableLottie: Boolean = false) : this(context) {
        this.enableLottie = enableLottie
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCustomDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_custom_loader_dialog,
            null,
            false
        )
        setContentView(mCustomDialogBinding.root)
        setCancelable(false)

        if (enableLottie) {
            mCustomDialogBinding.lottieIndicator.visibility = View.VISIBLE
        } else {
            mCustomDialogBinding.lottieIndicator.visibility = View.GONE
        }

    }

}
