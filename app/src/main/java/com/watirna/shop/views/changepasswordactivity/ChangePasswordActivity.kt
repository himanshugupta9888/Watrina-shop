package com.watirna.app.ui.changepasswordactivity

import android.content.Context
import android.text.TextUtils
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityChangepasswordBinding
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.utils.SessionManager
import kotlinx.android.synthetic.main.layout_header.view.*
import javax.inject.Inject


class ChangePasswordActivity : BaseActivity<ActivityChangepasswordBinding>(),
    ChangePasswordNavigator {

    lateinit var mViewDataBinding: ActivityChangepasswordBinding
    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private lateinit var context: Context

    @Inject
    lateinit var  sessionManager: SessionManager

    init {
        AppController.appComponent.inject(this)
    }

    @Inject
    lateinit var commanMethods: CommanMethods

    override fun getLayoutId(): Int = R.layout.activity_changepassword

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityChangepasswordBinding
        changePasswordViewModel = ViewModelProviders.of(this)[ChangePasswordViewModel::class.java]
        changePasswordViewModel.navigator = this
        this.mViewDataBinding.changePasswordViewModel = changePasswordViewModel
        baseLiveDataLoading = changePasswordViewModel.loadingProgress
        mViewDataBinding.setLifecycleOwner(this)
        context = this

        mViewDataBinding.changepasswordToolbarLayout.iv_right.visibility = View.GONE
        mViewDataBinding.changepasswordToolbarLayout.tbr_title.setText(resources.getString(R.string.title_change_pwd))
        mViewDataBinding.changepasswordToolbarLayout.iv_left.visibility = View.VISIBLE
        mViewDataBinding.changepasswordToolbarLayout.iv_left.setOnClickListener(View.OnClickListener {
            finish()
        })



        changePasswordViewModel.changePasswordResponse.observe(
            this@ChangePasswordActivity,
            Observer<CommonSuccessResponse> {
                loadingObservable.value = false
                commanMethods.showToast(context, resources.getString(R.string.password_updated), true)
                finish()

                /*sessionManager.sharedPreferences.edit().clear().commit()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)*/

            })

        changePasswordViewModel.errorResponse.observe(
            this@ChangePasswordActivity,
            Observer<String> {
                loadingObservable.value = false
            })

    }

    override fun saveNewPassword() {

    }

    override fun checkValidation(): Boolean {

        if (changePasswordViewModel.newPassword.get().equals(changePasswordViewModel.confrimPassword.get()) &&
            !TextUtils.isEmpty(changePasswordViewModel.newPassword.get().toString()) &&
            !TextUtils.isEmpty(changePasswordViewModel.oldPassword.get()) &&
            !TextUtils.isEmpty(changePasswordViewModel.confrimPassword.get())
        )
            return true
        else {
            commanMethods.showToast(
                context,
                resources.getString(R.string.password_not_matched),
                false
            )
            return false
        }
    }

    override fun showError(error: String) {
        commanMethods.showToast(context, error, false)
    }

}