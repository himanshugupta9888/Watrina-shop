package com.watirna.shop.views.forgotPasswordActivity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityForgotpasswordBinding
import com.watirna.shop.utils.CommanMethods
import kotlinx.android.synthetic.main.activity_forgotpassword.view.*
import kotlinx.android.synthetic.main.layout_header.view.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.watirna.shop.views.login.LoginActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*
import javax.inject.Inject

class ForgotPasswordActivity : BaseActivity<ActivityForgotpasswordBinding>(),
    ForgotPasswordNavigator {


    lateinit var mViewDataBinding: ActivityForgotpasswordBinding
    lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
    lateinit var mAlertDialog: AlertDialog
    var isResetShown = false

    @Inject
    lateinit var commanMethods: CommanMethods

    init {
        AppController.appComponent.inject(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_forgotpassword



    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityForgotpasswordBinding
        forgotPasswordViewModel = ViewModelProviders.of(this)[ForgotPasswordViewModel::class.java]
        forgotPasswordViewModel.navigator = this
        this.mViewDataBinding.forgotPasswordViewModel = forgotPasswordViewModel
        this.mViewDataBinding.forgotpasswordToolbar.forgotpassword_toolbar.tbr_title.text=(getString(R.string.forgot_password).removeSuffix("?"))
        this.mViewDataBinding.forgotpasswordToolbar.forgotpassword_toolbar.setOnClickListener { view ->
            finish()
        }

        forgotPasswordViewModel.loadingProgress.observe(this, Observer<Boolean> {
            loadingObservable.value = it
        })



        forgotPasswordViewModel.errorResponse.observe(this@ForgotPasswordActivity, Observer<String> { message ->

        })

        forgotPasswordViewModel.forgotPasswordResponse.observe(this, Observer<ForgotPasswordResponse> {
            if(it.statusCode == "200") {
                forgotPasswordViewModel.username = it.responseData.username
                forgotPasswordViewModel.otp = it.responseData.otp
                changePasswordAlert()
            }
        })

        forgotPasswordViewModel.resetResponse.observe(this, Observer<ResetPasswordResponse> {
            if(it.statusCode == "200") {
                commanMethods.showToast(this@ForgotPasswordActivity, it.message, true)
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                showError(it.message)
            }
        })
    }

    private fun dpsToPixels(activity: Activity, dps: Int): Int {
        val r = activity.resources
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }

    fun changePasswordAlert(){
        try {
            isResetShown = true
            val ResetDialogView = LayoutInflater.from(this).inflate(R.layout.activity_forgot_password, null, false)
            val builder = AlertDialog.Builder(this@ForgotPasswordActivity)
            builder.setView(ResetDialogView)
            //finally creating the alert dialog and displaying it
            mAlertDialog = builder.create()

            mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            mAlertDialog?.isShowing?.let {
                mAlertDialog!!.hide()
            }

            mAlertDialog.show()
            mAlertDialog.setCancelable(true)

            var otp = mAlertDialog.findViewById<TextInputEditText>(R.id.et_otp)
            var newpassword_et = mAlertDialog.findViewById<TextInputEditText>(R.id.et_newpassword)
            var confrimpassword_et = mAlertDialog.findViewById<TextInputEditText>(R.id.et_confrimpassword)

            mAlertDialog.btnsubmit.setOnClickListener {
                if (otp?.text?.isEmpty()!!)
                    commanMethods.showToast(this@ForgotPasswordActivity, "Please enter OTP", false)
                else if (newpassword_et?.text?.isEmpty()!!)
                    commanMethods.showToast(
                        this@ForgotPasswordActivity,
                        "Please enter new password",
                        false
                    )
                else if (confrimpassword_et?.text?.isEmpty()!!)
                    commanMethods.showToast(
                        this@ForgotPasswordActivity,
                        "Please enter Confirm password",
                        false
                    )
                else if (confrimpassword_et?.text!!.equals(newpassword_et?.text))
                    commanMethods.showToast(
                        this@ForgotPasswordActivity,
                        "Enter password are not mismatching",
                        false
                    )
                else
                    forgotPasswordViewModel.resetPasswordApicall(otp?.text.toString(), confrimpassword_et?.text.toString())


            }
        } catch (ce: Exception){
            ce.printStackTrace()
        }


    }

    override fun showError(error: String) {
        commanMethods.showToast(this@ForgotPasswordActivity, error, false)
    }

}