package com.watirna.shop.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.watirna.shop.R
import com.watirna.shop.databinding.LayoutCuisineItemBinding
import com.watirna.shop.datamodel.product.CuisineModel
import com.watirna.shop.views.selectcuisine.SelectCuisineViewModel

class CuisineListAdapter(
    val ctx: Context,
    var viewModel: SelectCuisineViewModel,
    val cuisineData: List<CuisineModel.ResponseData>,
    val loadingObservable: MutableLiveData<*>
) :
    RecyclerView.Adapter<CuisineListAdapter.MyViewHolder>(), CustomClickListner {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<LayoutCuisineItemBinding>(
            LayoutInflater.from(parent.context)
            , R.layout.layout_cuisine_item, parent, false
        )
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return cuisineData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.cuisineItemBinding.tvCuisineName.text = cuisineData.get(position).name
     /*   holder.cuisineItemBinding.constraintLayout.setOnClickListener {
            viewModel.cuisineModel.value = cuisineData.get(position)
        }*/
        holder.cuisineItemBinding.rbCuisine.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                viewModel.cuisineIds.add(cuisineData.get(position).id)
                viewModel.cuisineNames.add(cuisineData.get(position).name)
            }
        }
    }

    inner class MyViewHolder(itemView: LayoutCuisineItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val cuisineItemBinding = itemView
        fun bind() {

        }

    }

    override fun onListClickListner() {

    }
}