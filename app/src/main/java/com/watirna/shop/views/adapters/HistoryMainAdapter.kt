package com.watirna.shop.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.watirna.shop.R
import com.watirna.shop.databinding.RowOrderStatusBinding
import com.watirna.shop.datamodel.HistoryDetailModel
import com.watirna.shop.utils.Constants

class HistoryMainAdapter(
    val context: Context,
    val historyItem: HistoryDetailModel.ResponseData,
    val ItemSize: Int
) :
    RecyclerView.Adapter<HistoryMainAdapter.HisotryMainViewModel>() {

    inner class HisotryMainViewModel(binding: RowOrderStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val rowItemBinding = binding as RowOrderStatusBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HisotryMainViewModel {
        val historyViewModel = HisotryMainViewModel(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_order_status, parent, false
            )
        )
        return historyViewModel
    }

    override fun getItemCount(): Int {
        return ItemSize
    }

    override fun onBindViewHolder(holder: HisotryMainViewModel, position: Int) {
        when (position) {
            0 -> {
                    holder.rowItemBinding.ivOrderStatusImg.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_ecommerce
                        )
                    )

                    if (historyItem.status.equals(Constants.WebConstants.ORDERED)) {
                        holder.rowItemBinding.tvStatusDescription.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black
                            )
                        )
                        holder.rowItemBinding.tvStatusTitle.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black
                            )
                        )
                        holder.rowItemBinding.ivOrderStatusImg.setImageTintList(
                            ContextCompat.getColorStateList(
                                context,
                                R.color.black
                            )
                        )
                    }
                    holder.rowItemBinding.tvStatusTitle.setText(context.resources.getString(R.string.title_order_placed))
                    holder.rowItemBinding.tvStatusDescription.setText(context.resources.getString(R.string.label_order_recived))
            }

            1 -> {
                    holder.rowItemBinding.ivOrderStatusImg.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.security
                        )
                    )
                    if (historyItem.status.equals(Constants.ShopStatus.RECEIVED)) {
                        holder.rowItemBinding.tvStatusDescription.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black
                            )
                        )
                        holder.rowItemBinding.tvStatusTitle.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black
                            )
                        )
                        holder.rowItemBinding.ivOrderStatusImg.setImageTintList(
                            ContextCompat.getColorStateList(
                                context,
                                R.color.black
                            )
                        )
                    }
                    holder.rowItemBinding.tvStatusTitle.setText(context.resources.getString(R.string.title_order_confirmed))
                    holder.rowItemBinding.tvStatusDescription.setText(context.resources.getString(R.string.label_order_confimred_new))

            }

            2 -> {
                if(historyItem.order_type == "TAKEAWAY"){
                    if (historyItem.status.equals("PICKEDUP")) {
                        holder.rowItemBinding.tvStatusDescription.setTextColor(ContextCompat.getColor(context, R.color.black))
                        holder.rowItemBinding.tvStatusTitle.setTextColor(ContextCompat.getColor(context, R.color.black))
                        holder.rowItemBinding.ivOrderStatusImg.setImageTintList( ContextCompat.getColorStateList(context,R.color.black))
                    }
                    holder.rowItemBinding.ivOrderStatusImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.delivery_truck))
                    holder.rowItemBinding.tvStatusTitle.setText(context.resources.getString(R.string.title_order_Ready))
                    holder.rowItemBinding.tvStatusDescription.setText(context.resources.getString(R.string.label_order_ready))
                } else {
                    if (historyItem.status.equals(Constants.ShopStatus.SEARCHING)) {

                        holder.rowItemBinding.tvStatusDescription.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black
                            )
                        )

                        holder.rowItemBinding.tvStatusTitle.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.black
                            )
                        )
                        holder.rowItemBinding.ivOrderStatusImg.setImageTintList(
                            ContextCompat.getColorStateList(
                                context,
                                R.color.black
                            )
                        )
                    }
                    holder.rowItemBinding.ivOrderStatusImg.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.payment_method
                        )
                    )
                    holder.rowItemBinding.tvStatusTitle.setText(context.resources.getString(R.string.title_order_searching))
                    holder.rowItemBinding.tvStatusDescription.setText(context.resources.getString(R.string.label_order_searching))
                }
            }

            3 -> {
                if (historyItem.status.equals(Constants.ShopStatus.PICKEDUP) || historyItem.status.equals(Constants.WebConstants.ACCEPTED)) {
                    holder.rowItemBinding.tvStatusDescription.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.black
                        )
                    )
                    holder.rowItemBinding.tvStatusTitle.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.black
                        )
                    )
                    holder.rowItemBinding.ivOrderStatusImg.setImageTintList( ContextCompat.getColorStateList(context,R.color.black))
                }
                holder.rowItemBinding.ivOrderStatusImg.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.discount
                    )
                )
                holder.rowItemBinding.tvStatusTitle.setText(context.resources.getString(R.string.title_order_picked))
                holder.rowItemBinding.tvStatusDescription.setText(context.resources.getString(R.string.label_order_picked))
            }

            4 -> {
                if (historyItem.status.equals(Constants.ShopStatus.COMPLETED)) {
                    holder.rowItemBinding.tvStatusDescription.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.black
                        )
                    )
                    holder.rowItemBinding.tvStatusTitle.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.black
                        )
                    )
                    holder.rowItemBinding.ivOrderStatusImg.setImageTintList( ContextCompat.getColorStateList(context,R.color.black))
                }
                holder.rowItemBinding.ivOrderStatusImg.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.delivery_truck
                    )
                )
                holder.rowItemBinding.tvStatusTitle.setText(context.resources.getString(R.string.title_order_Delivered))
                holder.rowItemBinding.tvStatusDescription.setText(context.resources.getString(R.string.label_order_delivered))
            }
        }
    }
}