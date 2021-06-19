package com.watirna.shop.views.product.productDML.price

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityPriceSelectionBinding
import com.watirna.shop.datamodel.addon.KeyValuePair
import com.watirna.shop.datamodel.product.ItemsAddOn
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.views.product.productDML.addonSelection.AddOnSelectionActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_header.view.*
import javax.inject.Inject

class PriceSelectionActivity : BaseActivity<ActivityPriceSelectionBinding>(),
    PriceSelectionNavigator {
    lateinit var viewModel: PriceSelectionViewModel
    lateinit var mActivityProductCreateBinding: ActivityPriceSelectionBinding
    var keyValue = ArrayList<KeyValuePair>()

    var idArray: ArrayList<String>? = null
    var priceArray: ArrayList<String>? = null
    var addons = ArrayList<ItemsAddOn>()

    @Inject
    lateinit var sessionManager: SessionManager

    init {
        AppController.appComponent.inject(this)
    }


    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mActivityProductCreateBinding = mViewDataBinding as ActivityPriceSelectionBinding
        viewModel = ViewModelProviders.of(this).get(PriceSelectionViewModel::class.java)
        mActivityProductCreateBinding.priceViewModel = viewModel
        viewModel.navigator = this

        keyValue.add(KeyValuePair("0", "Percentage"))
        keyValue.add(KeyValuePair("1", "Amount"))
        setData()

        mViewDataBinding.addonHeader.tbr_title.setText(resources.getString(R.string.price_selection))

        mViewDataBinding.addonHeader.iv_left.setOnClickListener {
            finish()
        }

        if (intent.hasExtra("edit_product")) {
            addons = intent.getParcelableArrayListExtra<ItemsAddOn>("addons")!!

            viewModel.price.set("" + intent.getDoubleExtra("item_price", 0.0))
            viewModel.discount.set("" + intent.getDoubleExtra("item_discount", 0.0))
            for (distype in keyValue) {
                if (intent.getStringExtra("item_discount_type").equals(distype.value, true)) {
                    mActivityProductCreateBinding.percentageSpinner.setSelection(distype.key.toInt())
                    break
                }
            }
        }

        if (sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME, "")
                .equals("FOOD", true)
        ) {
            mActivityProductCreateBinding.createAddText.visibility = View.VISIBLE
        } else {
            mActivityProductCreateBinding.createAddText.visibility = View.GONE
        }

        mActivityProductCreateBinding.createAddText.setOnClickListener {
            if (intent.hasExtra("edit_product")) {
                if (addons != null && addons.size > 0) {
                    startActivityForResult(
                        Intent(this, AddOnSelectionActivity::class.java)
                            .putExtra("edit_addon",true)
                            .putParcelableArrayListExtra("addons", addons), 114
                    )
                } else startActivityForResult(Intent(this, AddOnSelectionActivity::class.java), 114)
            }else startActivityForResult(Intent(this, AddOnSelectionActivity::class.java), 114)
        }

        mActivityProductCreateBinding.btnSave.setOnClickListener {
            if (validataion()) {
                var intent = Intent()
                intent.putExtra("price", viewModel.price.get())
                intent.putExtra("discount", viewModel.discount.get())
                intent.putExtra(
                    "discount_type",
                    keyValue.get(mActivityProductCreateBinding.percentageSpinner.selectedItemPosition).value.toUpperCase()
                )
                if (idArray != null && priceArray != null) {
                    intent.putStringArrayListExtra("addonid", idArray)
                    intent.putStringArrayListExtra("addonprice", priceArray)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            /*else {
                Toasty.error(this, "Validation error", Toast.LENGTH_SHORT).show()
            }*/
        }
    }

    private fun setData() {
        //fill data in spinner
        val adapter: ArrayAdapter<KeyValuePair> = ArrayAdapter<KeyValuePair>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            keyValue
        )
        mActivityProductCreateBinding.percentageSpinner.adapter = adapter
        mActivityProductCreateBinding.percentageSpinner.setSelection(
            adapter.getPosition(
                keyValue.get(
                    0
                )
            )
        ) //Optional to set the selected item.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                114 -> {
                    idArray = data?.getStringArrayListExtra("addonid")
                    priceArray = data?.getStringArrayListExtra("addonprice")
                }
            }

        }
    }

    fun validataion(): Boolean {
        if (viewModel.price.get().equals("")) {
            Toasty.error(this, "Enter price for the Item", Toast.LENGTH_SHORT).show()
            return false
        }
        /*else if (viewModel.discount.get().equals("")) {
            Toasty.error(this, "Enter discount for the Item", Toast.LENGTH_SHORT).show()
            return false
        } */
        else return true
    }

    override fun getLayoutId() = R.layout.activity_price_selection


}