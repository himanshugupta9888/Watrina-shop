package com.watirna.shop.views.selectcuisine

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivitySelectCuisineBinding
import com.watirna.shop.datamodel.product.CuisineModel
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.views.adapters.CuisineListAdapter
import com.watirna.shop.views.product.productDML.create.ProductCreateActivity
import kotlinx.android.synthetic.main.activity_dash_line_check.view.*
import javax.inject.Inject

class SelectCuisine : BaseActivity<ActivitySelectCuisineBinding>(), SelectCuisineNavigator {

    @Inject
    lateinit var commanMethods: CommanMethods

    private lateinit var selectCuisineBinding: ActivitySelectCuisineBinding

    private lateinit var selectCuisineViewModel: SelectCuisineViewModel
    private lateinit var context: Context

    private var getNorthIndianFoodtext: String = ""
    private var getSouthIndianFoodtext: String = ""
    private var getAmericanFoodtext: String = ""
    private var getArabianFoodtext: String = ""
    private var getBakerstext: String = ""
    private var getAsiantext: String = ""
    private var getAfricanFoodtext: String = ""
    private val setCuisineText: Intent = Intent()
    private val cuisineArrayList: ArrayList<String> = ArrayList()


    override fun initView(mViewDataBinding: ViewDataBinding?) {

        selectCuisineBinding = mViewDataBinding as ActivitySelectCuisineBinding

        selectCuisineViewModel = ViewModelProviders.of(this).get(SelectCuisineViewModel::class.java)
        context = this
        selectCuisineBinding.setLifecycleOwner(this)
        selectCuisineViewModel.navigator = this
        selectCuisineBinding.selectCuisine = selectCuisineViewModel

        selectCuisineViewModel.selectCuisine()

        selectCuisineBinding.selectCuisineToolbar.toolbar.setTitle(R.string.select_cuisine)

        selectCuisineBinding.selectCuisineToolbar.toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        getObservable()

        selectCuisineBinding.constraintLayout.visibility = View.GONE

        /* if (intent.hasExtra("product")) {
             selectCuisineBinding.constraintLayout.visibility = View.GONE
         } else {
             selectCuisineBinding.rvCuisineList.visibility = View.GONE
         }*/

        selectCuisineViewModel.cuisineModel.observe(this, Observer {
            var intentPass = Intent(this, ProductCreateActivity::class.java)
            intentPass.putExtra("cuisine_name", "" + it.name)
            intentPass.putExtra("cuisine_id", "" + it.id)
            setResult(Activity.RESULT_OK, intentPass)
            finish()

        })

        selectCuisineBinding.butSave.setOnClickListener(View.OnClickListener {


            /*  if (selectCuisineBinding.radNorth.isChecked) {
                  getNorthIndianFoodtext = selectCuisineBinding.txtNorthIndian.text.toString()
                  cuisineArrayList.add(getNorthIndianFoodtext)

              }
              if (selectCuisineBinding.radSouth.isChecked) {

                  getSouthIndianFoodtext = selectCuisineBinding.txtSouthIndian.text.toString()
                  cuisineArrayList.add(getSouthIndianFoodtext)
              }

              if (selectCuisineBinding.radAmerican.isChecked) {

                  getAmericanFoodtext = selectCuisineBinding.txtAmerican.text.toString()
                  cuisineArrayList.add(getAmericanFoodtext)
              }

              if (selectCuisineBinding.radArabian.isChecked) {

                  getArabianFoodtext = selectCuisineBinding.txtArabian.text.toString()
                  cuisineArrayList.add(getArabianFoodtext)
              }

              if (selectCuisineBinding.radBakers.isChecked) {
                  getBakerstext = selectCuisineBinding.txtBakers.text.toString()
                  cuisineArrayList.add(getBakerstext)
              }
              if (selectCuisineBinding.radAsian.isChecked) {
                  getAsiantext = selectCuisineBinding.txtAsian.text.toString()
                  cuisineArrayList.add(getAsiantext)
              }

              if (selectCuisineBinding.radAfrican.isChecked) {

                  getAfricanFoodtext = selectCuisineBinding.txtAfrican.text.toString()
                  cuisineArrayList.add(getAfricanFoodtext)
              }*/
            //  setCuisineText.putExtra("CuisineList", cuisineArrayList)
            if(selectCuisineViewModel.cuisineIds!=null){
                setCuisineText.putIntegerArrayListExtra(
                    "cuisine_list",
                    selectCuisineViewModel.cuisineIds
                )
                setCuisineText.putStringArrayListExtra(
                    "cuisine_names",
                    selectCuisineViewModel.cuisineNames
                )
            }
            setResult(Activity.RESULT_OK, setCuisineText)
            finish()

        })

    }

    private fun getObservable() {

        selectCuisineViewModel.loadingLiveData.observe(this, Observer<Boolean> {
            if (it) {
                baseLiveDataLoading.value = true
            } else {
                baseLiveDataLoading.value = false
            }
        })

        selectCuisineViewModel.cuisineResponse.observe(this, Observer<CuisineModel> {

            if (it != null && it.statusCode.equals("200")) {

                if (it.responseData != null) {

                    selectCuisineBinding.selectCuisineAdapter = CuisineListAdapter(
                        this,
                        selectCuisineViewModel,
                        it.responseData!!,
                        loadingObservable
                    )

                    for (i in 0 until it.responseData.size) {
                        if (it.responseData[i].id == 1) {
                            val getFirst: String = it.responseData[i].name
                            selectCuisineBinding.txtNorthIndian.setText(getFirst)
                        }

                        if (it.responseData[i].id == 2) {
                            val getSecond: String = it.responseData[i].name
                            selectCuisineBinding.txtSouthIndian.setText(getSecond)
                        }

                        if (it.responseData[i].id == 3) {
                            val getThird: String = it.responseData[i].name
                            selectCuisineBinding.txtAmerican.setText(getThird)
                        }
                        if (it.responseData[i].id == 4) {
                            val getFourth: String = it.responseData[i].name
                            selectCuisineBinding.txtArabian.setText(getFourth)
                        }

                        if (it.responseData[i].id == 5) {
                            val getFifth: String = it.responseData[i].name
                            selectCuisineBinding.txtBakers.setText(getFifth)
                        }
                        if (it.responseData[i].id == 6) {
                            val getSixth: String = it.responseData[i].name
                            selectCuisineBinding.txtAsian.setText(getSixth)
                        }

                        if (it.responseData[i].id == 7) {
                            val getSeventh: String = it.responseData[i].name
                            selectCuisineBinding.txtAfrican.setText(getSeventh)
                        }
                    }
                }
            }

        })

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_select_cuisine
    }
}

