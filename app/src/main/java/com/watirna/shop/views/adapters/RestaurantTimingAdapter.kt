package com.watirna.shop.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.watirna.shop.BR
import com.watirna.shop.R
import com.watirna.shop.databinding.ItemEverydayBinding
import com.watirna.shop.datamodel.ShopTimeModel
import com.watirna.shop.views.edittime.EditTimeViewModel

class RestaurantTimingAdapter(private val editTimeViewModel: EditTimeViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RestaurantTimingVH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_everyday,
                parent, false
            )
        )

    override fun getItemCount(): Int {
        return editTimeViewModel.getData().size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RestaurantTimingVH).bindData(getItem(position),editTimeViewModel)
    }

    private fun getItem(index: Int): ShopTimeModel.ResponseData {
        return editTimeViewModel.getData()[index]
    }

    class RestaurantTimingVH(private val binding: ItemEverydayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(
            item: ShopTimeModel.ResponseData, editTimeViewModel: EditTimeViewModel) {
            binding.setVariable(BR.item, item)
            binding.setVariable(BR.viewModel, editTimeViewModel)
            binding.setVariable(BR.position, layoutPosition)
            binding.executePendingBindings()
        }

    }

}