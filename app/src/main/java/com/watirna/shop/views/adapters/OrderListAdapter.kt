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
import com.watirna.shop.databinding.RowCartAddonBinding
import com.watirna.shop.databinding.RowOrderItemHeaderBinding
import com.watirna.shop.datamodel.NewOrderModel
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.utils.SessionManager
import javax.inject.Inject

class OrderListAdapter(ctx: Context, itemList: List<NewOrderModel.ResponseData.Invoice.Item>) :
    SectionedRecyclerViewAdapter<OrderListAdapter.OrderListViewModel>() {
    private var contx: Context? = null
    private var listOfProduct: List<NewOrderModel.ResponseData.Invoice.Item>? = null

    @Inject
    lateinit var commanMethods: CommanMethods

    @Inject
    lateinit var sessionManager: SessionManager

    init {
        this.contx = ctx
        this.listOfProduct = itemList
        AppController.appComponent.inject(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewModel {
        var viewHolder: OrderListViewModel? = null
        when (viewType) {
            VIEW_TYPE_HEADER -> {
                viewHolder = OrderListViewModel(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.row_order_item_header, parent, false
                    )
                )
            }

            VIEW_TYPE_ITEM -> {
                viewHolder = OrderListViewModel(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.row_cart_addon, parent, false
                    )
                )
            }
        }

        return viewHolder!!
    }

    override fun onBindHeaderViewHolder(headerViewModel: OrderListViewModel?, position: Int) {
        val rowOrderItemHeaderBinding: RowOrderItemHeaderBinding = headerViewModel!!.viewssBinding as RowOrderItemHeaderBinding

        if(listOfProduct!!.get(position).product!=null && !listOfProduct!!.get(position).product!!.item_name.isNullOrEmpty()) {
            rowOrderItemHeaderBinding.root.visibility= View.VISIBLE
            rowOrderItemHeaderBinding.tvProductName.setText(listOfProduct!!.get(position).product!!.item_name)
            val itemQuantity = String.format(
                contx!!.resources.getString(R.string.item_quantity),
                "Qty",
                listOfProduct!!.get(position).quantity
            )
            rowOrderItemHeaderBinding.tvProductQuontity.setText(itemQuantity+listOfProduct!!.get(position).unit)
            rowOrderItemHeaderBinding.tvProductPrice.setText(
                commanMethods.getNumberFormat()!!.format(
                    listOfProduct!!.get(position).item_price
                )
            )
        }else{
            rowOrderItemHeaderBinding.root.visibility= View.GONE
        }

    }


    override fun onBindViewHolder(
        childViewModel: OrderListViewModel?,
        section: Int,
        relativePosition: Int,
        absolutePosition: Int
    ) {
        val rowCartAddonBinding: RowCartAddonBinding =
            childViewModel!!.viewssBinding as RowCartAddonBinding

        if(!listOfProduct!!.get(section).cartaddon.isNullOrEmpty()) {
            rowCartAddonBinding.root.visibility=View.VISIBLE
            rowCartAddonBinding.tvAddonName.setText(
                listOfProduct!!.get(section).cartaddon!!.get(
                    relativePosition
                )!!.addon_name
            )

            val itemQuantity = String.format(
                contx!!.resources.getString(R.string.item_quantity),
                "Qty",
                listOfProduct!!.get(section).quantity
            )

            rowCartAddonBinding.tvAddonQuantity.setText(itemQuantity)
            rowCartAddonBinding.tvAddonPrice.setText(
                commanMethods.getNumberFormat()!!.format(
                    listOfProduct!!.get(section).cartaddon!!.get(relativePosition)!!.addon_price
                )
            )
        }else{
            rowCartAddonBinding.root.visibility=View.GONE
        }

    }

    override fun getItemCount(section: Int): Int {
          if(listOfProduct!!.get(section).cartaddon!!.isNullOrEmpty())
              return  1
        else
         return  listOfProduct!!.get(section).cartaddon!!.size
    }


    override fun getSectionCount(): Int {
        return listOfProduct!!.size

    }

    inner class OrderListViewModel(itemView: ViewDataBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val viewssBinding = itemView

    }
}