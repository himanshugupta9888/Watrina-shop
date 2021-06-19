package com.watirna.shop.views.product.addons.edit

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityEditAddOnBinding
import com.watirna.shop.datamodel.addon.KeyValuePair
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_header.view.*

class EditAddOnActivity : BaseActivity<ActivityEditAddOnBinding>(), EditAddOnNavigator {


    lateinit var viewModel: EditAddOnViewModel
    lateinit var mEditAddOnBinding: ActivityEditAddOnBinding
    var keyValue = ArrayList<KeyValuePair>()

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mEditAddOnBinding = mViewDataBinding as ActivityEditAddOnBinding
        viewModel = ViewModelProviders.of(this).get(EditAddOnViewModel::class.java)
        mEditAddOnBinding.editAddOnMainViewModel = viewModel
        viewModel.navigator = this

        mViewDataBinding.addonHeader.tbr_title.setText(resources.getString(R.string.update_addon))
        mViewDataBinding.addonHeader.iv_left.visibility = View.VISIBLE
        mViewDataBinding.addonHeader.iv_left.setOnClickListener(View.OnClickListener {
            setResult(111)
            finish()
        })

        loadingObservable.value=true

        keyValue.add(KeyValuePair("1", "Active"))
        keyValue.add(KeyValuePair("0", "DeActive"))
        setData()

        viewModel.commonSuccessResponse.observe(this, Observer {
            loadingObservable.value = false
            //viewModel.addOnName.set("")
            Toasty.success(this, "" + it.message, Toast.LENGTH_SHORT).show()
            //mEditAddOnBinding.statusSpinner.setSelection(0) //Optional to set the selected item.
        })

        viewModel.addOnResonse.observe(this, Observer {
            loadingObservable.value=false
            viewModel.addOnName.set(it.responseData.addon_name)
            //mEditAddOnBinding.statusSpinner.setSelection(it.responseData.addon_status)
        })

        if(intent.hasExtra("addOnId")){
            viewModel.getSingleAddOnItem(intent.getIntExtra("addOnId",0))
        }
    }

    override fun getLayoutId() = R.layout.activity_edit_add_on

    private fun setData() {
        //fill data in spinner
        val adapter: ArrayAdapter<KeyValuePair> = ArrayAdapter<KeyValuePair>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            keyValue
        )
        mEditAddOnBinding.statusSpinner.adapter = adapter
        mEditAddOnBinding.statusSpinner.setSelection(adapter.getPosition(keyValue.get(0))) //Optional to set the selected item.
    }

    override fun EditAddOnFunction() {
        if (validation()) {
            loadingObservable.value=true
            var hashMap = HashMap<String, Any>()
            hashMap["addon_name"] = viewModel.addOnName.get().toString()
            hashMap["addon_status"] = keyValue.get(mEditAddOnBinding.statusSpinner.selectedItemPosition).key
            viewModel.apiCallAddaddOn(intent.getIntExtra("addOnId",0),hashMap)
        }
    }

    private fun validation(): Boolean {
        return !viewModel.addOnName.get().equals("")
    }

    override fun onBackPressed() {
        setResult(111)
        finish()
    }
}
