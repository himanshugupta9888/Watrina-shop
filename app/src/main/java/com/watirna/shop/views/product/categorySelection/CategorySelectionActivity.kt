package com.watirna.shop.views.product.categorySelection

import android.app.Activity
import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityCategorySelectionBinding
import com.watirna.shop.views.adapters.CategorySelectionAdapter
import com.watirna.shop.views.product.productDML.create.ProductCreateActivity
import kotlinx.android.synthetic.main.layout_header.view.*

class CategorySelectionActivity : BaseActivity<ActivityCategorySelectionBinding>(),
    CategorySelectionNavigator {
    lateinit var viewModel: CategorySelectionViewModel
    lateinit var mActivityCategorySelectionBinding: ActivityCategorySelectionBinding

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mActivityCategorySelectionBinding = mViewDataBinding as ActivityCategorySelectionBinding
        viewModel = ViewModelProviders.of(this).get(CategorySelectionViewModel::class.java)
        mActivityCategorySelectionBinding.categoryViewModel = viewModel
        viewModel.navigator = this


       mActivityCategorySelectionBinding.categoryHeader.tbr_title.setText(resources.getString(R.string.categories))

        mActivityCategorySelectionBinding.categoryHeader.iv_left.setOnClickListener {
            finish()
        }

        viewModel.categoryModel.observe(this, Observer {
            var intentPass = Intent(this, ProductCreateActivity::class.java)
            intentPass.putExtra("category_name",""+it.store_category_name)
            intentPass.putExtra("category_id",""+it.id)
            setResult(Activity.RESULT_OK, intentPass)
            finish()
        })

        viewModel.categoryItemsModel.observe(this, Observer {
            mActivityCategorySelectionBinding.selectCategoryAdapter= CategorySelectionAdapter(this,viewModel,it.responseData.data,loadingObservable)
        })

    }

    override fun getLayoutId() = R.layout.activity_category_selection
}