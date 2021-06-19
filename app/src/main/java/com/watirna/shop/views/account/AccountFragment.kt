package com.watirna.shop.views.account

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.watirna.app.ui.changepasswordactivity.ChangePasswordActivity
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseFragment
import com.watirna.shop.databinding.FragmentAccountBinding
import com.watirna.shop.datamodel.AccountMenuModel
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.views.dashboard.DashboardNavigator
import com.watirna.shop.views.editrestaurant.EditRestaurantActivity
import com.watirna.shop.views.edittime.EditTimeActivity
import com.watirna.shop.views.revenue.RevenueActivity
import javax.inject.Inject

class AccountFragment : BaseFragment<FragmentAccountBinding>(), AccountNavigator {
    private lateinit var mBinding: FragmentAccountBinding
    private lateinit var mViewModel: AccountViewModel
    private lateinit var dashBoardNavigator: DashboardNavigator

    @Inject
    lateinit var sessionManager: SessionManager

    init {
        AppController.appComponent.inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashboardNavigator
    }

    override fun getLayoutId() = R.layout.fragment_account
    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        mViewModel.navigator = this
        mBinding = mViewDataBinding as FragmentAccountBinding
        mBinding.lifecycleOwner = this
        mBinding.accountViewModel = mViewModel
        dashBoardNavigator.setTitle(resources.getString(R.string.title_account))
        dashBoardNavigator.setRightIcon(R.drawable.ic_logout)
        dashBoardNavigator.hideRightIcon(false)
        dashBoardNavigator.hideLeftIcon(true)
        //dashBoardNavigator.showLogo(false)

        /*  dashBoardNavigator.getInstance().iv_right.setOnClickListener {
              ViewUtils.showAlert(activity!!, getString(R.string.xjek_logout_alert), object : ViewUtils.ViewCallBack {
                  override fun onPositiveButtonClick(dialog: DialogInterface) {
                      mViewModel.logoutApp()
                      dialog.dismiss()
                  }

                  override fun onNegativeButtonClick(dialog: DialogInterface) {
                      dialog.dismiss()
                  }
              })
          }*/

        val accountMenuTitles = resources.getStringArray(R.array.title_account)
        val accountMenuIcons = resources.obtainTypedArray(R.array.icon_account)
        val accountMenus = List(accountMenuTitles.size) {
            AccountMenuModel(accountMenuTitles[it], accountMenuIcons.getResourceId(it, -1))
        }

        for (accountMenu: AccountMenuModel in accountMenus) {
            if (!sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME,"").equals("FOOD",true)) {
                if (accountMenu.text.equals("Edit Restaurant",true)) {
                    accountMenu.text = "Edit Shop"
                }
            }
            Log.e("Icon", "---" + accountMenu.resId + " -----" + accountMenu.text)

        }
        accountMenuIcons.recycle()
        mViewModel.setAccountMenus(accountMenus)
        mViewModel.setAdapter()


    }


    override fun onMenuItemClicked(position: Int) = when (position) {

        0 -> {
            val intent = Intent(activity, EditRestaurantActivity::class.java)
            startActivity(intent)
        }

        1 -> {
            val intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        2 -> {
            val intent = Intent(activity, EditTimeActivity::class.java)
            startActivity(intent)
        }

        3 -> {
            val intent = Intent(activity, RevenueActivity::class.java)
            startActivity(intent)
        }


        else -> {

        }
    }
}