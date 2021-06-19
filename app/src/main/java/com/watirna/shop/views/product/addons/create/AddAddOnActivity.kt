package com.watirna.shop.views.product.addons.create

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityAddAddOnBinding
import com.watirna.shop.datamodel.addon.KeyValuePair
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_header.view.*

class AddAddOnActivity : BaseActivity<ActivityAddAddOnBinding>(), AddAddOnNavigator {

    lateinit var viewModel: AddAddOnViewModel
    lateinit var mAddAddOnAddOnBinding: ActivityAddAddOnBinding
    var keyValue = ArrayList<KeyValuePair>()

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mAddAddOnAddOnBinding = mViewDataBinding as ActivityAddAddOnBinding
        viewModel = ViewModelProviders.of(this).get(AddAddOnViewModel::class.java)
        mAddAddOnAddOnBinding.addAddOnMainViewModel = viewModel
        viewModel.navigator = this


        mViewDataBinding.addonHeader.tbr_title.setText(resources.getString(R.string.create_addon))
        mViewDataBinding.addonHeader.iv_left.visibility = View.VISIBLE
        mViewDataBinding.addonHeader.iv_left.setOnClickListener(View.OnClickListener {
            setResult(111)
            finish()
        })

        keyValue.add(KeyValuePair("1", "Active"))
        keyValue.add(KeyValuePair("0", "DeActive"))
        setData()

        viewModel.commonSuccessResponse.observe(this, Observer {
            loadingObservable.value=false
            viewModel.addOnName.set("")
            Toasty.success(this, "" + it.message, Toast.LENGTH_SHORT).show()
            mAddAddOnAddOnBinding.statusSpinner.setSelection(0) //Optional to set the selected item.
        })
    }

    override fun getLayoutId() = R.layout.activity_add_add_on

    private fun setData() {
        //fill data in spinner
        val adapter: ArrayAdapter<KeyValuePair> = ArrayAdapter<KeyValuePair>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            keyValue
        )
        mAddAddOnAddOnBinding.statusSpinner.adapter = adapter
        mAddAddOnAddOnBinding.statusSpinner.setSelection(adapter.getPosition(keyValue.get(0))) //Optional to set the selected item.
    }

    override fun createAddFunction() {
        if (validation()) {
            loadingObservable.value=true
            var hashMap = HashMap<String, Any>()
            hashMap["addon_name"] = viewModel.addOnName.get().toString()
            hashMap["addon_status"] = keyValue.get(mAddAddOnAddOnBinding.statusSpinner.selectedItemPosition).key
            viewModel.apiCallAddaddOn(hashMap)
        }
    }

    private fun validation(): Boolean {
        return !viewModel.addOnName.get().equals("")
    }
}
