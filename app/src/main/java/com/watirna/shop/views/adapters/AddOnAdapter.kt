package com.watirna.shop.views.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.databinding.LayoutAddonListItemBinding
import com.watirna.shop.datamodel.addon.AddOnResponseModel
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.views.product.addons.AddOnListViewModel
import javax.inject.Inject


class AddOnAdapter(
    val ctx: Context,
    var viewModel: AddOnListViewModel,
    val addOnResponseData: List<AddOnResponseModel.ResponseData>,
    val loadingObservable: MutableLiveData<*>
) :
    RecyclerView.Adapter<AddOnAdapter.MyViewHolder>(), CustomClickListner {
    @Inject
    lateinit var commanMethods: CommanMethods

    init {
        AppController.appComponent.inject(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<LayoutAddonListItemBinding>(
            LayoutInflater.from(parent.context)
            , R.layout.layout_addon_list_item, parent, false
        )
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return addOnResponseData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.addoItemBinding.tvAddonName.text = addOnResponseData.get(position).addon_name
        holder.addoItemBinding.closeButton.setOnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            loadingObservable.value = true
                            viewModel.deleteAddOn(addOnResponseData.get(position).id)
                        }
                        DialogInterface.BUTTON_NEGATIVE -> {
                            dialog.dismiss()
                        }
                    }
                }

            val builder: AlertDialog = AlertDialog.Builder(ctx).setMessage("Are you sure want to delete?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show()
            commanMethods.AlertButton(builder)
        }
        holder.addoItemBinding.tvAddonName.setOnClickListener {
            viewModel.openEditActivity(addOnResponseData.get(position).id)
        }
    }

    inner class MyViewHolder(itemView: LayoutAddonListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val addoItemBinding = itemView
        fun bind() {

        }

    }

    override fun onListClickListner() {

    }
}