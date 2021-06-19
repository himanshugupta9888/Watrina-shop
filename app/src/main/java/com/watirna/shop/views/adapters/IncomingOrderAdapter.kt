package com.watirna.shop.views.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.databinding.RowIncomingOrdersBinding
import com.watirna.shop.datamodel.NewOrderModel
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.utils.ImageUtils
import com.watirna.shop.views.requestdetail.RequestDetailActivity
import com.watirna.shop.views.orders.incoming.IncomingViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class IncomingOrderAdapter(
    mViewModel: IncomingViewModel,
    contxt: Context,
    newOrderModel: NewOrderModel
) : RecyclerView.Adapter<IncomingOrderAdapter.IncomingOrderViewModel>() {
    @Inject
    lateinit var imageUtils: ImageUtils

    @Inject
    lateinit var commanMethods: CommanMethods

    private var mViewModel: IncomingViewModel? = null
    private var ctxt: Context? = null
    private var incomingOrderModel: NewOrderModel? = null

    init {
        this.mViewModel = mViewModel
        this.ctxt = contxt
        this.incomingOrderModel = newOrderModel
        AppController.appComponent.inject(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomingOrderViewModel {
        val incomingViewModel = IncomingOrderViewModel(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_incoming_orders, parent, false
            )
        )
        return incomingViewModel
    }

    override fun getItemCount(): Int {
        return incomingOrderModel!!.responseData!!.size
    }

    override fun onBindViewHolder(holder: IncomingOrderViewModel, position: Int) {
        if(incomingOrderModel!!.responseData!!.get(position)!!.delivery != null){
            holder.incomingOrderBinding.address.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location,0,0,0)
            holder.incomingOrderBinding.address.compoundDrawablePadding = 0
            if(incomingOrderModel!!.responseData!!.get(position)!!.delivery!!.map_address != null)
                holder.incomingOrderBinding.address.text = incomingOrderModel!!.responseData!!.get(position)!!.delivery!!.flat_no + "" + incomingOrderModel!!.responseData!!.get(position
                )!!.delivery!!.street + " " + incomingOrderModel!!.responseData!!.get(position)!!.delivery!!.map_address
            else
                holder.incomingOrderBinding.address.text = incomingOrderModel!!.responseData!!.get(position)!!.delivery!!.flat_no + "" + incomingOrderModel!!.responseData!!.get(position
                )!!.delivery!!.street
        } else {
            holder.incomingOrderBinding.address.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_take_away,0,0,0)
            holder.incomingOrderBinding.address.compoundDrawablePadding = 5
            holder.incomingOrderBinding.address.text = "TakeAway"
        }

        try {
            var date: Date? = null
            val df = SimpleDateFormat("yyyy-MM-dd hh:mm aa")
            date = df.parse(incomingOrderModel!!.responseData!!.get(position)!!.created_time!!)
            val sdf = SimpleDateFormat("hh:mm aa")
            val shortTimeStr = sdf.format(date)
            holder.incomingOrderBinding.orderTime.text = shortTimeStr
            println(shortTimeStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        } catch (ce: Exception) {
            ce.printStackTrace()
        }

        imageUtils.loadProfileImage(
            ctxt!!,
            holder.incomingOrderBinding.userImg,
            incomingOrderModel!!.responseData!!.get(position)!!.user!!.picture.toString()
        )
        holder.incomingOrderBinding.userName.setText(
            incomingOrderModel!!.responseData!!.get(
                position
            )!!.store_order_invoice_id
        )
        holder.incomingOrderBinding.paymentMode.setText(
            incomingOrderModel!!.responseData!!.get(
                position
            )!!.invoice!!.payment_mode
        )

        holder.incomingOrderBinding.itemLayout.setOnClickListener(View.OnClickListener {
            val intent = Intent(ctxt, RequestDetailActivity::class.java)
            val strOrderModel = Gson().toJson(incomingOrderModel!!.responseData!!.get(position))
            intent.putExtra("orderDetail", strOrderModel)
            ctxt!!.startActivity(intent)
        })
    }

    inner class IncomingOrderViewModel(itemView: RowIncomingOrdersBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val incomingOrderBinding: RowIncomingOrdersBinding = itemView
    }

}