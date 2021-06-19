package com.watirna.shop.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.databinding.HistoryRowCartAddonBinding
import com.watirna.shop.databinding.HistoryRowOrderItemHeaderBinding
import com.watirna.shop.datamodel.HistoryDetailModel
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.utils.SessionManager
import javax.inject.Inject

class OrderListHistoryDetailsAdapter(ctx: Context, itemList: List<HistoryDetailModel.ResponseData.OrderInvoice.Item>) :
    SectionedRecyclerViewAdapter<OrderListHistoryDetailsAdapter.OrderListHistoryDetailsModel>() {
    private var contx: Context? = null
    private var listOfProduct: List<HistoryDetailModel.ResponseData.OrderInvoice.Item>? = null

    @Inject
    lateinit var commanMethods: CommanMethods

    @Inject
    lateinit var sessionManager: SessionManager

    init {
        this.contx = ctx
        this.listOfProduct = itemList
        AppController.appComponent.inject(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListHistoryDetailsModel {
        var viewHolder: OrderListHistoryDetailsModel? = null
        when (viewType) {
            VIEW_TYPE_HEADER -> {
                viewHolder = OrderListHistoryDetailsModel(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.history_row_order_item_header, parent, false
                    )
                )
            }

            VIEW_TYPE_ITEM -> {
                viewHolder = OrderListHistoryDetailsModel(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.history_row_cart_addon, parent, false
                    )
                )
            }
        }

        return viewHolder!!
    }

    override fun onBindHeaderViewHolder(headerViewModel: OrderListHistoryDetailsModel?, position: Int) {
        val historyRowOrderItemHeaderBinding: HistoryRowOrderItemHeaderBinding = headerViewModel!!.viewssBinding as HistoryRowOrderItemHeaderBinding

        if(listOfProduct!!.get(position).product !=null && !listOfProduct!!.get(position).product!!.item_name.isNullOrEmpty()) {
            historyRowOrderItemHeaderBinding.root.visibility= View.VISIBLE
            historyRowOrderItemHeaderBinding.tvProductName.setText(listOfProduct!!.get(position).product!!.item_name)
            val itemQuantity = String.format(
                contx!!.resources.getString(R.string.item_quantity),
                "Qty",
                listOfProduct!!.get(position).quantity
            )
            historyRowOrderItemHeaderBinding.tvProductQuontity.setText(itemQuantity)
            historyRowOrderItemHeaderBinding.tvProductPrice.setText(
                commanMethods.getNumberFormat()!!.format(
                    listOfProduct!!.get(position).item_price
                )
            )
        }else{
            historyRowOrderItemHeaderBinding.root.visibility= View.GONE
        }

    }


    override fun onBindViewHolder(
        childViewModel: OrderListHistoryDetailsModel?,
        section: Int,
        relativePosition: Int,
        absolutePosition: Int
    ) {
        val historyRowCartAddonBinding: HistoryRowCartAddonBinding =
            childViewModel!!.viewssBinding as HistoryRowCartAddonBinding

        if(listOfProduct!!.get(section).cartaddon !=null && !listOfProduct!!.get(section).cartaddon.isNullOrEmpty()) {
            historyRowCartAddonBinding.root.visibility= View.VISIBLE
            historyRowCartAddonBinding.tvAddonName.setText(
                listOfProduct!!.get(section).cartaddon!!.get(
                    relativePosition
                )!!.addon_name
            )

            val itemQuantity = String.format(
                contx!!.resources.getString(R.string.item_quantity),
                "Qty",
                listOfProduct!!.get(section).quantity
            )

            historyRowCartAddonBinding.tvAddonQuantity.setText(itemQuantity)
            historyRowCartAddonBinding.tvAddonPrice.setText(
                commanMethods.getNumberFormat()!!.format(
                    listOfProduct!!.get(section)!!.cartaddon!!.get(relativePosition)!!.addon_price
                )
            )
        }else{
            historyRowCartAddonBinding.root.visibility= View.GONE
        }

    }

    override fun getItemCount(section: Int): Int {
        if(listOfProduct!!.get(section).cartaddon !=null && listOfProduct!!.get(section)!!.cartaddon!!.isNullOrEmpty())
            return  1
        else
            return  listOfProduct!!.get(section).cartaddon!!.size
    }


    override fun getSectionCount(): Int {
        return listOfProduct!!.size

    }

    inner class OrderListHistoryDetailsModel(itemView: ViewDataBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val viewssBinding = itemView

    }
}