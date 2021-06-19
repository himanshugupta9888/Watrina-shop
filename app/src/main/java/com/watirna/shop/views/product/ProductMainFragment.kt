package com.watirna.shop.views.product

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseFragment
import com.watirna.shop.databinding.FragmentProductMainBinding
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.views.adapters.ProductMainAdapter
import com.watirna.shop.views.dashboard.DashboardNavigator
import javax.inject.Inject

class ProductMainFragment : BaseFragment<FragmentProductMainBinding>(), ProductMainNavigator {

    private lateinit var viewModel: ProductMainViewModel
    private lateinit var productMainBinding: FragmentProductMainBinding
    private lateinit var dashBoardNavigator: DashboardNavigator

    @Inject
    lateinit var sessionManager: SessionManager

    init {
        AppController.appComponent.inject(this)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashboardNavigator
        dashBoardNavigator.hideRightIcon(true)
    }

    override fun getLayoutId() = R.layout.fragment_product_main

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        productMainBinding = mViewDataBinding as FragmentProductMainBinding
        viewModel = ViewModelProviders.of(this).get(ProductMainViewModel::class.java)
        productMainBinding.productMainViewModel = viewModel
        viewModel.navigator = this

        viewModel.mainViewModel.clear()

        if (sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME,"").equals("FOOD",true)) {
            viewModel.mainViewModel.add(ProductMainModel.ProductMain(1, "ADDONS", R.drawable.image_1))
            viewModel.mainViewModel.add(ProductMainModel.ProductMain(2, "CATEGORY", R.drawable.image_2))
            viewModel.mainViewModel.add(ProductMainModel.ProductMain(3, "PRODUCT", R.drawable.image_3))
        }else{
            viewModel.mainViewModel.add(ProductMainModel.ProductMain(2, "CATEGORY", R.drawable.banner_product))
            viewModel.mainViewModel.add(ProductMainModel.ProductMain(3, "PRODUCT", R.drawable.banner_catogery))
        }


        productMainBinding.productMainAdapter =
            ProductMainAdapter(context!!, viewModel.mainViewModel)

        dashBoardNavigator.setTitle(resources.getString(R.string.title_product))
    }


    override fun setProductMainAdapter() {

    }
}