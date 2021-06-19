package com.watirna.shop.views.dialogs

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.watirna.base.base.BaseDialogFragment
import com.watirna.shop.R
import com.watirna.shop.databinding.TimepickerBinding
import com.watirna.shop.datamodel.ShopTimeModel
import com.watirna.shop.utils.Constants
import com.watirna.shop.views.edittime.EditTimeViewModel
import kotlinx.android.synthetic.main.activity_dash_line_check.view.*
import kotlinx.android.synthetic.main.activity_edit_product.*
import kotlinx.android.synthetic.main.item_everyday.view.*
import kotlinx.android.synthetic.main.timepicker.*
import kotlinx.android.synthetic.main.timepicker.view.*
import java.text.SimpleDateFormat

class ShopTimeDialog : BaseDialogFragment<TimepickerBinding>() {

    private lateinit var timepickerBinding: TimepickerBinding
    private lateinit var editTimeViewModel: EditTimeViewModel
    private var hour: String = ""
    private var minutes: String = ""
    private var seconds: String = ""
    private var openTimeList: ArrayList<String> = ArrayList()
    private var closeTimeList: ArrayList<String> = ArrayList()
    private lateinit var bundle: Bundle
    private lateinit var shopTimeModel: ShopTimeModel
    private var afterConverting: String? = ""


    override fun getLayout(): Int {
        return R.layout.timepicker
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {


        timepickerBinding = viewDataBinding as TimepickerBinding

        editTimeViewModel = ViewModelProviders.of(this).get(EditTimeViewModel::class.java)
        this.shopTimeModel = ShopTimeModel()
        bundle = this!!.arguments!!


        //While changing the time

        timePicker.setOnTimeChangedListener(TimePicker.OnTimeChangedListener {

                view, hourOfDay, minute ->
            var hourofDay = hourOfDay

            var am_pm = ""
            // AM_PM decider logic
            when {
                hourOfDay == 0 -> {
                    hourofDay += 12
                    am_pm = "AM"
                }
                hourOfDay == 12 -> am_pm = "PM"
                hourOfDay > 12 -> {
                    hourofDay -= 12
                    am_pm = "PM"
                }
                else -> am_pm = "AM"
            }
            Toast.makeText(
                activity,
                "Hour " + hourOfDay + " Minute " + minute + " UST : " + am_pm,
                Toast.LENGTH_LONG
            ).show()

            hour = hourOfDay.toString()
            minutes = minute.toString()


        })

        //clicking Cancel Button close the dailog box
        timepickerBinding.butCancel.setOnClickListener(View.OnClickListener {
            dialog!!.cancel()
        })


        //Getting the time after clicking OK button

        timepickerBinding.butOk.setOnClickListener(View.OnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val hour = timepickerBinding.timePicker.hour

                val minutes = timepickerBinding.timePicker.minute

                val from: SimpleDateFormat = SimpleDateFormat("HH:mm")

                val to = SimpleDateFormat("HH:mm")

                afterConverting = to.format(from.parse(hour.toString() + ":" + minutes.toString()))


                if (bundle.containsKey(Constants.EditTime.OpenTime) && (bundle.containsKey(Constants.EditTime.EditTextPosition))) {
                    val openTime: Boolean = bundle!!.getBoolean(Constants.EditTime.OpenTime)
                    val getPosition: Int? = bundle.getInt(Constants.EditTime.EditTextPosition)

                    Toast.makeText(
                        activity,
                        "Position for Open Time " + getPosition,
                        Toast.LENGTH_SHORT
                    ).show()

                    Toast.makeText(
                        activity,
                        "After Converting String " + afterConverting,
                        Toast.LENGTH_SHORT
                    ).show()


                        editTimeViewModel.selectedTime.value?.set(
                            getPosition.toString(),
                            afterConverting!!

                        )

                      //  editTimeViewModel.isOpened.value = openTime

                        dialog!!.dismiss()
                }
                if (bundle.containsKey(Constants.EditTime.CloseTime) && (bundle.containsKey(
                        Constants.EditTime.EditTextPosition
                    ))
                ) {

                    val closeTime: Boolean =
                        arguments!!.getBoolean(Constants.EditTime.CloseTime)
                    val getPosition: Int? = bundle.getInt(Constants.EditTime.EditTextPosition)

                    Toast.makeText(
                        activity,
                        "Position for Close Time " + getPosition,
                        Toast.LENGTH_SHORT
                    ).show()

                    editTimeViewModel.selectedTime.value?.set(
                        getPosition.toString(),
                        afterConverting!!
                    )
                  //  editTimeViewModel.isOpened.value = closeTime

                    dialog!!.dismiss()

                }
            }
        })
    }
}







