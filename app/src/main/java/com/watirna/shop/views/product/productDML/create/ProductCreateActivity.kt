package com.watirna.shop.views.product.productDML.create

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.*
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityProductCreateBinding
import com.watirna.shop.datamodel.addon.KeyValuePair
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.views.product.categorySelection.CategorySelectionActivity
import com.watirna.shop.views.product.productDML.price.PriceSelectionActivity
import com.watirna.shop.views.selectcuisine.SelectCuisine
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.layout_header.view.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ProductCreateActivity : BaseActivity<ActivityProductCreateBinding>(),
    ProductCreateNavigator {

    lateinit var viewModel: ProductCreateViewModel
    lateinit var productCreateBinding: ActivityProductCreateBinding
    var keyValue = ArrayList<KeyValuePair>()
    var keyValueFoodType = ArrayList<KeyValuePair>()
    var unitArray = ArrayList<KeyValuePair>()
    var categoryId = ""
    var cuisineId = ""
    var addons: ArrayList<Int>? = null
    var addonsPrices: ArrayList<Int>? = null


    @Inject
    lateinit var sessionManager: SessionManager

    init {
        AppController.appComponent.inject(this)
    }

    override fun getLayoutId() = R.layout.activity_product_create
    var productImage: Uri? = null
    var featureProductImage: Uri? = null
    var productPick = false
    var featurePick = false


    override fun initView(mViewDataBinding: ViewDataBinding?) {
        productCreateBinding = mViewDataBinding as ActivityProductCreateBinding
        viewModel = ViewModelProviders.of(this).get(ProductCreateViewModel::class.java)
        productCreateBinding.createProductMainViewModel = viewModel
        viewModel.navigator = this
        viewModel.getUnits()

        productCreateBinding.productHeader.iv_left.setOnClickListener {
            finish()
        }

        productCreateBinding.productHeader.tbr_title.text = "Create Product"

        productCreateBinding.edtxtProdCuisine.setOnClickListener() {
            var intent = Intent(this, SelectCuisine::class.java)
            intent.putExtra("product", "true")
            startActivityForResult(intent, 111)
        }

        if (sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME, "")
                .equals("Food", true)
        ) {
            //    mViewDataBinding.edtxtCat.visibility = View.VISIBLE
            productCreateBinding.edtxtFoodType.visibility = View.VISIBLE
           // productCreateBinding.edtxtProd.visibility = View.VISIBLE
        } else {
            //    mViewDataBinding.edtxtCat.visibility = View.GONE
            productCreateBinding.edtxtFoodType.visibility = View.GONE
           // productCreateBinding.edtxtProd.visibility = View.GONE
        }

        productCreateBinding.tvProductCategory.setOnClickListener {
            var intent = Intent(this, CategorySelectionActivity::class.java)
            startActivityForResult(intent, 112)
        }

        productCreateBinding.imgUpload.setOnClickListener {
            productPick = true
            featurePick = false
            checkPhonePermission()
        }

        /*productCreateBinding.imgFeatureUpload.setOnClickListener {
            if (productCreateBinding.chckYes.isChecked) {
                productPick = false
                featurePick = true
                checkPhonePermission()
            }
        }*/

        keyValue.add(KeyValuePair("1", "Active"))
        keyValue.add(KeyValuePair("0", "DeActive"))
        keyValueFoodType.add(KeyValuePair("0", "Pure Veg"))
        keyValueFoodType.add(KeyValuePair("1", "Non Veg"))
        setData()
        setDataFoodtype()

      /*  if (sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME, "")
                .equals("Food", true)
        ) {
            productCreateBinding.edtxtProdCuisine.visibility = View.VISIBLE
        } else {
            productCreateBinding.edtxtProdCuisine.visibility = View.GONE
        }*/

        /*productCreateBinding.chckYes.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                productCreateBinding.chckNo.isChecked = false
            }
        }

        productCreateBinding.chckNo.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                productCreateBinding.chckYes.isChecked = false
            }
        }*/

        productCreateBinding.btnSave.setOnClickListener {
            if (validation())
                startActivityForResult(Intent(this, PriceSelectionActivity::class.java), 113)
            // else Toasty.error(this, "Validation error", Toast.LENGTH_SHORT).show()
        }

        viewModel.commonSuccessResponse.observe(this, Observer {
            loadingObservable.value=false
            Toasty.success(this, "" + it.message, Toast.LENGTH_SHORT).show()
            setResult(115)
            finish()
        })

        viewModel.unitsModel.observe(this, Observer {
            loadingObservable.value=false
            if(it.responseData != null && it.responseData.size > 0) {
                unitArray.clear()
                unitArray.add(KeyValuePair("0","Select Unit"))
                for (units in it.responseData) {
                    unitArray.add(KeyValuePair(units.id.toString(), units.name))
                }
                setUnits()
            } else {
                unitArray.add(KeyValuePair("0","No units"))
            }

        })

        productCreateBinding.unitSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selectedUnit = unitArray[position].key
            }

        }
    }

    private fun setUnits() {
        val adapter: ArrayAdapter<KeyValuePair> = ArrayAdapter<KeyValuePair>(this, android.R.layout.simple_spinner_dropdown_item, unitArray)
        productCreateBinding.unitSpinner.adapter = adapter
        productCreateBinding.unitSpinner.setSelection(adapter.getPosition(unitArray.get(0)))
    }

    private fun setData() {
        //fill data in spinner
        val adapter: ArrayAdapter<KeyValuePair> = ArrayAdapter<KeyValuePair>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            keyValue
        )
        productCreateBinding.statusSpinner.adapter = adapter
        productCreateBinding.statusSpinner.setSelection(adapter.getPosition(keyValue.get(0))) //Optional to set the selected item.
    }

    private fun setDataFoodtype() {
        //fill data in spinner
        val adapter: ArrayAdapter<KeyValuePair> = ArrayAdapter<KeyValuePair>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            keyValueFoodType
        )
        productCreateBinding.foodTypeSpinner.adapter = adapter
        productCreateBinding.foodTypeSpinner.setSelection(adapter.getPosition(keyValueFoodType.get(0))) //Optional to set the selected item.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                111 -> {
                    /* viewModel.cuisuieName.set(data?.getStringExtra("cuisine_name"))
                     data?.getStringExtra("cuisine_id")
                     productCreateBinding.edtxtProdCuisine.setText(
                         data?.getStringExtra("cuisine_name").toString()
                     )*/

                    viewModel.cuisineIds.clear()

                    if (data?.getIntegerArrayListExtra("cuisine_list") != null) {
                        var cuisineList = data?.getIntegerArrayListExtra("cuisine_list")
                        var cuisineNames = data?.getStringArrayListExtra("cuisine_names")
                        for (cuisine in cuisineList!!) {
                            viewModel.cuisineIds.add(cuisine)
                        }
                        for (cuisineName in cuisineNames!!) {
                            viewModel.cuisineName += "$cuisineName,"
                        }
                        productCreateBinding.edtxtProdCuisine.setText(viewModel.cuisineName)
                    }
                }
                112 -> {
                    viewModel.categroyString.set(
                        "" + data?.getStringExtra("category_name").toString()
                    )
                    categoryId = data?.getStringExtra("category_id").toString()
                    productCreateBinding.tvProductCategory.setText(
                        data?.getStringExtra("category_name").toString()
                    )
                }
                113 -> {
                    loadingObservable.value=true

                    /* data?.getStringExtra("price")
                     data?.getStringExtra("discount")
                     data?.getStringExtra("discount_type")*/
                    var hashMap = HashMap<String, RequestBody>()
                    hashMap.put(
                        "item_name", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            "" + viewModel.productName.get().toString()
                        )
                    )

                    hashMap.put(
                        "item_description", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(), "" + viewModel.descriptionString.get()
                        )
                    )
                    hashMap.put(
                        "store_category_id", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(), categoryId
                        )
                    )
                    /*  if (sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME, "")
                              .equals("Food", true)
                      ) {

                      }*/
                    hashMap.put(
                        "item_price", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(), "" + data?.getStringExtra("price")
                        )
                    )
                    hashMap.put(
                        "item_discount", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(), data?.getStringExtra("discount")!!
                        )
                    )
                    hashMap.put(
                        "item_discount_type", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(), data?.getStringExtra("discount_type")!!
                        )
                    )

                    hashMap.put(
                        "quantity", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            "" + viewModel.quantity.get().toString()
                        )
                    )

                    hashMap.put(
                        "unit", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            "" + viewModel.selectedUnit
                        )
                    )
                    /* hashMap.put(
                         "_method", RequestBody.create(
                             MediaType.parse("text/plain"), "PATCH"
                         )
                     )*/
                    if (sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME, "")
                            .equals("Food", true)
                    ) {
                        hashMap.put(
                            "is_veg", RequestBody.create(
                                "text/plain".toMediaTypeOrNull(),
                                "" + keyValueFoodType.get(productCreateBinding.foodTypeSpinner.selectedItemPosition).value
                            )
                        )
                    }
                    hashMap.put(
                        "status", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            "" + keyValue.get(productCreateBinding.statusSpinner.selectedItemPosition).key
                        )
                    )


                    if (data?.getStringArrayListExtra("addonid") != null) {
                        addons = ArrayList()
                        var ids = ""
                        var p = 0
                        for (i in data.getStringArrayListExtra("addonid")!!) {
                            ids += "$i,"
                            addons?.add(i.toInt())
                            // arryIdString.add("$p=>'$i'")
                            p++
                        }

                        /* if (data?.getStringArrayListExtra("addonid").size > 0) {
                             hashMap.put(
                                 "addon[]",
                                 RequestBody.create(
                                     MediaType.parse("text/plain"),
                                     ids.substring(0, ids.length - 1)
                                 )
                             )
                         }*/

                        /*  hashMap.put(
                              "addon[]",
                              RequestBody.create(
                                  MediaType.parse("multipart/form-data"), // notice I'm using "multipart/form-data"
                                  jsonObject.getString("addon")
                              )
                          )*/
                    }
                    if (data?.getStringArrayListExtra("addonprice") != null) {
                        addonsPrices = ArrayList()
                        var duplicate = ArrayList<Int>()
                        var prices = ""
                        var p = 0
                        var f = false
                        for (a in 1..Collections.max(addons!!)) {
                            if (addons?.contains(a)!!) {
                                if (!f) {
                                    f = true
                                    addonsPrices?.add(0)
                                    addonsPrices?.add(data.getStringArrayListExtra("addonprice")!!.get(p).toInt())
                                    p++
                                } else{ addonsPrices?.add(data.getStringArrayListExtra("addonprice")!!.get(p).toInt())
                                    p++ }
                            } else addonsPrices?.add(0)
                        }


                        //removeDuplicates(duplicate)?.let { addonsPrices?.addAll(it) }


                        /* for (i in data?.getStringArrayListExtra("addonprice")) {
                             prices += "$i,"

                             for (a in 1..addons?.get(p)!!.plus(1)) {
                                 if (a == addons?.get(p)!!) {
                                     addonsPrices?.add(0)
                                     addonsPrices?.add(i.toInt())
                                 } else addonsPrices?.add(0)
                             }
                             //arryIdString.add("$p=>$i")
                             p++
                         }*/
                        /*hashMap.put(
                            "addon_price" + data?.getStringArrayListExtra("addonid"),
                            RequestBody.create(
                                MediaType.parse("text/plain"),
                                prices.substring(0, prices.length - 1)
                            )
                        )*/


                        /*    hashMap.put(
                                "addon_price[]",
                                RequestBody.create(
                                    MediaType.parse("multipart/form-data"), // notice I'm using "multipart/form-data"
                                    jsonObject.getString("addon_price")
                                )
                            )*/
                    }

                    /* if (jsonObject.has("addon")) {
                         hashMap.put(
                             "addon_price" + data?.getStringArrayListExtra("addonid"),
                             RequestBody.create(
                                 MediaType.parse("multipart/form-data"), // notice I'm using "multipart/form-data"
                                 jsonObject.toString()
                             )
                         )
                     }*/

                    if (productImage?.path != null) {
                        val categoryImage = File(productImage?.path)
                        val requestFile = RequestBody.create(
                            "*/*".toMediaTypeOrNull(),
                            categoryImage
                        )

                        val productImage =
                            MultipartBody.Part.createFormData(
                                "picture",
                                categoryImage.name,
                                requestFile
                            )

                        if (featureProductImage?.path != null) {
                            val categoryImage = File(featureProductImage?.path)
                            val requestFile = RequestBody.create(
                                "*/*".toMediaTypeOrNull(),
                                categoryImage
                            )

                            val fproductImage =
                                MultipartBody.Part.createFormData(
                                    "picture",
                                    categoryImage.name,
                                    requestFile
                                )
                            if (addons == null) {
                                addons = ArrayList()
                            }
                            if (addonsPrices == null) {
                                addonsPrices = ArrayList()
                            }
                            viewModel.createProductFeature(
                                productImage,
                                fproductImage,
                                hashMap,
                                addons!!,
                                addonsPrices!!,viewModel.cuisineIds)
                        } else {
                            if (addonsPrices == null) {
                                addonsPrices = ArrayList()
                            }
                            if (addons == null) {
                                addons = ArrayList()
                            }
                            viewModel.createProduct(productImage, hashMap, addons!!, addonsPrices!!,viewModel.cuisineIds)
                        }
                    }

                }
            }
        }

        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                val imageUri = CropImage.getPickImageResultUri(this, data)

                // For API >= 23 we need to check specifically that we have permissions to read external storage.
                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    if (productPick)
                        productImage = imageUri
                    else if (featurePick) productImage = imageUri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            0
                        )
                    }
                } else {
                    // no permissions required or already grunted, can start crop image activity
                    startCropImageActivity(imageUri)
                }

            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                if (productPick) {
                    productCreateBinding.imgUpload.setImageURI(result.uri)
                    productImage = result.uri
                } else if (featurePick) {
//                    productCreateBinding.imgFeatureUpload.setImageURI(result.uri)
                    featureProductImage = result.uri
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: ", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
            .setFixAspectRatio(true)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setMultiTouchEnabled(true)
            .start(this)
    }

    private fun checkPhonePermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    CropImage.startPickImageActivity(this@ProductCreateActivity)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest?,
                    token: PermissionToken?
                ) {
                    //close activity
                    token?.continuePermissionRequest()
                }


                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    //close activity
                    Toast.makeText(
                        this@ProductCreateActivity,
                        "Unable to perform this action",
                        Toast.LENGTH_SHORT
                    ).show()
                    //finish()
                }

            }).check()
    }

    fun validation(): Boolean {

        /*if (viewModel.price.get().equals("")) {
            return false
        } else*/ if (viewModel.productName.get().equals("")) {
            Toasty.error(this, "Enter product name", Toast.LENGTH_SHORT).show()
            return false
        } else if (categoryId.equals("")) {
            Toasty.error(this, "Please select any Category", Toast.LENGTH_SHORT).show()
            return false
        }/* else if (viewModel.productCuisine.equals("")) {
            return false
        }  else if (viewModel.productOrder.equals("")) {
            Toasty.error(this, "Product order", Toast.LENGTH_SHORT).show()
            return false
        } */else if (viewModel.descriptionString.get().equals("")) {
            Toasty.error(this, "Product Description", Toast.LENGTH_SHORT).show()
            return false
        } else if (viewModel.quantity.get().toString() == "") {
            Toasty.error(this, "Please enter quantity", Toast.LENGTH_SHORT).show()
            return false
        } else if (productImage == null) {
            Toasty.error(this, "Product Image", Toast.LENGTH_SHORT).show()
            return false
        } else if (featurePick) {
            if (featureProductImage == null) {
                return false
            }
        }
        return true
    }

    override fun showError(errorMsg: String) {
        Toasty.error(this, errorMsg, Toast.LENGTH_SHORT).show()
    }

}