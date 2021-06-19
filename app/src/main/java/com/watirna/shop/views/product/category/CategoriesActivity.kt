package com.watirna.shop.views.product.category

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityCategoriesBinding
import com.watirna.shop.views.adapters.CategoryAdapter
import com.watirna.shop.views.product.category.create.CreateCategoryActivity
import com.watirna.shop.views.product.category.edit.EditCategoryActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_header.view.*

class CategoriesActivity : BaseActivity<ActivityCategoriesBinding>(), CategoriesNavigator {

    lateinit var viewModel: CategoriesViewModel
    lateinit var categoriesBinding: ActivityCategoriesBinding

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        categoriesBinding = mViewDataBinding as ActivityCategoriesBinding
        viewModel = ViewModelProviders.of(this).get(CategoriesViewModel::class.java)
        categoriesBinding.categoriesViewModel = viewModel
        viewModel.navigator = this


        mViewDataBinding.addonHeader.tbr_title.setText(resources.getString(R.string.categories))
        mViewDataBinding.addonHeader.iv_left.visibility = View.VISIBLE
        mViewDataBinding.addonHeader.iv_left.setOnClickListener(View.OnClickListener {
            finish()
        })

        viewModel.getCategoryList()
        loadingObservable.value = true

        viewModel.categoryItemsModel.observe(this, Observer {
            loadingObservable.value = false
            categoriesBinding.categoriesAdapter =
                CategoryAdapter(this, viewModel, it.responseData.data, loadingObservable)
        })

        viewModel.commonSuccessResponse.observe(this, Observer {
            loadingObservable.value = false
            Toasty.success(this, "" + it.message, Toast.LENGTH_SHORT).show()
            viewModel.getCategoryList()
        })
    }

    override fun getLayoutId() = R.layout.activity_categories

    override fun openCreateCategoryActivity() {
        var intent = Intent(this, CreateCategoryActivity::class.java)
        startActivityForResult(intent, 111)
    }

    override fun openEditActivity(id: Int) {
       startActivityForResult(Intent(this,EditCategoryActivity::class.java).putExtra("id",id),111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 111) {
            loadingObservable.value = true
            viewModel.getCategoryList()
        }
    }

}
