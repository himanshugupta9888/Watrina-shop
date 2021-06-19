package com.watirna.shop.views.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.databinding.RowHistoryItemBinding
import com.watirna.shop.datamodel.HistoryDataModel
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.utils.Constants
import com.watirna.shop.utils.PreferenceKey
import com.watirna.shop.utils.SessionManager
import com.watirna.shop.views.orderdetail.OrderDetailActivity
import java.lang.Exception
import javax.inject.Inject

class  HistoryCommonAdapter(context:Context, itemList:ArrayList<HistoryDataModel.ResponseData.Data>, historyType:String):RecyclerView.Adapter<HistoryCommonAdapter.HistoryViewModel>(){

    private  lateinit var ctx:Context
    private  lateinit var  itemArrayList:ArrayList<HistoryDataModel.ResponseData.Data>
    private  lateinit var   orderType:String
    private  lateinit var   FROM:String
    @Inject
    lateinit var  sessionManager: SessionManager

    init {
        AppController.appComponent?.inject(this)
    }
    init {
        ctx=context
        itemArrayList=itemList
        this.orderType=historyType
        AppController.appComponent.inject(this)
    }

    @Inject
    lateinit var  commanMethods: CommanMethods

    inner class  HistoryViewModel(binding:RowHistoryItemBinding):RecyclerView.ViewHolder(binding.root){
        val rowItemBinding=binding as RowHistoryItemBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewModel {
        val historyViewModel=HistoryViewModel(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.row_history_item,parent,false))
        return  historyViewModel
    }

    override fun getItemCount(): Int {
        return  itemArrayList.size
    }

    override fun onBindViewHolder(holder: HistoryViewModel, position: Int) {
      holder.rowItemBinding.tvOrderId.text = itemArrayList.get(position).store_order_invoice_id
      holder.rowItemBinding.tvDate.setText(commanMethods.getLocalTimeStamp(itemArrayList.get(position).created_time!!,"Req_Date_Month"))
      holder.rowItemBinding.tvTime.setText(commanMethods.getLocalTimeStamp(itemArrayList.get(position).created_time!!,"Req_time"))
      holder.rowItemBinding.tvUsername.setText(itemArrayList.get(position).user?.let { it.first_name })
      holder.rowItemBinding.tvPrice.setText(sessionManager.get(PreferenceKey.CURRENCY_SYMBOL,"$").toString() + itemArrayList.get(position).total_amount?.let { it.toString() })
      holder.rowItemBinding.tvPaymentType.setText(itemArrayList.get(position).payment_mode)
        if(itemArrayList.get(position).delivery != null) {
            holder.rowItemBinding.tvLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location,0,0,0)
            holder.rowItemBinding.tvLocation.setPadding(0,0,0,0)
            holder.rowItemBinding.tvLocation.compoundDrawablePadding = 0
            holder.rowItemBinding.tvLocation.text = itemArrayList.get(position).delivery!!.flat_no + " " + itemArrayList.get(position).delivery!!.street
        } else {
            holder.rowItemBinding.tvLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_take_away,0,0,0)
            holder.rowItemBinding.tvLocation.setPadding(4,0,0,0)
            holder.rowItemBinding.tvLocation.compoundDrawablePadding = 5
            holder.rowItemBinding.tvLocation.text = "TakeAway"
        }
//       if(!orderType.equals(Constants.WebConstants.CANCELLED))
        holder.itemView.setOnClickListener {
            try {
                if (itemArrayList.get(position).id != null) {
                    val intent = Intent(ctx, OrderDetailActivity::class.java)
                    val orderStatusDetailID = itemArrayList.get(position).id
                    intent.putExtra("orderStatusDetailID", orderStatusDetailID.toString())
                    intent.putExtra("from", FROM)
                    ctx!!.startActivity(intent)
                }
            } catch (N: NullPointerException){
                N.printStackTrace()
                Toast.makeText(ctx,"Tracking not available",Toast.LENGTH_SHORT).show()
            } catch (CE: Exception){
                CE.printStackTrace()
            }
        }
        when(orderType){
            Constants.WebConstants.ONGOING -> {
                FROM = Constants.WebConstants.ONGOING
            }

            Constants.WebConstants.COMPLETED -> {
                FROM = Constants.WebConstants.COMPLETED
            }

            Constants.WebConstants.CANCELLED -> {
                FROM = Constants.WebConstants.CANCELLED
            }
        }
    }

}