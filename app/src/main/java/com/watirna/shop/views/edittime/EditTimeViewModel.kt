package com.watirna.shop.views.edittime

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.watirna.shop.application.AppController
import com.watirna.shop.datamodel.ShopTimeModel
import com.watirna.shop.interfaces.ApiListener
import com.watirna.shop.interfaces.EditTimingListener
import com.watirna.shop.repository.ShopRepository
import com.watirna.shop.datamodel.request.EditTimingRequest
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.views.adapters.RestaurantTimingAdapter
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class EditTimeViewModel : ViewModel() {

    @Inject
    lateinit var shopRepository: ShopRepository

    @Inject
    lateinit var commonMethods: CommanMethods

    private var timings: ArrayList<ShopTimeModel.ResponseData> = ArrayList()
    private var weekList: ArrayList<ShopTimeModel.ResponseData> = ArrayList()
    private var allDay: ArrayList<ShopTimeModel.ResponseData> = ArrayList()

    var isEveryDay = MutableLiveData(true)

    var selectedTime = MutableLiveData<HashMap<String, String>>()

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    var shopTimingResponse = MutableLiveData<ShopTimeModel>()
    var loadingLiveData = MutableLiveData<Boolean>()
    var errorLiveData = MutableLiveData<String>()
    var saveTimingsResponseLiveData = MutableLiveData<Boolean>()

    private var mAdapter: RestaurantTimingAdapter
    private lateinit var editTimingListener: EditTimingListener

    val invalidRequest = "INVALID"


    init {
        AppController.appComponent.inject(this)
        mAdapter = RestaurantTimingAdapter(this)
    }


    fun shopTiming() {
        loadingLiveData.value = true
        val params = HashMap<String, String>()

        compositeDisposable.add(shopRepository.getShopTiming(params, object : ApiListener {
            override fun success(successData: Any) {
                shopTimingResponse.value = successData as ShopTimeModel
                prepareAdapterData()
            }

            override fun fail(failData: Throwable) {
                errorLiveData.value = commonMethods.getErrorMessage(failData)
                loadingLiveData.value = false
            }
        }))
    }

    private fun prepareAdapterData() {

        val data: List<ShopTimeModel.ResponseData>? = shopTimingResponse.value?.responseData
        if (data != null && data.isNotEmpty()) {
            if (data[0].store_day.equals("ALL", true)) {
                data[0].isChecked = true
                data[0].displayName = allDay[0].displayName
                timings = data as ArrayList<ShopTimeModel.ResponseData>
                allDay = data

            } else {

                for (resItem: ShopTimeModel.ResponseData in data) {
                    when (resItem.store_day) {
                        "SUN" -> {
                            weekList[0].store_id = resItem.store_id
                            weekList[0].store_day = resItem.store_day
                            weekList[0].store_start_time = resItem.store_start_time
                            weekList[0].store_end_time = resItem.store_end_time
                            if(resItem.store_end_time != "00:00:00" || resItem.store_start_time!="00:00:00")
                            weekList[0].isChecked = true
                        }
                        "MON" -> {
                            weekList[1].store_id = resItem.store_id
                            weekList[1].store_day = resItem.store_day
                            weekList[1].store_start_time = resItem.store_start_time
                            weekList[1].store_end_time = resItem.store_end_time
                            if(resItem.store_end_time != "00:00:00" || resItem.store_start_time!="00:00:00")
                            weekList[1].isChecked = true
                        }
                        "TUE" -> {
                            weekList[2].store_id = resItem.store_id
                            weekList[2].store_day = resItem.store_day
                            weekList[2].store_start_time = resItem.store_start_time
                            weekList[2].store_end_time = resItem.store_end_time
                            if(resItem.store_end_time != "00:00:00" || resItem.store_start_time!="00:00:00")
                            weekList[2].isChecked = true
                        }
                        "WED" -> {
                            weekList[3].store_id = resItem.store_id
                            weekList[3].store_day = resItem.store_day
                            weekList[3].store_start_time = resItem.store_start_time
                            weekList[3].store_end_time = resItem.store_end_time
                            if(resItem.store_end_time != "00:00:00" || resItem.store_start_time!="00:00:00")
                            weekList[3].isChecked = true
                        }
                        "THU" -> {
                            weekList[4].store_id = resItem.store_id
                            weekList[4].store_day = resItem.store_day
                            weekList[4].store_start_time = resItem.store_start_time
                            weekList[4].store_end_time = resItem.store_end_time
                            if(resItem.store_end_time != "00:00:00" || resItem.store_start_time!="00:00:00")
                            weekList[4].isChecked = true
                        }
                        "FRI" -> {
                            weekList[5].store_id = resItem.store_id
                            weekList[5].store_day = resItem.store_day
                            weekList[5].store_start_time = resItem.store_start_time
                            weekList[5].store_end_time = resItem.store_end_time
                            if(resItem.store_end_time != "00:00:00" || resItem.store_start_time!="00:00:00")
                            weekList[5].isChecked = true
                        }
                        "SAT" -> {
                            weekList[6].store_id = resItem.store_id
                            weekList[6].store_day = resItem.store_day
                            weekList[6].store_start_time = resItem.store_start_time
                            weekList[6].store_end_time = resItem.store_end_time
                            if(resItem.store_end_time != "00:00:00" || resItem.store_start_time!="00:00:00")
                            weekList[6].isChecked = true
                        }
                    }
                }
                timings = weekList
                isEveryDay.value = false

            }
        } else {
            timings = allDay
            isEveryDay.value = true
        }
        notifyAdapter()
        loadingLiveData.value = false
    }


    fun getData(): List<ShopTimeModel.ResponseData> {
        return timings
    }

    fun getAdapter(): RestaurantTimingAdapter {
        return mAdapter
    }

    private fun notifyAdapter() {
        mAdapter.notifyDataSetChanged()
    }

    fun modifyItem(item: ShopTimeModel.ResponseData, index: Int) {
        timings[index] = item
        mAdapter.notifyItemChanged(index)
    }

    fun initialPreparationDays(weekDays: Array<String>, all: Array<String>) {
        for (day: String in weekDays) {
            weekList.add(
                ShopTimeModel.ResponseData(
                    0,
                    day.substring(0, 3).toUpperCase(Locale.ENGLISH),
                    "00:00:00",
                    0,
                    "00:00:00",
                    false,
                    day,
                    ""
                )
            )
        }

        allDay.add(
            ShopTimeModel.ResponseData(
                0,
                "ALL",
                "00:00:00",
                0,
                "00:00:00",
                false,
                all[0],
                ""
            )
        )
    }

    fun onSwitchChangeListener(isChecked: Boolean) {
        timings = if (isChecked) {
            allDay[0].isChecked = true
            allDay
        } else {
            weekList
        }
        notifyAdapter()
    }

    fun onCheckedChange(index: Int) {
        val item = timings[index]
        if (item.isChecked) {
            item.isChecked = false
            item.store_start_time = "00:00:00"
            item.store_end_time = "00:00:00"
            modifyItem(item, index)
        } else {
            item.isChecked = true
            editTimingListener.onItemChange(index, true)

        }
    }

    fun onOpenCloseTimingClick(index: Int, isOpenTiming: Boolean) {
        editTimingListener.onItemChange(index, isOpenTiming)
    }

    fun setListener(editTimingListener: EditTimingListener) {
        this.editTimingListener = editTimingListener
    }

    fun getTimeArray(time: String): List<String> {
        return time.split(":")
    }

    fun timePad(input: Int): String? {
        return if (input >= 10) {
            input.toString()
        } else {
            "0$input"
        }
    }

    fun onSaveRestaurantTimingsClick() {
        loadingLiveData.value = true
        var isRequestValid = true

        val dayList = ArrayList<String>()
        val openTimingsMap = HashMap<String, String>()
        val closeTimingsMap = HashMap<String, String>()

        for (item: ShopTimeModel.ResponseData in timings) {
            if ((item.isChecked && item.store_end_time == "00:00:00")) {
                if (isRequestValid)
                    isRequestValid = false

                item.error = invalidRequest
            } else {
                item.error = ""
                openTimingsMap[item.store_day] = item.store_start_time
                closeTimingsMap[item.store_day] = item.store_end_time
                dayList.add(item.store_day)
            }
        }



        if (isRequestValid) {
            val req = EditTimingRequest(
                dayList = dayList,
                openingHoursList = openTimingsMap,
                closingHoursList = closeTimingsMap
            )
            saveTimings(req)
        } else {
            loadingLiveData.value = false
            errorLiveData.value = invalidRequest
            notifyAdapter()
        }
    }

    private fun saveTimings(request: EditTimingRequest) {
        compositeDisposable.add(shopRepository.saveShopTiming(request, object : ApiListener {
            override fun success(successData: Any) {
                val res = successData as ShopTimeModel
                if ((res.statusCode== "200"))
                    saveTimingsResponseLiveData.value = true
                else errorLiveData.value = res.message
                loadingLiveData.value = false
            }

            override fun fail(failData: Throwable) {
                errorLiveData.value = commonMethods.getErrorMessage(failData)
                loadingLiveData.value = false
            }
        }))

    }
}