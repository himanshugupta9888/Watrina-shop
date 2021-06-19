package com.watirna.shop.views.edittime

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.watirna.shop.R
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.EditTimingBinding
import com.watirna.shop.datamodel.ShopTimeModel
import com.watirna.shop.interfaces.EditTimingListener
import kotlinx.android.synthetic.main.toolbar_main.view.*

class EditTimeActivity : BaseActivity<EditTimingBinding>() {

    private var TAG: String = "EditTimeActivity"

    private lateinit var activityEditTimeBinding: EditTimingBinding
    private lateinit var editTimeViewModel: EditTimeViewModel

    override fun getLayoutId(): Int {
        return R.layout.edit_timing
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        editTimeViewModel = ViewModelProviders.of(this).get(EditTimeViewModel::class.java)
        activityEditTimeBinding = mViewDataBinding as EditTimingBinding
        activityEditTimeBinding.editTimeActivity = this
        activityEditTimeBinding.lifecycleOwner = this
        activityEditTimeBinding.viewModel = editTimeViewModel

        mViewDataBinding.editimingToolbar.toolbar.title =
            resources.getString(R.string.title_edit_time)
        mViewDataBinding.editimingToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        viewModelObservers()

    }

    private fun viewModelObservers() {

        editTimeViewModel.loadingLiveData.observe(this, Observer {
            loadingObservable.value = it
        })

        editTimeViewModel.errorLiveData.observe(this, Observer {
            if (editTimeViewModel.invalidRequest == it)
                showErrorToast(it)
            else showErrorToast(getString(R.string.resaurant_timing_request_invalid_message))
        })

        editTimeViewModel.saveTimingsResponseLiveData.observe(this, Observer {
            showSuccessToast(getString(R.string.save_restaurant_timing_message))
        })

        // PREPARE DATA
        editTimeViewModel.initialPreparationDays(
            resources.getStringArray(R.array.week_days),
            resources.getStringArray(R.array.all_days)
        )

        editTimeViewModel.setListener(object : EditTimingListener {
            override fun onItemChange(index: Int, isOpenTiming: Boolean) {
                val data = editTimeViewModel.getData()[index]
                val time: String
                time = if (isOpenTiming) {
                    data.store_start_time
                } else {
                    data.store_end_time
                }
                val timeSplit = editTimeViewModel.getTimeArray(time)
                val hour = timeSplit[0].toInt()
                val minute = timeSplit[1].toInt()
                showTimeDialog(hour, minute, index, isOpenTiming, data)

            }
        })

        // LISTENER & LIVE_DATA IMPLEMENTED AFTER FETCH CURRENT DATA
        editTimeViewModel.shopTiming()

    }

    private fun showTimeDialog(
        hour: Int,
        minute: Int,
        index: Int,
        isOpenTiming: Boolean,
        data: ShopTimeModel.ResponseData
    ) {
        val timePicker = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { picker: TimePicker, hours: Int, minutes: Int ->
                data.isChecked = true
                if (isOpenTiming) {
                    data.store_start_time =
                        "${editTimeViewModel.timePad(hours)}:${editTimeViewModel.timePad(minutes)}:00"
                    editTimeViewModel.modifyItem(data, index)
                } else {
                    // CHECK CLOSE TIME IS BIGGER THAN OPEN TIME
                    val timeSplit = editTimeViewModel.getTimeArray(data.store_start_time)
                    val openHours = timeSplit[0].toInt()
                    if (openHours < hours) {
                        data.store_end_time =
                            "${editTimeViewModel.timePad(hours)}:${editTimeViewModel.timePad(minutes)}:00"
                        editTimeViewModel.modifyItem(data, index)
                    } else {
                        showErrorToast(getString(R.string.open_close_time_compare_validation))
                    }
                }

            },
            hour,
            minute,
            true
        )
        val title = data.displayName + " - " + if (isOpenTiming) {
            getString(R.string.open_time)
        } else {
            getString(R.string.close_time)
        }
        timePicker.setMessage(title)

        timePicker.show()

    }


}






