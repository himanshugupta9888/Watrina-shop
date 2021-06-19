package com.watirna.shop.views.requestdetail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.base.BaseActivity
import com.watirna.shop.databinding.ActivityRequestAcceptBinding
import com.watirna.shop.datamodel.CommonSuccessResponse
import com.watirna.shop.datamodel.NewOrderModel
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.utils.ImageUtils
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.views.accept.AcceptOrderFragment
import com.watirna.shop.views.adapters.OrderListAdapter
import kotlinx.android.synthetic.main.header_toolbar.view.*
import kotlinx.android.synthetic.main.invoice.view.*
import kotlinx.android.synthetic.main.layout_header.view.*
import javax.inject.Inject

class RequestDetailActivity : BaseActivity<ActivityRequestAcceptBinding>(), RequestDetailNavigator {

    @Inject
    lateinit var imageUtils: ImageUtils

    @Inject
    lateinit var commanMethods: CommanMethods

    @Inject
    lateinit var  sessionManager: SessionManager

    init {
        AppController.appComponent.inject(this)
    }

    private lateinit var activityRequestAcceptBinding: ActivityRequestAcceptBinding
    private lateinit var orderDetailViewModel: RequestDetailViewModel
    private lateinit var context: Context
    private var firstName: String? = ""
    private var lastName: String? = ""
    private lateinit var mAlertDialog: AlertDialog
    private var deliveryDate = ""
    private var deliveryTime = ""


    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityRequestAcceptBinding = mViewDataBinding as ActivityRequestAcceptBinding
        orderDetailViewModel = ViewModelProviders.of(this).get(RequestDetailViewModel::class.java)
        activityRequestAcceptBinding.orderDetailViewModel = orderDetailViewModel
        activityRequestAcceptBinding.setLifecycleOwner(this)
        orderDetailViewModel.navigator = this
        setSupportActionBar(activityRequestAcceptBinding.tlOrderDetail.tl_header)
        context = this
        getIntentValues()
        updateViews()
        getObserValues()
        mViewDataBinding.ivOrderCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data =
                Uri.parse("tel:" + orderDetailViewModel.incomingOrderModelLd.value?.user?.mobile)
            startActivity(intent)
        }
        mViewDataBinding.tlOrderDetail.iv_left.setOnClickListener{
            finish()
        }

    }

    fun getObserValues() {
        orderDetailViewModel.preparationTime.observe(this, Observer<String> {
            if (!it.isNullOrEmpty()) {
                orderDetailViewModel.acceptOrder()
            }
        })

        orderDetailViewModel.commonRespnoseLd.observe(this, Observer<CommonSuccessResponse> {
            if (it.statusCode.equals("200")) {
                commanMethods.showToast(context, it.message!!, true)
                finish()
            }
        })


        orderDetailViewModel.loadingProgressLd.observe(this, Observer {
            baseLiveDataLoading.value = it
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_request_accept
    }

    fun getIntentValues() {
        val newOrderTypeToken = object : TypeToken<NewOrderModel.ResponseData>() {}.type
        val strOrderModel =
            if (intent != null && intent.hasExtra("orderDetail")) intent.getStringExtra("orderDetail") else ""
        if (!strOrderModel.isNullOrEmpty())
            orderDetailViewModel.incomingOrderModelLd.value =
                Gson().fromJson<NewOrderModel.ResponseData>(strOrderModel!!, newOrderTypeToken)

    }

    fun updateViews() {
        activityRequestAcceptBinding.disputeBtn.visibility = View.GONE
        if (orderDetailViewModel.incomingOrderModelLd.value != null) {
            if (orderDetailViewModel.incomingOrderModelLd.value!!.user!!.picture != null)
                imageUtils.loadProfileImage(
                    context,
                    activityRequestAcceptBinding.ivOrderUserImage,
                    orderDetailViewModel.incomingOrderModelLd.value!!.user!!.picture!!
                )

            if (orderDetailViewModel.incomingOrderModelLd.value!!.delivery!!.street != null)
                activityRequestAcceptBinding.tvOrderUserAdd.setText(orderDetailViewModel.incomingOrderModelLd.value!!.delivery!!.street)

            if(orderDetailViewModel.incomingOrderModelLd.value!!.store_order_invoice_id != null)
                activityRequestAcceptBinding.tvOrderId.text = "ORDER ID : "+orderDetailViewModel.incomingOrderModelLd.value!!.store_order_invoice_id

            if(orderDetailViewModel.incomingOrderModelLd.value!!.order_type == "TAKEAWAY")
                activityRequestAcceptBinding.tvOrderUserAdd.setText("TAKEAWAY")

            activityRequestAcceptBinding.tvOrderPayMode.setText(orderDetailViewModel.incomingOrderModelLd.value!!.invoice!!.payment_mode)

            firstName = orderDetailViewModel.incomingOrderModelLd.value!!.user!!.first_name ?: ""
            lastName = orderDetailViewModel.incomingOrderModelLd.value!!.user!!.last_name ?: ""
            activityRequestAcceptBinding.tvOrderUserName.setText(firstName + " " + lastName)

            activityRequestAcceptBinding.orderAcceptLay.visibility = View.VISIBLE
            orderDetailViewModel.storeID.value =
                orderDetailViewModel.incomingOrderModelLd.value!!.id.toString()
            orderDetailViewModel.userID.value =
                orderDetailViewModel.incomingOrderModelLd.value!!.user_id.toString()

            activityRequestAcceptBinding.layInvoice.tvOrderSubTotal.setText(
                commanMethods.getNumberFormat()!!
                    .format(orderDetailViewModel.incomingOrderModelLd.value!!.invoice!!.item_price)
            )
            activityRequestAcceptBinding.layInvoice.tvPackingCharge.setText(
                commanMethods.getNumberFormat()!!
                    .format(orderDetailViewModel.incomingOrderModelLd.value!!.invoice!!.store_package_amount)
            )
            activityRequestAcceptBinding.layInvoice.tvServiceTax.setText(
                commanMethods.getNumberFormat()!!
                    .format(orderDetailViewModel.incomingOrderModelLd.value!!.invoice!!.tax_amount)
            )
            activityRequestAcceptBinding.layInvoice.tvOrderDeliveryCharge.setText(
                commanMethods.getNumberFormat()!!
                    .format(orderDetailViewModel.incomingOrderModelLd.value!!.invoice!!.delivery_amount)
            )
            activityRequestAcceptBinding.layInvoice.tvOrderPromoAmt.setText(" - " +
                commanMethods.getNumberFormat()!!
                    .format(orderDetailViewModel.incomingOrderModelLd.value!!.invoice!!.promocode_amount)
            )
            activityRequestAcceptBinding.layInvoice.tvOrderShopDiscount.setText(" - " +
                commanMethods.getNumberFormat()!!
                    .format(orderDetailViewModel.incomingOrderModelLd.value!!.invoice!!.discount)
            )
            activityRequestAcceptBinding.layInvoice.tvOrderTotal.setText(
                commanMethods.getNumberFormat()!!
                    .format(orderDetailViewModel.incomingOrderModelLd.value!!.invoice!!.payable)
            )
            val orderListAdapter = OrderListAdapter(
                this,
                orderDetailViewModel.incomingOrderModelLd.value!!.invoice!!.items!!
            )
            activityRequestAcceptBinding.rvOrderDetail.setAdapter(orderListAdapter)
        }
    }

    override fun getErrorMessage(message: String) {
        commanMethods.showToast(context, message, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun acceptOrder() {
        if(sessionManager.sharedPreferences.getString(PreferenceKey.SHOP_TYPE_NAME,"").equals("FOOD")) {
            val tollChargeDialog = AcceptOrderFragment()
            tollChargeDialog.show(supportFragmentManager, "preparetTime")
        } else {
            deliveryDate()
        }
    }

    override fun cancelOrder() {
        try {
            val ReasonDialogView = LayoutInflater.from(this).inflate(R.layout.layout_cancel_details, null, false)
            val builder = AlertDialog.Builder(this@RequestDetailActivity)
            builder.setView(ReasonDialogView)
            mAlertDialog = builder.create()
            mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            mAlertDialog?.isShowing?.let {
                mAlertDialog!!.hide()
            }

            mAlertDialog.show()
            mAlertDialog.setCancelable(true)

            var comment = mAlertDialog.findViewById<EditText>(R.id.edt_comment)
            var send_button = mAlertDialog.findViewById<TextView>(R.id.send_button)
            var cancel_button = mAlertDialog.findViewById<TextView>(R.id.cancel_button)

            send_button?.setOnClickListener {
                if(comment?.text == null && comment?.text!!.equals(""))
                    Toast.makeText(this@RequestDetailActivity, "Please add reason for cancellation",Toast.LENGTH_SHORT).show()
                else if(comment?.text?.length!! < 3)
                    Toast.makeText(this@RequestDetailActivity, "Reason should have minimum 4 letters",Toast.LENGTH_SHORT).show()
                else
                    orderDetailViewModel.cancelOrder(comment?.text!!.toString())
            }

            cancel_button?.setOnClickListener {
                mAlertDialog!!.hide()
            }
        } catch (ce: Exception){
            ce.printStackTrace()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun deliveryDate() {
        try {
            val calenderDialogView = LayoutInflater.from(this).inflate(R.layout.layout_calender, null, false)
            val builder = AlertDialog.Builder(this@RequestDetailActivity)
            builder.setView(calenderDialogView)
            mAlertDialog = builder.create()
            mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mAlertDialog?.isShowing?.let { mAlertDialog!!.hide() }
            mAlertDialog.show()
            mAlertDialog.setCancelable(true)

            var datepicker = mAlertDialog.findViewById<DatePicker>(R.id.datepicker)
            var timePicker = mAlertDialog.findViewById<TimePicker>(R.id.timePicker)
            var submit = mAlertDialog.findViewById<Button>(R.id.btn_okay)

            datepicker?.setOnClickListener {
                println("selected date " + datepicker.dayOfMonth + "/" + (datepicker.month + 1))
            }

            timePicker?.setOnClickListener {
                println("selected time " + timePicker.hour +" " + timePicker.minute )
            }

            submit?.setOnClickListener {
                if(datepicker?.visibility == View.VISIBLE) {
                    timePicker?.visibility = View.VISIBLE
                    datepicker?.visibility = View.GONE
                    deliveryDate = ""+ datepicker.year+ "-" + (datepicker.month + 1) + "-" + datepicker.dayOfMonth
                    println("selected date " + datepicker.dayOfMonth+"/"+datepicker.month+"/"+datepicker.year)
                } else if(timePicker?.visibility == View.VISIBLE) {
                    println("selected time " + timePicker.hour +" " + timePicker.minute)
                    deliveryTime= ""+ timePicker.hour + ":" + timePicker.minute + ":00"
                    orderDetailViewModel.preparationTime.value = deliveryDate + " " + deliveryTime
                }
            }

        } catch (ce: Exception){
            ce.printStackTrace()
        }

    }

}