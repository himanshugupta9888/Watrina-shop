package com.watirna.shop.views.editrestaurant

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.watirna.app.ui.countrypicker.CountryCodeActivity
import com.watirna.shop.Constants.Constant.CITYLIST_REQUEST_CODE
import com.watirna.shop.Constants.Constant.COUNTRYCODE_PICKER_REQUEST_CODE
import com.watirna.shop.Constants.Constant.COUNTRYLIST_REQUEST_CODE
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityEditRestaurantBinding
import com.watirna.shop.datamodel.City
import com.watirna.shop.datamodel.CountryResponseData
import com.watirna.shop.datamodel.PlacesModel
import com.watirna.shop.datamodel.addon.KeyValuePair
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.utils.PlacesAutocompleteUtil
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.views.adapters.PlaceListAdapter
import com.watirna.shop.views.selectcuisine.SelectCuisine
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_dash_line_check.view.*
import kotlinx.android.synthetic.main.activity_edit_restaurant.*
import kotlinx.android.synthetic.main.activity_edit_restaurant.view.*
import java.util.*
import javax.inject.Inject

class EditRestaurantActivity : BaseActivity<ActivityEditRestaurantBinding>(),
    EditRestaurantNavigator, AdapterView.OnItemClickListener {

    @Inject
    lateinit var commanMethods: CommanMethods

    @Inject
    lateinit var sessionManager: SessionManager

    init {
        AppController.appComponent.inject(this)
    }

    private lateinit var editRestaurantBinding: ActivityEditRestaurantBinding
    private lateinit var editRestaurantViewModel: EditRestaurantViewModel
    private lateinit var context: Context
    private var TAG: String = "EditRestaurantActivity"
    private var mCropImageUri: Uri? = null
    private val SELECT_CUISINE: Int = 1
    private var stringBuilder: StringBuilder = StringBuilder()
    private var getNorthIndianFood: String? = ""
    private var getSouthIndianFood: String? = ""
    private var getAmericanFood: String? = ""
    private var getArabianFood: String? = ""
    private var getBakers: String? = ""
    private var getAsian: String? = ""
    private var getAfrican: String? = ""
    private var countryCode: String = ""
    private var cityList: ArrayList<City> = arrayListOf<City>()
    private var cuisineList: ArrayList<String> = arrayListOf<String>()
    private var cuisine: String = ""
    var StatusValue = ArrayList<KeyValuePair>()
    var ZoneValue = ArrayList<KeyValuePair>()
    var selected: Int = 0
    private lateinit var textWatcher: PlaceTextLisnter
    private lateinit var placesAutocompleteUtil: PlacesAutocompleteUtil
    private var prediction: ArrayList<PlacesModel> = ArrayList()
//    val preferenceHelper = PreferencesHelper(BaseApplication.baseApplication)


    override fun initView(mViewDataBinding: ViewDataBinding?) {
        editRestaurantBinding = mViewDataBinding as ActivityEditRestaurantBinding
        editRestaurantBinding.setLifecycleOwner(this)
        editRestaurantViewModel =
            ViewModelProviders.of(this).get(EditRestaurantViewModel::class.java)
        commanMethods = CommanMethods()
        editRestaurantBinding.editRestaurantViewModel = editRestaurantViewModel
        context = this
        editRestaurantViewModel.navigator = this
        placesAutocompleteUtil = PlacesAutocompleteUtil(this@EditRestaurantActivity)
        textWatcher = PlaceTextLisnter()
        editRestaurantBinding.autoTxtLocation.setText("")
        editRestaurantBinding.autoTxtLocation.isEnabled = false
        editRestaurantViewModel.editRestaurant()
        setDefaultCountry()

        getObservableValues()

        StatusValue.add(KeyValuePair("1", "Active"))
        StatusValue.add(KeyValuePair("0", "Deactive"))

        if (sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME, "").equals("FOOD", true)) {
            mViewDataBinding.editRestaurantToolbar.toolbar.setTitle(R.string.edit_restaurant)
            mViewDataBinding.edtxtCuisine.visibility = View.VISIBLE
            mViewDataBinding.txtPureVeg.visibility = View.VISIBLE
            mViewDataBinding.radioGroup.visibility = View.VISIBLE
        } else {
            mViewDataBinding.editRestaurantToolbar.toolbar.setTitle(R.string.edit_shop)
            mViewDataBinding.edtxtCuisine.visibility = View.GONE
            mViewDataBinding.txtPureVeg.visibility = View.GONE
            mViewDataBinding.radioGroup.visibility = View.GONE
        }

        mViewDataBinding.editRestaurantToolbar.toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        mViewDataBinding.Cuisine.setOnClickListener(View.OnClickListener {
            intent = Intent(context, SelectCuisine::class.java)
            startActivityForResult(intent, SELECT_CUISINE)
        })

        mViewDataBinding.imgUpload.setOnClickListener(View.OnClickListener {
            checkPhonePermission()

        })
        /*mViewDataBinding.edtxtStatus.setOnClickListener(View.OnClickListener {
            val view: View = layoutInflater.inflate(R.layout.custom_status, null)

        })*/

        /*  mViewDataBinding.edtTxtCountryName.setOnClickListener{
             *//* intent = Intent(context, CountryListActivity::class.java)
            startActivityForResult(intent, COUNTRYLIST_REQUEST_CODE)*//*
            editRestaurantViewModel.getCountryList()
        }

        mViewDataBinding.edtTextCity.setOnClickListener{
            if(cityList.size!=0){
                val intent = Intent(this, CityListActivity::class.java)
                intent.putExtra("selectedfrom", "city")
                intent.putExtra("citylistresponse", cityList as Serializable)
                startActivityForResult(intent, CITYLIST_REQUEST_CODE)
            }
        }*/
        editRestaurantViewModel.countryListResponse.observe(this, Observer {
            /*   val intent = Intent(this, CountryListActivity::class.java)
               intent.putExtra("selectedfrom", "country")
               intent.putExtra("countrylistresponse", it as Serializable)
               startActivityForResult(intent, COUNTRYLIST_REQUEST_CODE)*/
            for (country in editRestaurantViewModel.countryListResponse.value?.responseData!!) {
                if (country.id == editRestaurantViewModel.editRestaurantResponse.value?.responseData?.country_id!!) {
                    editRestaurantViewModel.country.value=country.country_name
                    break
                }
            }
        })
        editRestaurantViewModel.commonSuccessResponse.observe(this, Observer {
            Toasty.success(this, "" + it.message, Toast.LENGTH_SHORT).show()
        })

        editRestaurantBinding.statusSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                editRestaurantViewModel.selectedStatus = StatusValue[position].key
            }

        }

        editRestaurantBinding.zoneSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                editRestaurantViewModel.zone.value = ZoneValue[position].value
                editRestaurantViewModel.zoneid = "" + ZoneValue[position].key
            }

        }
        editRestaurantBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val rb = findViewById<View>(checkedId) as RadioButton
            if(rb.text.toString() == "Yes")
                editRestaurantViewModel.is_veg.value = "Pure Veg"
            else
                editRestaurantViewModel.is_veg.value = "Non Veg"
            print("check id : "+rb.text)
        }

        editRestaurantBinding.editLocation.setOnClickListener {
            editRestaurantBinding.autoTxtLocation.isEnabled = true
            editRestaurantViewModel.textWatch = true
            editRestaurantBinding.autoTxtLocation.addTextChangedListener(textWatcher)

        }
    }

    private fun setData(i: Int, from:String) {
        if (from == "status") {
            val adapter: ArrayAdapter<KeyValuePair> = ArrayAdapter<KeyValuePair>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                StatusValue
            )
            editRestaurantBinding.statusSpinner.adapter = adapter
            editRestaurantBinding.statusSpinner.setSelection(adapter.getPosition(StatusValue[i]))
            editRestaurantViewModel.selectedStatus = StatusValue[i].key
        } else if(from == "zone"){
            val adapter: ArrayAdapter<KeyValuePair> = ArrayAdapter<KeyValuePair>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                ZoneValue
            )
            editRestaurantBinding.zoneSpinner.adapter = adapter
            editRestaurantBinding.zoneSpinner.setSelection(adapter.getPosition(ZoneValue[i]))
            editRestaurantViewModel.zoneid = ZoneValue[i].key
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_restaurant
    }


    override fun openCountryPicker() {
        val intent = Intent(this@EditRestaurantActivity, CountryCodeActivity::class.java)
        startActivityForResult(intent, COUNTRYCODE_PICKER_REQUEST_CODE)
    }

    override fun validateInputs(): Boolean {


        if (editRestaurantViewModel.shopName.value.isNullOrEmpty()) {
            val message = resources.getString(R.string.username_empty)
            commanMethods.showToast(context, message, false)
            return false
        }

        if (editRestaurantViewModel.email.value.isNullOrEmpty()) {
            val message = resources.getString(R.string.email_empty)
            commanMethods.showToast(context, message, false)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(editRestaurantViewModel.email.value!!.trim())
                .matches()
        ) {
            val message = resources.getString(R.string.email_invalid)
            commanMethods.showToast(context, message, false)
            return false
        }

        if (editRestaurantViewModel.country_code.value.isNullOrEmpty()) {
            val message = resources.getString(R.string.code_empty)
            commanMethods.showToast(context, message, false)
            return false
        }

        if (editRestaurantViewModel.city.value.isNullOrEmpty()) {
            val message = resources.getString(R.string.city_empty)
            commanMethods.showToast(context, message, false)
            return false
        }

        if (editRestaurantViewModel.country.value.isNullOrEmpty()) {
            val message = resources.getString(R.string.country_empty)
            commanMethods.showToast(context, message, false)
            return false
        }
        if (editRestaurantViewModel.phoneNumber.value.isNullOrEmpty()) {
            val message = resources.getString(R.string.phonenumber_empty)
            commanMethods.showToast(context, message, false)
            return false
        } else if (!Patterns.PHONE.matcher(editRestaurantViewModel.phoneNumber.value)
                .matches() || (commanMethods.isMinLength(
                editRestaurantViewModel.phoneNumber.value.toString(),
                5
            ))
        ) {
            val message = resources.getString(R.string.number_invalid)
            commanMethods.showToast(context, message, false)
            return false
        }
        return true
    }

    override fun showError(errorMsg: String) {
        Toasty.error(this, errorMsg, Toast.LENGTH_SHORT).show()
    }

    fun getObservableValues() {

        editRestaurantViewModel.loadingLiveData.observe(this, Observer {
            baseLiveDataLoading.value = it
        })

        editRestaurantViewModel.editRestaurantResponse.observe(this, Observer {

            try {

                if (it.responseData != null && it.statusCode.equals("200")) {
                    editRestaurantViewModel.getCountryList()

                    editRestaurantViewModel.country_id = "" + it.responseData.country_id!!
                    editRestaurantViewModel.latitude.value = "" + it.responseData.latitude!!
                    editRestaurantViewModel.longitude.value = "" + it.responseData.longitude!!
                    editRestaurantViewModel.status.value = "" + it.responseData.status
                    //  editRestaurantViewModel.contactPerson.value=""+it.responseData.contact_person

                    if (it.responseData!!.country_code != null) {

                        editRestaurantViewModel.country_code.value =
                            it.responseData.country_code.toString()
                    }

                    if (it.responseData!!.store_name != null) {

                        editRestaurantViewModel.shopName.value = it.responseData.store_name
                    }

                    if (it.responseData!!.status != null) {
                        if(it.responseData.status == 0)
                            setData(1,"status")
                        else
                            setData(0,"status")
                    }

                    if (it.responseData!!.email != null) {

                        editRestaurantViewModel.email.value = it.responseData.email
                    }
                    if (it.responseData!!.contact_number != null) {

                        editRestaurantViewModel.phoneNumber.value = it.responseData.contact_number
                    }

                    if (it.responseData!!.city_data.size > 0) {
                        for (city in it.responseData!!.city_data) {
                            if (city.city.id == it.responseData!!.city_id) {
                                editRestaurantViewModel.city.value =
                                    city.city.city_name
                                editRestaurantViewModel.city_id =
                                    "" + city.city.id
                                break
                            }
                        }
                    }
                    if (it.responseData!!.store_gst != null) {
                        editRestaurantViewModel.gst.value = it.responseData.store_gst.toString()
                    }

                    if (it.responseData!!.contact_person != null) {
                        editRestaurantViewModel.contactPerson.value = it.responseData.contact_person
                    }
                    if (it.responseData!!.offer_min_amount != null) {
                        editRestaurantViewModel.minimumAmount.value =
                            it.responseData.offer_min_amount
                    }

                    if (it.responseData!!.offer_percent != null) {
                        editRestaurantViewModel.offer.value =
                            it.responseData.offer_percent.toString()
                    }
                    if (it.responseData!!.store_packing_charges != null) {
                        editRestaurantViewModel.storePackageCharge.value =
                            it.responseData.store_packing_charges.toString()
                    }

                    if (it.responseData!!.store_response_time != null) {
                        editRestaurantViewModel.stroeResponseTime.value =
                            it.responseData.store_response_time.toString()
                    }

                    if (it.responseData!!.commission != null) {
                        editRestaurantViewModel.commission.value =
                            it.responseData.commission.toString()
                    }
                    if (it.responseData!!.estimated_delivery_time != null) {
                        editRestaurantViewModel.maximumDeliveryTime.value =
                            it.responseData.estimated_delivery_time
                    }
                    if (it.responseData!!.description != null) {
                        editRestaurantViewModel.description.value = it.responseData.description
                    }
                    if (it.responseData!!.store_location != null) {
                        editRestaurantViewModel.landMark.value = it.responseData.store_location
                    }
                    if (it.responseData!!.flat_no != null) {
                        editRestaurantViewModel.flatno.value = it.responseData.flat_no
                    }
                    if (it.responseData!!.street != null) {
                        editRestaurantViewModel.street.value = it.responseData.street
                    }

                    if (it.responseData!!.is_veg != null) {
                        editRestaurantBinding.chckYes.isChecked = true
                    } else
                        editRestaurantBinding.chckNo.isChecked = true

                    if (it.responseData!!.store_zipcode != null) {
                        editRestaurantViewModel.zipCode.value = it.responseData.store_zipcode
                    }


                    if (it.responseData.picture != null) {
                        Glide.with(this)
                            .load(it.responseData.picture)
                            .into(editRestaurantBinding.imgUpload)
                    } else {
                        Glide.with(this)
                            .load(R.drawable.ic_profile_place_holder)
                            .into(editRestaurantBinding.imgUpload)
                    }

                    if (it.responseData.cui_selectdata.size > 0) {
                        var cusineNames = ""
                        var cusineIds = ""
                        for (selected in it.responseData.cui_selectdata) {
                            for (cuisine in it.responseData.cuisine_data) {
                                if (selected == cuisine.id) {
                                    editRestaurantViewModel.cuisineIds.add(cuisine.id)
                                    cusineNames = cusineNames + "" + cuisine.name + ","
                                    cusineIds = cusineIds + "" + cuisine.id + ","
                                }
                            }
                        }
                        editRestaurantBinding.Cuisine.setText(cusineNames!!)
                    }

                    if (it.responseData!!.is_veg != null) {
                        editRestaurantViewModel.is_veg.value = it.responseData!!.is_veg!!
                        if(it.responseData!!.is_veg == "Pure Veg") {
                            editRestaurantBinding.chckYes.isChecked = true
                        } else {
                            editRestaurantBinding.chckNo.isChecked = true
                        }
                    }

                    if (it.responseData!!.zone_data.size > 0) {
                        ZoneValue.clear()
                        var i: Int = -1
                        for (zone in it.responseData!!.zone_data) {
                            i++
                            ZoneValue.add(KeyValuePair(zone.id.toString(),zone.name))
                            if (it.responseData!!.zone_id == zone.id) {
                                selected = i
                                editRestaurantViewModel.zone.value = it.responseData.zone_data[0].name
                                editRestaurantViewModel.zoneid = "" + zone.id
                            }
                        }
                    } else {
                        ZoneValue.add(KeyValuePair("0","No units"))
                    }

                    setData(selected,"zone")
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        })
    }


    private fun checkPhonePermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    CropImage.startPickImageActivity(this@EditRestaurantActivity)
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
                    commanMethods.showToast(
                        applicationContext,
                        "Unable to perform this action",
                        false
                    )
                    //finish()
                }

            }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)

            if (resultCode == Activity.RESULT_OK) {

                when (requestCode) {
                    COUNTRYLIST_REQUEST_CODE -> {

                        if (data != null) setCountry(data)
                    }

                    CITYLIST_REQUEST_CODE -> {
                        setCity(data)
                    }
                    COUNTRYCODE_PICKER_REQUEST_CODE -> if (data != null) {

                        setCountryCode(data)

                    }

                }
            }
            if (resultCode != Activity.RESULT_CANCELED) {
                if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                    val imageUri = CropImage.getPickImageResultUri(this, data)

                    // For API >= 23 we need to check specifically that we have permissions to read external storage.
                    if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                        // request permissions and handle the result in onRequestPermissionsResult()
                        mCropImageUri = imageUri
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

                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == Activity.RESULT_OK) {
                        editRestaurantBinding.imgUpload.setImageURI(result.uri)
                        mCropImageUri = result.uri
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Toast.makeText(this, "Cropping failed: ", Toast.LENGTH_LONG).show()
                    }
                }
                if (requestCode == SELECT_CUISINE) {
                    if (resultCode == Activity.RESULT_OK) {


                        if (data!!.getSerializableExtra("CuisineList") != null) {
                            val seperator: String = ","

                            cuisineList =
                                data!!.getSerializableExtra("CuisineList") as ArrayList<String>

                            val stringBuilder = StringBuilder()
                            for (cuisine in cuisineList) {
                                stringBuilder.append(cuisine)
                                stringBuilder.append(seperator)
                            }
                            var afterAppending: String = stringBuilder.toString()
                            //Remove last comma
                            afterAppending = afterAppending.substring(
                                0,
                                afterAppending.length - seperator.length
                            )
                            var length = afterAppending.length - seperator.length


                            //After Appending set text in Edit Text Cuisines
                            editRestaurantBinding.Cuisine.setText(afterAppending)
                        }
                        editRestaurantViewModel.cuisineIds.clear()

                        var cuisineList = data.getIntegerArrayListExtra("cuisine_list")!!
                        var cuisineNames = data.getStringArrayListExtra("cuisine_names")!!
                        for (cuisine in cuisineList) {
                            editRestaurantViewModel.cuisineIds.add(cuisine)
                        }
                        for (cuisineName in cuisineNames) {
                            editRestaurantViewModel.cuisineName += "$cuisineName,"
                        }
                        editRestaurantBinding.Cuisine.setText(editRestaurantViewModel.cuisineName)
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setCountry(data: Intent) {
        val selectedCountry = data.extras?.get("selected_list") as CountryResponseData
        Log.d("countrylist", selectedCountry?.country_name + "")
        editRestaurantBinding.edtTxtCountryName.setText(selectedCountry?.country_name)
        editRestaurantViewModel.country_id = selectedCountry?.id.toString()
        cityList.clear()
        selectedCountry?.city?.let {
            cityList = it as ArrayList<City>
        }
    }

    private fun setCity(data: Intent?) {
        val selectedCity = data?.extras?.get("selected_list") as? City
        Log.d("statelist", selectedCity?.city_name + "")
        editRestaurantBinding.edtTextCity.setText(selectedCity?.city_name)
        editRestaurantViewModel.city_id = selectedCity?.id.toString()
    }

    private fun setCountryCode(data: Intent) {
        editRestaurantBinding.countrycodeRegisterEt.setText(
            data.extras?.get("countryCode").toString()
        )
        val dr = ContextCompat.getDrawable(this, data.extras?.get("countryFlag") as Int)
        //preferenceHelper.setValue(PreferenceKey.COUNTRY_FLAG, data.extras?.get("countryFlag") as Int)
        val bitmap = (dr as BitmapDrawable).bitmap
        var width: Int = 0
        var height: Int = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            width = dpsToPixels(this@EditRestaurantActivity, 8)
            height = dpsToPixels(this@EditRestaurantActivity, 8)
        } else {
            width = dpsToPixels(this@EditRestaurantActivity, 15)
            height = dpsToPixels(this@EditRestaurantActivity, 15)
        }
        val d = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, width, height, true))
        editRestaurantBinding.countrycodeRegisterEt.setCompoundDrawablesWithIntrinsicBounds(
            d,
            null,
            null,
            null
        )
        editRestaurantViewModel.country_code.value = data.extras?.get("countryCode").toString()
        countryCode = data.extras?.get("countryCode").toString()
    }

    private fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
            .setFixAspectRatio(true)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setMultiTouchEnabled(true)
            .start(this)
    }


    fun setDefaultCountry() {
        val dr = ContextCompat.getDrawable(this, R.drawable.flag_india as Int)
        val bitmap = (dr as BitmapDrawable).bitmap
        var width: Int = 0
        var height: Int = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            width = dpsToPixels(this, 8)
            height = dpsToPixels(this, 8)
        } else {
            width = dpsToPixels(this, 15)
            height = dpsToPixels(this, 15)
        }
        val d = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, width, height, true))
        editRestaurantBinding.countrycodeRegisterEt.setCompoundDrawablesWithIntrinsicBounds(
            d,
            null,
            null,
            null
        )
    }

    private fun dpsToPixels(activity: Activity, dps: Int): Int {
        val r = activity.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics
        ).toInt()
    }

    inner class PlaceTextLisnter : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if(count > 2) {
                if(editRestaurantViewModel.textWatch) {
                    placesAutocompleteUtil.findAutocompletePredictions(s.toString().trim(), "IN",
                        object : PlacesAutocompleteUtil.PlaceSuggestion {
                            override fun onPlaceReceived(mPlacesList: ArrayList<PlacesModel>?) {
                                try {
                                    prediction = mPlacesList!!
                                    var adapter =
                                        PlaceListAdapter(this@EditRestaurantActivity, prediction)
                                    editRestaurantBinding.autoTxtLocation.setAdapter(adapter)
                                    adapter.notifyDataSetChanged()
                                    editRestaurantBinding.autoTxtLocation.onItemClickListener =
                                        this@EditRestaurantActivity
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                }
            }

        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (prediction.size > 0) try {
            val mPlace = prediction[position]
            editRestaurantBinding.autoTxtLocation.isEnabled = false
            editRestaurantViewModel.textWatch = false
            editRestaurantBinding.autoTxtLocation.removeTextChangedListener(textWatcher)
            val mLatLng = placesAutocompleteUtil.getPlaceName(mPlace.mPlaceId, object : PlacesAutocompleteUtil.onLoccationListner {
                override fun onLatLngRecived(placeLatLng: LatLng) {
                    if (placeLatLng.latitude != 0.0 && placeLatLng.longitude != 0.0) {
                        editRestaurantBinding.autoTxtLocation.setText(prediction[position].mFullAddress)
                        editRestaurantViewModel.landMark.value = prediction[position].mFullAddress
                        editRestaurantViewModel.latitude.value = placeLatLng.latitude.toString()
                        editRestaurantViewModel.longitude.value = placeLatLng.longitude.toString()
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

