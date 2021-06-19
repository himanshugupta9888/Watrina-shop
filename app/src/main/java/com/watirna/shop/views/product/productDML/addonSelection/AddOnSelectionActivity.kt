package com.watirna.shop.views.product.productDML.addonSelection

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityAddSelectionBinding
import com.watirna.shop.datamodel.product.ItemsAddOn
import com.watirna.shop.views.adapters.AddOnSelectionAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_header.view.*

class AddOnSelectionActivity : BaseActivity<ActivityAddSelectionBinding>(),
    AddOnSelectionNavigator {
    lateinit var mActivityAddSelectionBinding: ActivityAddSelectionBinding
    lateinit var viewModel: AddOnSelectionViewModel
    var addons = ArrayList<ItemsAddOn>()

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mActivityAddSelectionBinding = mViewDataBinding as ActivityAddSelectionBinding
        viewModel = ViewModelProviders.of(this).get(AddOnSelectionViewModel::class.java)
        mActivityAddSelectionBinding.addOnSelectionViewModel = viewModel
        viewModel.navigator = this
        viewModel.addOnResponse.observe(this, Observer {
            mActivityAddSelectionBinding.addOnSelectionAdapter =
                AddOnSelectionAdapter(this, viewModel, it.responseData, loadingObservable)
            if (intent.hasExtra("edit_addon")) {
                addons = intent.getParcelableArrayListExtra<ItemsAddOn>("addons")!!
                for (selection in addons)
                    for (data in it.responseData) {
                        if (data.id == selection.store_addon_id){
                            data.selection = true
                            data.price=selection.price
                            viewModel.addOnIdArray.add(""+data.id!!)
                            viewModel.addOnPrice.add(""+data.price!!)
                        }
                    }
            }
        })

        mViewDataBinding.addonHeader.tbr_title.setText(resources.getString(R.string.addon_name))

        mViewDataBinding.addonHeader.iv_left.setOnClickListener {
            finish()
        }

        mActivityAddSelectionBinding.btnSave.setOnClickListener {
            if (validation()) {
                var intent = Intent()
                if (viewModel.addOnIdArray.size > 0) {
                    intent.putStringArrayListExtra("addonid", viewModel.addOnIdArray)
                    intent.putStringArrayListExtra("addonprice", viewModel.addOnPrice)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            } else {
                Toasty.error(this, "Please select any AddOn and Save", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun validation(): Boolean {
        if (viewModel.addOnIdArray.size == 0) {
            return false
        }
        return true
    }

    override fun getLayoutId() = R.layout.activity_add_selection
}