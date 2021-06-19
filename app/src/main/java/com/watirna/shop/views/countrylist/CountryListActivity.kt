package com.watirna.shop.views.countrylist

import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityCountryListBinding
import com.watirna.shop.datamodel.CountryListResponse
import com.watirna.shop.views.adapters.CountryListAdapter

class CountryListActivity : BaseActivity<ActivityCountryListBinding>(),
        SearchView.OnQueryTextListener, CountryNavigator {

    lateinit var mBinding: ActivityCountryListBinding

    override fun getLayoutId(): Int = R.layout.activity_country_list

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mBinding = mViewDataBinding as ActivityCountryListBinding
        this.mBinding.lifecycleOwner = this
        val countryList = intent.getSerializableExtra("countrylistresponse") as CountryListResponse
        mViewDataBinding.countrylistadapter = CountryListAdapter(this, countryList.responseData)
        val mViewModel = CountryViewModel()
        mViewModel.navigator = this
        mViewDataBinding.countrylistmodel = mViewModel
        mViewDataBinding.svCountry.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        mBinding.countrylistadapter!!.filter.filter(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mBinding.countrylistadapter!!.filter.filter(newText)
        return true
    }

    override fun closeActivity() {
      //  ViewUtils.hideSoftInputWindow(this)
        finish()
    }

}