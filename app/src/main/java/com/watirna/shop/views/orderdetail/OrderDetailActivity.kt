package com.watirna.shop.views.orderdetail

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityHistoryDetailBinding
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.utils.Constants
import com.watirna.shop.views.adapters.HistoryMainAdapter
import com.watirna.shop.views.adapters.OrderListHistoryDetailsAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.invoice.view.*
import kotlinx.android.synthetic.main.toolbar_main.view.*
import java.util.*
import javax.inject.Inject

class OrderDetailActivity : BaseActivity<ActivityHistoryDetailBinding>() {
    private lateinit var activityHistoryDetailBinding: ActivityHistoryDetailBinding
    private lateinit var orderDetailViewModel: OrderDetailViewModel
    private  lateinit var  linearLayoutManager: LinearLayoutManager
    private  lateinit var  linearLayoutManager1: LinearLayoutManager

    private  lateinit var  historyMainAdapter: HistoryMainAdapter
    private var storeId: String = ""
    private var Id: String = ""
    private var Status: String = "PICKEDUP"
    private var orderStatusDetailID: String = ""
    private var responseStatus: String = ""
    private var FROM: String = ""
    private var count: Int = 5
    private var checkRequestTimer: Timer? = null

    @Inject
    lateinit var  commanMethods: CommanMethods

    init {
        AppController.appComponent.inject(this)
    }
    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityHistoryDetailBinding = mViewDataBinding as ActivityHistoryDetailBinding
        orderDetailViewModel = ViewModelProviders.of(this).get(OrderDetailViewModel::class.java)
        checkRequestTimer = Timer()
        mViewDataBinding.editimingToolbar.toolbar.title =
            resources.getString(R.string.title_order_detail)
        mViewDataBinding.editimingToolbar.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        //Get order Detail

        getIntentValue()

        //Get observe Values
        getObserverValues()

        activityHistoryDetailBinding.btnTakeAway.setOnClickListener {
            orderDetailViewModel.callTakeAway(Id, storeId, Status)
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_history_detail
    }

    fun getIntentValue(){
        orderStatusDetailID= if(intent!=null && intent.hasExtra("orderStatusDetailID")) intent.getStringExtra("orderStatusDetailID")!! else ""
        if(intent!=null && intent.hasExtra("from"))
            FROM = intent.getStringExtra("from")!!
        if(!orderStatusDetailID.isNullOrEmpty()) orderDetailViewModel.orderID.value = Integer.valueOf(orderStatusDetailID)
            getOrderDetail()
    }

    fun getOrderDetail() {
        checkRequestTimer!!.schedule(object : TimerTask() {
            override fun run() {
                orderDetailViewModel.callOrderDetail()
            }
        }, 0, 5000)
    }


    fun initalizieAdapter() {
        linearLayoutManager= LinearLayoutManager(this)
        activityHistoryDetailBinding.rvShopStatus.layoutManager= linearLayoutManager
        if(orderDetailViewModel.orderDetailResponse.value!!.responseData!!.order_type == "TAKEAWAY")
            count = 3
        else
            count = 5
        if(orderDetailViewModel.orderDetailResponse.value!=null)
        historyMainAdapter = HistoryMainAdapter(this,orderDetailViewModel.orderDetailResponse.value!!.responseData!!, count)
        activityHistoryDetailBinding.rvShopStatus.adapter =historyMainAdapter
        activityHistoryDetailBinding.rvShopStatus.adapter!!.notifyDataSetChanged()
        linearLayoutManager1= LinearLayoutManager(this)
        activityHistoryDetailBinding.rvOrderDetail.layoutManager= linearLayoutManager1

        Id = orderDetailViewModel.orderDetailResponse.value!!.responseData!!.id.toString()
        storeId = orderDetailViewModel.orderDetailResponse.value!!.responseData!!.store_id.toString()
        responseStatus = orderDetailViewModel.orderDetailResponse.value!!.responseData!!.status.toString()

        val orderListHistoryDetailsAdapter:OrderListHistoryDetailsAdapter= OrderListHistoryDetailsAdapter(this,orderDetailViewModel.orderDetailResponse.value!!.responseData!!.order_invoice!!.items)
        activityHistoryDetailBinding.rvOrderDetail.adapter= orderListHistoryDetailsAdapter
        activityHistoryDetailBinding.rvOrderDetail.adapter!!.notifyDataSetChanged()

        activityHistoryDetailBinding.llitemList.visibility = View.VISIBLE
        activityHistoryDetailBinding.llitemListTopic.visibility = View.VISIBLE


        activityHistoryDetailBinding.layInvoice.tvOrderSubTotal.setText(commanMethods.getNumberFormat()!!.format(orderDetailViewModel.orderDetailResponse.value!!.responseData!!.order_invoice!!.item_price))
        activityHistoryDetailBinding.layInvoice.tvOrderDeliveryCharge.setText(commanMethods.getNumberFormat()!!.format(orderDetailViewModel.orderDetailResponse.value!!.responseData!!.order_invoice!!.delivery_amount))
        activityHistoryDetailBinding.layInvoice.tvOrderPromoAmt.setText(" - " + commanMethods.getNumberFormat()!!.format(orderDetailViewModel.orderDetailResponse.value!!.responseData!!.order_invoice!!.promocode_amount))
        activityHistoryDetailBinding.layInvoice.tvOrderShopDiscount.setText(" - " + commanMethods.getNumberFormat()!!.format(orderDetailViewModel.orderDetailResponse.value!!.responseData!!.order_invoice!!.discount))
        activityHistoryDetailBinding.layInvoice.tvPackingCharge.setText(commanMethods.getNumberFormat()!!.format(orderDetailViewModel.orderDetailResponse.value!!.responseData!!.order_invoice!!.store_package_amount))
        activityHistoryDetailBinding.layInvoice.tvServiceTax.setText(commanMethods.getNumberFormat()!!.format(orderDetailViewModel.orderDetailResponse.value!!.responseData!!.order_invoice!!.tax_amount))
        activityHistoryDetailBinding.layInvoice.tvOrderTotal.setText(commanMethods.getNumberFormat()!!.format(orderDetailViewModel.orderDetailResponse.value!!.responseData!!.order_invoice!!.total_amount))
        activityHistoryDetailBinding.layInvoice.tvOrderTotal.setTextColor(resources.getColor(R.color.blue))
        activityHistoryDetailBinding.layInvoice.total_tittle_tv.setTextColor(resources.getColor(R.color.blue))
        activityHistoryDetailBinding.llinvoiceTopic.visibility = View.VISIBLE

        if(FROM == Constants.WebConstants.ONGOING && orderDetailViewModel.orderDetailResponse.value!!.responseData!!.order_type == "TAKEAWAY") {
            activityHistoryDetailBinding.btnTakeAway.visibility = View.VISIBLE
            if(responseStatus == "RECEIVED") {
                activityHistoryDetailBinding.btnTakeAway.text = "Order Ready"
                Status = "PICKEDUP"
            } else {
                activityHistoryDetailBinding.btnTakeAway.text = "Take Away"
                Status = "COMPLETED"
            }
        } else
            activityHistoryDetailBinding.btnTakeAway.visibility = View.GONE
    }

    fun getObserverValues() {
        orderDetailViewModel.mLoadingProgress.observe(this, Observer {
            baseLiveDataLoading.postValue(it)
        })
        orderDetailViewModel.orderDetailResponse.observe(this, Observer {
            if (it.responseData != null) {
                initalizieAdapter()
            }
        })

        orderDetailViewModel.takeAwayResponse.observe(this, Observer {
            if (it.statusCode == "200") {
                if(Status == "PICKEDUP") {
                    getOrderDetail()
                } else {
                    Toasty.success(this@OrderDetailActivity,it.message.toString())
                    finish()
                }
            } else {
                Toasty.error(this@OrderDetailActivity,it.message.toString())
            }
        })

    }


}