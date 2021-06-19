package com.watirna.shop.views.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.watirna.app.ui.countrypicker.CountryCodeActivity
import com.watirna.shop.Constants.Constant.COUNTRYCODE_PICKER_REQUEST_CODE
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityProfileBinding
import com.watirna.shop.datamodel.ProfileModel
import com.watirna.shop.utils.CommanMethods
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.toolbar_main.view.*
import java.lang.Exception
import javax.inject.Inject


class ProfileActivity : BaseActivity<ActivityProfileBinding>(), ProfileNavigator {

    @Inject
    lateinit var commanMethods: CommanMethods

    private lateinit var profileBinding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var context: Context
    private var TAG = "ProfileActivity"
    private var mCropImageUri: Uri? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_profile
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        profileBinding = mViewDataBinding as ActivityProfileBinding
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        profileBinding.setLifecycleOwner(this)
        context = this
        profileViewModel.navigator = this
        commanMethods = CommanMethods()
        mViewDataBinding.profileToolBar.toolbar.setTitle(R.string.title_Profile)
        mViewDataBinding.profileToolBar.toolbar.setNavigationOnClickListener({
            onBackPressed()
        })
        profileBinding.profileViewModel = profileViewModel

        profileViewModel.profile()

        getObservableValues()

        profileBinding.circleImg.setOnClickListener(View.OnClickListener {
            checkPhonePermission()
        })
    }


    override fun validateProfileInputs(): Boolean {


        if (profileViewModel.userName.value.isNullOrEmpty()) {
            val message = resources.getString(R.string.username_empty)
            commanMethods.showToast(context, message, false)
            return false
        }

        if (profileViewModel.email.value.isNullOrEmpty()) {
            val message = resources.getString(R.string.email_empty)
            commanMethods.showToast(context, message, false)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(profileViewModel.email.value!!.trim())
                .matches()
        ) {
            val message = resources.getString(R.string.email_invalid)
            commanMethods.showToast(context, message, false)
            return false
        }

        if (profileViewModel.country_code.value.isNullOrEmpty()) {
            val message = resources.getString(R.string.code_empty)
            commanMethods.showToast(context, message, false)
            return false
        }

        if (profileViewModel.city.value.isNullOrEmpty()) {
            val message = resources.getString(R.string.city_empty)
            commanMethods.showToast(context, message, false)
            return false
        }

        if (profileViewModel.country.value.isNullOrEmpty()) {
            val message = resources.getString(R.string.country_empty)
            commanMethods.showToast(context, message, false)
            return false
        }
        if (profileViewModel.phoneNumber.value.isNullOrEmpty()) {
            val message = resources.getString(R.string.phonenumber_empty)
            commanMethods.showToast(context, message, false)
            return false
        } else if (!Patterns.PHONE.matcher(profileViewModel.phoneNumber.value)
                .matches() || (commanMethods.isMinLength(
                profileViewModel.phoneNumber.value.toString(),
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
        commanMethods.showToast(context, errorMsg, false)
    }


    override fun openCountryPicker() {
        val intent = Intent(this, CountryCodeActivity::class.java)
        startActivityForResult(intent, COUNTRYCODE_PICKER_REQUEST_CODE)
    }

    override fun selectProfileImage() {
        TODO("Not yet implemented")
    }


    private fun checkPhonePermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    CropImage.startPickImageActivity(this@ProfileActivity)
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
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                val imageUri = CropImage.getPickImageResultUri(this, data)

                // For API >= 23 we need to check specifically that we have permissions to read external storage.
                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    mCropImageUri = imageUri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                    }
                } else {
                    // no permissions required or already grunted, can start crop image activity
                    startCropImageActivity(imageUri)
                }

            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    profileBinding.circleImg.setImageURI(result.uri)
                    mCropImageUri = result.uri
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
            .setFixAspectRatio(true)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setMultiTouchEnabled(true)
            .start(this)
    }


    private fun getObservableValues() {

        profileViewModel.loadingLiveData.observe(this, Observer<Boolean> {
            if (it) {
                baseLiveDataLoading.value = true
            } else {
                baseLiveDataLoading.value = false
            }
        })

        profileViewModel.profileResponse.observe(this, Observer<ProfileModel> {
            try {

                if (it != null && it.statusCode.equals("200")) {
                    if (it.responseData!!.country_code!=null) {

                        profileViewModel.country_code.value =
                            it.responseData.country_code.toString()
                    }

                    if (it.responseData!!.store_name != null) {

                        profileViewModel.userName.value = it.responseData.store_name
                    }
                    if (it.responseData!!.email != null) {

                        profileViewModel.email.value = it.responseData.email
                    }
                    if (it.responseData!!.contact_number != null) {

                        profileViewModel.phoneNumber.value = it.responseData.contact_number
                    }
                    if (it.responseData!!.city_data[0].city.city_name != null) {
                        profileViewModel.city.value = it.responseData!!.city_data[0].city.city_name
                    }
                    if (it.responseData.picture != null) {
                        Glide.with(this@ProfileActivity)
                            .load(it.responseData.picture)
                            .into(profileBinding.circleImg)
                    } else {
                        Glide.with(this@ProfileActivity)
                            .load(R.drawable.ic_profile_place_holder)
                            .into(profileBinding.circleImg)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        })
    }
}


