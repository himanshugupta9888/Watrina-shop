package com.watirna.shop.views.product.productDML

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.watirna.foodiemodule.data.PaginationScrollListener
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityProductListBinding
import com.watirna.shop.datamodel.product.ProductItemsModel
import com.watirna.shop.views.adapters.ProductListAdapter
import com.watirna.shop.views.product.productDML.create.ProductCreateActivity
import com.watirna.shop.views.product.productDML.edit.editproduct.EditProduct
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_header.view.*

class ProductListActivity : BaseActivity<ActivityProductListBinding>(), ProductListNavigator {

    lateinit var viewModel: ProductListViewModel
    lateinit var productListBinding: ActivityProductListBinding
//    var products: ArrayList<ProductItemsModel.ResponseData.Data> = ArrayList()
    var products =  MutableLiveData<ObservableArrayList<ProductItemsModel.ResponseData.Data>>(
        ObservableArrayList()
    )

    var page = 1
    var loadMore = false
    var offset = 0

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        productListBinding = mViewDataBinding as ActivityProductListBinding
        viewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        productListBinding.productListViewModel = viewModel
        viewModel.navigator = this

        mViewDataBinding.productHeader.tbr_title.setText(resources.getString(R.string.title_product))
        mViewDataBinding.productHeader.iv_left.visibility = View.VISIBLE
        mViewDataBinding.productHeader.iv_left.setOnClickListener(View.OnClickListener {
            finish()
        })
        products.value!!.clear()

        var hashMap = HashMap<String, Any>()
        hashMap.put("limit", 10)
        hashMap.put("offset", offset)

        viewModel.apiCallProducts(hashMap, page)

        viewModel.loadingLiveData.observe(this, Observer<Boolean> {
            baseLiveDataLoading.value = it
        })

        viewModel.productListItems.observe(this, Observer {
            loadingObservable.value=false

            products.value!!.addAll(it.responseData?.data!!)
            if(page == 1) {
                if (it.responseData.next_page_url != null) {
                    page += 1
                    loadMore = true
                }
                productListBinding.productListAdapter =
                    ProductListAdapter(this, viewModel, products.value!!, loadingObservable)
            }else{
                if(it.responseData.next_page_url != null){
                    page += 1
                    loadMore = true
                }
                productListBinding.productListAdapter!!.notifyDataSetChanged()
            }
            if(it.responseData.to!! < it.responseData.total!!){
                loadMore = true
            }
        })

        viewModel.commonSuccessResponse.observe(this, Observer {
            products.value!!.clear()
            loadingObservable.value=false
            Toasty.success(this,""+it.message,Toast.LENGTH_SHORT).show()
            page = 1
            viewModel.apiCallProducts(hashMap, page)
        })

        productListBinding.btnAddProduct.setOnClickListener{
            startActivity(Intent(this,ProductCreateActivity::class.java))
        }

        productListBinding.itemRv.addOnScrollListener(object : PaginationScrollListener(productListBinding.itemRv.layoutManager as LinearLayoutManager) {
            override fun isLastPage() = false
            override fun isLoading() = false
            override fun loadMoreItems() {
                when (loadMore) {
                    true -> {
                        loadMore = false
                        var hashMap = HashMap<String, Any>()
                        hashMap.put("limit", 10)
                        hashMap.put("offset", offset)
                        viewModel.apiCallProducts(hashMap, page)
                    }
                }
            }
        })

    }

    override fun getLayoutId() = R.layout.activity_product_list
    override fun openCreateProductActivity() {
        startActivityForResult(Intent(this,ProductCreateActivity::class.java),115)
    }

    override fun openEditProductActivity(id: Int) {
        startActivityForResult(Intent(this, EditProduct::class.java).putExtra("product_id", id),120)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 115) {
            products.value!!.clear()
            loadingObservable.value = true
            var hashMap = HashMap<String, Any>()
            hashMap.put("limit", 10)
            offset = 0
            hashMap.put("offset", offset)
            page = 1
            viewModel.apiCallProducts(hashMap, page)
        } else if (resultCode == 120) {
            products.value!!.clear()
            loadingObservable.value = true
            var hashMap = HashMap<String, Any>()
            hashMap.put("limit", 10)
            offset = 0
            hashMap.put("offset", offset)
            page = 1
            viewModel.apiCallProducts(hashMap, page)
        }
    }
}
