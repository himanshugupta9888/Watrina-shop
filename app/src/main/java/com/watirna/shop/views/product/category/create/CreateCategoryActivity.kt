package com.watirna.shop.views.product.category.create

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityCreateCategoryBinding
import com.watirna.shop.datamodel.addon.KeyValuePair
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

class CreateCategoryActivity : BaseActivity<ActivityCreateCategoryBinding>(),
    CreateCategroyNavigator {

    lateinit var viewModel: CreateCategoryViewModel
    lateinit var mCreateCategoryBinding: ActivityCreateCategoryBinding
    var keyValue = ArrayList<KeyValuePair>()
    private var mCropImageUri: Uri? = null

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mCreateCategoryBinding = mViewDataBinding as ActivityCreateCategoryBinding
        viewModel = ViewModelProviders.of(this).get(CreateCategoryViewModel::class.java)
        mCreateCategoryBinding.createCategoryViewModel = viewModel
        viewModel.navigator = this

        loadingObservable.value = false

        mViewDataBinding.addonHeader.tbr_title.setText(resources.getString(R.string.create_category))
        mViewDataBinding.addonHeader.iv_left.visibility = View.VISIBLE
        mViewDataBinding.addonHeader.iv_left.setOnClickListener(View.OnClickListener {
            setResult(111)
            finish()
        })

        keyValue.add(KeyValuePair("1", "Active"))
        keyValue.add(KeyValuePair("0", "DeActive"))
        setData()

        viewModel.commonSuccessResponse.observe(this, Observer {
            loadingObservable.value = false
            mCropImageUri = null
            viewModel.categoryName.set("")
            viewModel.category_desc.set("")
            setResult(111)
            finish()
            Toasty.success(this, "" + it.message, Toast.LENGTH_SHORT).show()
        })

        viewModel.errorResponse.observe(this, Observer {
            loadingObservable.value = false
            Toasty.error(this, "" + it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun getLayoutId() = R.layout.activity_create_category

    private fun setData() {
        //fill data in spinner
        val adapter: ArrayAdapter<KeyValuePair> = ArrayAdapter<KeyValuePair>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            keyValue
        )
        mCreateCategoryBinding.statusSpinner.adapter = adapter
        mCreateCategoryBinding.statusSpinner.setSelection(adapter.getPosition(keyValue.get(0))) //Optional to set the selected item.
    }

    override fun selectImage() {
        checkPhonePermission()
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
                    mCreateCategoryBinding.imageCategory.setImageURI(result.uri)
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
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setMultiTouchEnabled(true)
            .start(this)
    }

    private fun checkPhonePermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    CropImage.startPickImageActivity(this@CreateCategoryActivity)
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
                    Toasty.error(this@CreateCategoryActivity, "Access Denied", Toast.LENGTH_SHORT)
                        .show()
                    //finish()
                }

            }).check()
    }

    fun validation(): Boolean {
        if (viewModel.categoryName.get().isNullOrEmpty()) {
            Toasty.error(this, " Enter category name", Toast.LENGTH_SHORT).show()
            return false
        } else if (viewModel.category_desc.get().isNullOrEmpty()) {
            Toasty.error(this, " Enter category description", Toast.LENGTH_SHORT).show()
            return false
        } else if (mCropImageUri == null) {
            Toasty.error(this, "Select category image", Toast.LENGTH_SHORT).show()
            return false
        } else
            return true
    }

    override fun createCategoryMethod() {
        if (validation()) {
            loadingObservable.value = true
            val hashMap: HashMap<String, RequestBody> = HashMap()
            hashMap.put(
                "store_category_name",
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    viewModel.categoryName.get().toString()
                )
            )
            hashMap.put(
                "store_category_description",
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    viewModel.categoryName.get().toString()
                )
            )
            hashMap.put(
                "store_category_status",
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    keyValue.get(mCreateCategoryBinding.statusSpinner.selectedItemPosition).key
                )
            )

            if (mCropImageUri?.path != null) {
                val categoryImage = File(mCropImageUri?.path)
                val requestFile = RequestBody.create(
                    "*/*".toMediaTypeOrNull(),
                    categoryImage
                )

                val fileBody =
                    MultipartBody.Part.createFormData("picture", categoryImage.name, requestFile)

                viewModel.createCategoryApi(hashMap, fileBody)
            }
        }
    }

    override fun onBackPressed() {
        setResult(111)
        finish()
    }
}
