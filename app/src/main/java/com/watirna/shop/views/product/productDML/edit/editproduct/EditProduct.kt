package com.watirna.shop.views.product.productDML.edit.editproduct

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityEditProductBinding
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

class EditProduct : BaseActivity<ActivityEditProductBinding>(), EditProductNavigator {

    private lateinit var editProductBinding: ActivityEditProductBinding
    private lateinit var editProductViewModel: EditProductViewModel
    private lateinit var editProductNavigator: EditProductNavigator
    var keyValue = ArrayList<KeyValuePair>()
    var keyValueFoodType = ArrayList<KeyValuePair>()
    var unitArray = ArrayList<KeyValuePair>()

    var categoryId = ""
    var cuisineId = ""
    var addons: ArrayList<Int>? = null
    var addonsPrices: ArrayList<String>? = null

    var productImage: Uri? = null
    var featureProductImage: Uri? = null
    var productPick = false
    var featurePick = false
    var selectedunits = 0

    @Inject
    lateinit var sessionManager: SessionManager

    init {
        AppController.appComponent.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_product
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        editProductBinding = mViewDataBinding as ActivityEditProductBinding
        editProductViewModel = ViewModelProviders.of(this).get(EditProductViewModel::class.java)
        editProductBinding.setLifecycleOwner(this)
        editProductViewModel.navigator = this
        editProductBinding.editProduct = editProductViewModel
        editProductViewModel.getUnits()
        mViewDataBinding.edProTool.tbr_title.setText(R.string.edit_prod)
        mViewDataBinding.edProTool.iv_left.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        keyValue.add(KeyValuePair("1", "Active"))
        keyValue.add(KeyValuePair("0", "DeActive"))
        keyValueFoodType.add(KeyValuePair("0", "Pure Veg"))
        keyValueFoodType.add(KeyValuePair("1", "Non Veg"))
        setData(0, "status")
        setData(0, "veg")

        if (intent.hasExtra("product_id")) {
            editProductViewModel.product_Id = "" + intent.getIntExtra("product_id", 0)
            editProductViewModel.getProductDetails()
        }

        editProductViewModel.categoryItemsModel.observe(this, Observer {
            for(cat in it.responseData?.data!!){
                if(categoryId.equals(""+cat.id,true)){
                    editProductViewModel.categroyString.set(cat.store_category_name)
                    break
                }
            }
        })

        editProductViewModel.editProductReponse.observe(this, Observer {
            editProductViewModel.productName.set(it.responseData.item_name)
            editProductViewModel.descriptionString.set(it.responseData.item_description)
            editProductViewModel.productOrder.set(""+it.responseData.item_order)
            if(it.responseData.quantity != null)
                editProductViewModel.quantity.set(""+it.responseData.quantity)

            for(unit in 0 until unitArray.size) {
                if (it.responseData.unit_id.toString().equals(unitArray.get(unit).key)){
                    setUnits(unit)
                    selectedunits = unit
                } else if (it.responseData.unit_id == null) {
                    editProductBinding.unitSpinner.prompt = "Select Unit"
                }
             }
            //productImage=Uri.parse(it.responseData.picture)

            if(it.responseData.store_category_id !=null){
                categoryId=""+it.responseData.store_category_id
                editProductViewModel.getCategoryList()
            }

            if(it.responseData.status != null){
                if(it.responseData.status == "1")
                    setData(0, "status")
                else
                    setData(1, "status")
            }

            Glide.with(this).load(it.responseData.picture)
                .placeholder(R.drawable.ic_image_upload).dontAnimate()
                .into(editProductBinding.imgUpload);

            for(selected in keyValueFoodType){
                if(selected.value.equals(it.responseData.is_veg)){
                    editProductBinding.foodTypeSpinner.setSelection(selected.key.toInt())
                }
            }

        })

        editProductBinding.edtxtProdCuisine.setOnClickListener() {
            var intent = Intent(this, SelectCuisine::class.java)
            intent.putExtra("product", "true")
            startActivityForResult(intent, 111)
        }

        if (sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME, "")
                .equals("Food", true)
        ) {
            //    mViewDataBinding.edtxtCat.visibility = View.VISIBLE
            editProductBinding.edtxtFoodType.visibility = View.VISIBLE
           // editProductBinding.edtxtProd.visibility = View.VISIBLE
        } else {
            //    mViewDataBinding.edtxtCat.visibility = View.GONE
            editProductBinding.edtxtFoodType.visibility = View.GONE
           // editProductBinding.edtxtProd.visibility = View.GONE
        }

        editProductBinding.tvProductCategory.setOnClickListener {
            var intent = Intent(this, CategorySelectionActivity::class.java)
            startActivityForResult(intent, 112)
        }

        editProductBinding.imgUpload.setOnClickListener {
            productPick = true
            featurePick = false
            checkPhonePermission()
        }

        /*editProductBinding.imgFeatureUpload.setOnClickListener {
            if (editProductBinding.chckYes.isChecked) {
                productPick = false
                featurePick = true
                checkPhonePermission()
            }
        }*/

      /*  if (sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME, "")
                .equals("Food", true)
        ) {
            editProductBinding.edtxtProdCuisine.visibility = View.VISIBLE
        } else {
            editProductBinding.edtxtProdCuisine.visibility = View.GONE
        }
*/
        /*editProductBinding.chckYes.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                editProductBinding.chckNo.isChecked = false
            }
        }

        editProductBinding.chckNo.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                editProductBinding.chckYes.isChecked = false
            }
        }*/

        editProductBinding.btnSave.setOnClickListener {
            if (validation())
                startActivityForResult(Intent(this, PriceSelectionActivity::class.java)
                    .putExtra("edit_product",true)
                    .putParcelableArrayListExtra("addons",editProductViewModel.editProductReponse.value?.responseData?.itemsaddon)
                    .putExtra("item_price",editProductViewModel.editProductReponse.value?.responseData?.item_price)
                    .putExtra("item_discount",editProductViewModel.editProductReponse.value?.responseData?.item_discount)
                    .putExtra("item_discount_type",editProductViewModel.editProductReponse.value?.responseData?.item_discount_type)
                    , 113)
            // else Toasty.error(this, "Validation error", Toast.LENGTH_SHORT).show()
        }

        editProductViewModel.commonSuccessResponse.observe(this, Observer {
            Toasty.success(this, "" + it.message, Toast.LENGTH_SHORT).show()
            setResult(115)
            finish()
        })

        editProductViewModel.unitsModel.observe(this, Observer {
            loadingObservable.value=false
            if(it.responseData != null && it.responseData.size > 0) {
                unitArray.clear()
                unitArray.add(KeyValuePair("0","Select Unit"))
                for (units in it.responseData) {
                    unitArray.add(KeyValuePair(units.id.toString(), units.name))
                }
                setUnits(selectedunits)
            } else {
                unitArray.add(KeyValuePair("0","No units"))
            }

        })

        editProductBinding.unitSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                editProductViewModel.selectedUnit = unitArray[position].key
            }

        }

    }

    override fun validateInputs() {

    }

    private fun setUnits(selected: Int) {
        val adapter: ArrayAdapter<KeyValuePair> = ArrayAdapter<KeyValuePair>(this, android.R.layout.simple_spinner_dropdown_item, unitArray)
        editProductBinding.unitSpinner.adapter = adapter
        editProductBinding.unitSpinner.setSelection(adapter.getPosition(unitArray.get(selected)))
    }

    private fun setData(i: Int, from : String) {
        if(from == "status") {
            val adapter: ArrayAdapter<KeyValuePair> = ArrayAdapter<KeyValuePair>(this, android.R.layout.simple_spinner_dropdown_item, keyValue)
            editProductBinding.statusSpinner.adapter = adapter
            editProductBinding.statusSpinner.setSelection(adapter.getPosition(keyValue.get(i)))
        } else {
            val adapter: ArrayAdapter<KeyValuePair> = ArrayAdapter<KeyValuePair>(this, android.R.layout.simple_spinner_dropdown_item, keyValueFoodType)
            editProductBinding.foodTypeSpinner.adapter = adapter
            editProductBinding.foodTypeSpinner.setSelection(adapter.getPosition(keyValueFoodType.get(i)))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                111 -> {
                /*    editProductViewModel.cuisuieName.set(data?.getStringExtra("cuisine_name"))
                    data?.getStringExtra("cuisine_id")
                    editProductBinding.edtxtProdCuisine.setText(
                        data?.getStringExtra("cuisine_name").toString()
                    )*/

                    editProductViewModel.cuisineIds.clear()

                    if (data?.getIntegerArrayListExtra("cuisine_list") != null) {
                        var cuisineList = data?.getIntegerArrayListExtra("cuisine_list")
                        var cuisineNames = data?.getStringArrayListExtra("cuisine_names")
                        for (cuisine in cuisineList!!) {
                            editProductViewModel.cuisineIds.add(cuisine)
                        }
                        for (cuisineName in cuisineNames!!) {
                            editProductViewModel.cuisineName += "$cuisineName,"
                        }
                        editProductBinding.edtxtProdCuisine.setText(editProductViewModel.cuisineName)
                    }
                }
                112 -> {
                    editProductViewModel.categroyString.set(
                        "" + data?.getStringExtra("category_name").toString()
                    )
                    categoryId = data?.getStringExtra("category_id").toString()
                    editProductBinding.tvProductCategory.setText(
                        data?.getStringExtra("category_name").toString()
                    )
                }
                113 -> {

                    /* data?.getStringExtra("price")
                     data?.getStringExtra("discount")
                     data?.getStringExtra("discount_type")*/
                    var hashMap = HashMap<String, RequestBody>()
                    hashMap.put(
                        "item_name", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            "" + editProductViewModel.productName.get().toString()
                        )
                    )
                    hashMap.put(
                        "item_description", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            "" + editProductViewModel.descriptionString.get()
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
                            "text/plain".toMediaTypeOrNull(), data.getStringExtra("discount_type")!!
                        )
                    )
                     hashMap.put(
                         "_method", RequestBody.create(
                             "text/plain".toMediaTypeOrNull(), "PATCH"
                         )
                     )
                    if (sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME, "")
                            .equals("Food", true)
                    ) {
                        hashMap.put(
                            "is_veg", RequestBody.create(
                                "text/plain".toMediaTypeOrNull(),
                                "" + keyValueFoodType.get(editProductBinding.foodTypeSpinner.selectedItemPosition).value
                            )
                        )
                    }
                    hashMap.put(
                        "status", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            "" + keyValue.get(editProductBinding.statusSpinner.selectedItemPosition).key
                        )
                    )
                    hashMap.put(
                        "quantity", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            "" + editProductViewModel.quantity.get().toString()
                        )
                    )

                    hashMap.put(
                        "unit", RequestBody.create(
                            "text/plain".toMediaTypeOrNull(),
                            "" + editProductViewModel.selectedUnit
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
                                    addonsPrices?.add("0")
                                    addonsPrices?.add(data.getStringArrayListExtra("addonprice")!!.get(p).toString())
                                    p++
                                } else{ addonsPrices?.add(
                                    data.getStringArrayListExtra("addonprice")!!.get(p).toString())
                                    p++ }
                            } else addonsPrices?.add("0")
                        }
                     /*   for (i in data?.getStringArrayListExtra("addonprice")) {
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
                            editProductViewModel.createProductFeature(
                                productImage,
                                fproductImage,
                                hashMap,
                                addons!!,
                                addonsPrices!!
                            )
                        } else {
                            if (addonsPrices == null) {
                                addonsPrices = ArrayList()
                            }
                            if (addons == null) {
                                addons = ArrayList()
                            }
                            editProductViewModel.createProduct(
                                productImage,
                                hashMap,
                                addons!!,
                                addonsPrices!!
                            )
                        }
                    }else{
                        if (addons == null) {
                            addons = ArrayList()
                        }
                        if (addonsPrices == null) {
                            addonsPrices = ArrayList()
                        }
                        editProductViewModel.updateWithouotImage(
                            hashMap,
                            addons!!,
                            addonsPrices!!
                        )
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
                    editProductBinding.imgUpload.setImageURI(result.uri)
                    productImage = result.uri
                } else if (featurePick) {
//                    editProductBinding.imgFeatureUpload.setImageURI(result.uri)
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
                    CropImage.startPickImageActivity(this@EditProduct)
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
                        this@EditProduct,
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
        } else*/ if (editProductViewModel.productName.equals("")) {
            Toasty.error(this, "Enter product name", Toast.LENGTH_SHORT).show()
            return false
        } /*else if (viewModel.categroyString.equals("")) {
            return false
        }*//* else if (viewModel.productCuisine.equals("")) {
            return false
        } else if (editProductViewModel.productOrder.equals("")) {
            Toasty.error(this, "Product order", Toast.LENGTH_SHORT).show()
            return false
        } */ else if (editProductViewModel.descriptionString.equals("")) {
            Toasty.error(this, "Product Description", Toast.LENGTH_SHORT).show()
            return false
        } /*else if (productImage == null) {
            Toasty.error(this, "Product Image", Toast.LENGTH_SHORT).show()
            return false
        } else if (featurePick) {
            if (featureProductImage == null) {
                return false
            }
        }*/
        return true
    }

    override fun showError(errorMsg: String) {
        Toasty.error(this, errorMsg, Toast.LENGTH_SHORT).show()
    }

}
