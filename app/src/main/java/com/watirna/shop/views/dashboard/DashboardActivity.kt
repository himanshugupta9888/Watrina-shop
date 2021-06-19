package com.watirna.shop.views.dashboard

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityDashboardBinding
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.views.account.AccountFragment
import com.watirna.shop.views.history.historymain.HisotryMainFragment
import com.watirna.shop.views.login.LoginActivity
import com.watirna.shop.views.orders.incoming.IncomingFragment
import com.watirna.shop.views.product.ProductMainFragment
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_hisotry.*
import kotlinx.android.synthetic.main.header_toolbar.view.*
import kotlinx.android.synthetic.main.layout_header.*
import javax.inject.Inject

class DashboardActivity : BaseActivity<ActivityDashboardBinding>(),
    FragmentManager.OnBackStackChangedListener,
    BottomNavigationView.OnNavigationItemSelectedListener, DashboardNavigator {

    @Inject
    lateinit var commanMethods: CommanMethods

    @Inject
    lateinit var  sessionManager: SessionManager

    init {
        AppController.appComponent.inject(this)
    }

    private var currentFragment: String? = ""
    private lateinit var activityDashboardBinding: ActivityDashboardBinding
    private lateinit var dashboardViewModel: DashboardViewModel
    private var isFragmentInBackstack: Boolean = false

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityDashboardBinding = mViewDataBinding as ActivityDashboardBinding
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        activityDashboardBinding.dashboardModel = dashboardViewModel
        activityDashboardBinding.setLifecycleOwner(this)
        setSupportActionBar(activityDashboardBinding.tlDashboard.tl_header)
        activityDashboardBinding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        supportFragmentManager.addOnBackStackChangedListener(this)

        activityDashboardBinding.bottomNavigation.setSelectedItemId(R.id.home_fragment);

        iv_right.setOnClickListener(View.OnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            dashboardViewModel.logout()
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            dialog.dismiss()
                        }
                    }
                }

            val builder: AlertDialog = AlertDialog.Builder(this).setMessage("Are you sure want to logout?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show()
            commanMethods.AlertButton(builder)

        })
        getObserveValues()
    }

    fun getObserveValues() {
        dashboardViewModel.loadingLiveData.observe(this, Observer<Boolean> {
            baseLiveDataLoading.value = it
        })

        dashboardViewModel.logoutResPonseLiveData.observe(this, Observer<CommonSuccessResponse> {
            if (it.statusCode.equals("200")) {
                commanMethods.showToast(this, getString(R.string.logout_message), true)
                //Clear session values
                sessionManager.sharedPreferences.edit().clear().commit()

               // commanMethods.showToast(context, "successfully logout", true)

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()

            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_dashboard
    }


    private fun addBackStack(
        isReplaceAll: Boolean,
        ft: FragmentTransaction,
        backStackName: String,
        callingFragment: Fragment
    ) {
        if (currentFragment.equals(backStackName)) return

        if (isReplaceAll) {
            if (replaceFragment(backStackName) == false) {
                ft.replace(R.id.main_container, callingFragment, backStackName)
                ft.addToBackStack(callingFragment::class.java.simpleName)
                ft.commit()
                return
            }
        } else {
            ft.replace(R.id.main_container, callingFragment, backStackName)
            ft.addToBackStack(callingFragment::class.java.simpleName)
            ft.commit()
        }
    }

    /**
     * Method called for replacing the fragment.
     */
    private fun replaceFragment(backStackName: String): Boolean {
        val manager: FragmentManager = supportFragmentManager
        isFragmentInBackstack = false
        val count: Int = manager.getBackStackEntryCount()
        if (count > 0) {
            for (i in 0 until count) {
                if (manager.getBackStackEntryAt(i) != null &&
                    manager.getBackStackEntryAt(i).getName().isNullOrEmpty()
                    && !TextUtils.isEmpty(backStackName)
                ) {
                    if (manager.getBackStackEntryAt(i).getName().equals(backStackName)) {
                        manager.popBackStackImmediate(backStackName, 0)
                        isFragmentInBackstack = true
                        break
                    }
                }
            }
        }
        return isFragmentInBackstack

    }

    fun setCurrentFrag(currentFrag: String) {
        currentFragment = currentFrag
    }

    override fun setTitle(title: String) {
        if (!title.isNullOrEmpty())
            tbr_title.setText(title)
    }

    override fun showLogo(isNeedShow: Boolean) {

    }

    override fun setRightIcon(rightIcon: Int) {

    }

    override fun hideRightIcon(isNeedHide: Boolean) {
        if (isNeedHide)
            iv_right.visibility = View.GONE
        else
            iv_right.visibility = View.VISIBLE
    }

    override fun hideLeftIcon(isNeedHide: Boolean) {
        if (isNeedHide)
            iv_left.visibility = View.GONE
        else
            iv_left.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val fragCount = supportFragmentManager.backStackEntryCount
        if (fragCount == 0) {
            finish()
        }
    }

    override fun onBackStackChanged() {
        var topFragment: Fragment? = getTopFragment()
        val bottomNavigationItemView = bottom_navigation
        bottomNavigationItemView.setOnNavigationItemSelectedListener(null)
        if (topFragment != null) {
            when (topFragment::class.java.simpleName) {
                IncomingFragment::class.java.simpleName -> {
                    bottomNavigationItemView.menu.getItem(0).isChecked = true
                }

                HisotryMainFragment::class.java.simpleName -> {
                    bottomNavigationItemView.menu.getItem(1).isChecked = true
                }

                ProductMainFragment::class.java.simpleName -> {
                    bottomNavigationItemView.menu.getItem(2).isChecked = true
                }

                AccountFragment::class.java.simpleName -> {
                    bottomNavigationItemView.menu.getItem(3).isChecked = true
                }

            }
        }
        bottomNavigationItemView.setOnNavigationItemSelectedListener(this)
    }

    fun getTopFragment(): Fragment? {
        supportFragmentManager.run {
            return when (backStackEntryCount) {
                0 -> null
                else -> findFragmentByTag(getBackStackEntryAt(backStackEntryCount - 1).name)
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home_fragment -> {
                val incomingFragment = IncomingFragment()
                val ft = supportFragmentManager.beginTransaction()
                addBackStack(
                    true,
                    ft,
                    incomingFragment::class.java.simpleName,
                    incomingFragment

                )
                true
            }

            R.id.history_fragment -> {
                val historyMainFragment = HisotryMainFragment()
                val ft = supportFragmentManager.beginTransaction()
                addBackStack(
                    true,
                    ft,
                    historyMainFragment::class.java.simpleName,
                    historyMainFragment
                )
                true
            }

            R.id.product_fragment -> {
                val productMainFragment = ProductMainFragment()
                val ft = supportFragmentManager.beginTransaction()
                addBackStack(
                    true,
                    ft,
                    "Product",
                    productMainFragment
                )
                true
            }

            R.id.myaccount_fragment -> {
                val accountFragment = AccountFragment()
                val ft = supportFragmentManager.beginTransaction()
                addBackStack(
                    true,
                    ft,
                    accountFragment::class.java.simpleName,
                    accountFragment
                )
                true
            }

            else -> false
        }
    }

}