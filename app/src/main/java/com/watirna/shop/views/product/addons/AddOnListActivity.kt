package com.watirna.shop.views.product.addons

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityAddOnListBinding
import com.watirna.shop.views.adapters.AddOnAdapter
import com.watirna.shop.views.product.addons.create.AddAddOnActivity
import com.watirna.shop.views.product.addons.edit.EditAddOnActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_header.view.*

class AddOnListActivity : BaseActivity<ActivityAddOnListBinding>(), AddOnListNavigator {


    lateinit var viewModel: AddOnListViewModel
    lateinit var addOnListBinding: ActivityAddOnListBinding

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        addOnListBinding = mViewDataBinding as ActivityAddOnListBinding
        viewModel = ViewModelProviders.of(this).get(AddOnListViewModel::class.java)
        addOnListBinding.addOnMainViewModel = viewModel
        viewModel.navigator = this

        loadingObservable.value = true

        mViewDataBinding.addonHeader.tbr_title.setText(resources.getString(R.string.select_addons))
        mViewDataBinding.addonHeader.iv_left.visibility = View.VISIBLE
        mViewDataBinding.addonHeader.iv_left.setOnClickListener(View.OnClickListener {
            finish()
        })

        viewModel.addOnResponse.observe(this, Observer {
            val addonAdapter = AddOnAdapter(
                this,viewModel,
                (it.responseData),loadingObservable
            )
            addOnListBinding.addonAdapter.adapter = addonAdapter
            loadingObservable.value = false
        })

        viewModel.commonSuccessResponse.observe(this, Observer {
            Toasty.success(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            viewModel.addOnList()
        })

    }

    override fun getLayoutId() = R.layout.activity_add_on_list

    override fun openAddOnCreateActivity() {
        startActivityForResult(Intent(this, AddAddOnActivity::class.java), 111)
    }

    override fun openEditAddOnActivity(addOnId: Int) {
        var intent = Intent(this, EditAddOnActivity::class.java)
        intent.putExtra("addOnId", addOnId)
        startActivityForResult(intent, 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 111) {
            loadingObservable.value=true
            viewModel.addOnList()
        }

    }
}
