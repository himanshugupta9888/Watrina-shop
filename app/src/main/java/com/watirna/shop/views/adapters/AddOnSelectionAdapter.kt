package com.watirna.shop.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.watirna.shop.R
import com.watirna.shop.databinding.LayoutAddonSelectionItemBinding
import com.watirna.shop.datamodel.addon.AddOnResponseModel
import com.watirna.shop.views.product.productDML.addonSelection.AddOnSelectionViewModel
import es.dmoral.toasty.Toasty

class AddOnSelectionAdapter(
    val ctx: Context,
    var viewModel: AddOnSelectionViewModel,
    val addOnResponseData: List<AddOnResponseModel.ResponseData>,
    val loadingObservable: MutableLiveData<*>
) :
    RecyclerView.Adapter<AddOnSelectionAdapter.MyViewHolder>(), CustomClickListner {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<LayoutAddonSelectionItemBinding>(
            LayoutInflater.from(parent.context)
            , R.layout.layout_addon_selection_item, parent, false
        )
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return addOnResponseData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.addoItemBinding.tvAddonName.text = addOnResponseData.get(position).addon_name
        holder.addoItemBinding.rbAddOn.isChecked = addOnResponseData.get(position).selection
        if (addOnResponseData.get(position).price != 0.0)
            holder.addoItemBinding.tvPrice.setText("" + addOnResponseData.get(position).price!!)

        holder.addoItemBinding.rbAddOn.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                if (holder.addoItemBinding.tvPrice.text != null && !holder.addoItemBinding.tvPrice.text.toString()
                        .equals("")
                ) {
                    viewModel.addOnIdArray.add("" + addOnResponseData.get(position).id)
                    viewModel.addOnPrice.add("" + holder.addoItemBinding.tvPrice.text.toString())

                } else {
                    Toasty.error(ctx, "Please enter the price", Toast.LENGTH_SHORT).show()
                    holder.addoItemBinding.rbAddOn.isChecked = false
                }
            }
        }

    }

    inner class MyViewHolder(itemView: LayoutAddonSelectionItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val addoItemBinding = itemView
        fun bind() {

        }

    }

    override fun onListClickListner() {

    }
}