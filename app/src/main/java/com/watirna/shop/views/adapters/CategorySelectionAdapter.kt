package com.watirna.shop.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.watirna.shop.R
import com.watirna.shop.databinding.LayoutCategorySelectionItemBinding
import com.watirna.shop.datamodel.category.CategoryItemsModel
import com.watirna.shop.views.product.categorySelection.CategorySelectionViewModel

class CategorySelectionAdapter(
    val ctx: Context,
    var viewModel: CategorySelectionViewModel,
    val categoryData: List<CategoryItemsModel.Category>,
    val loadingObservable: MutableLiveData<*>
) :
    RecyclerView.Adapter<CategorySelectionAdapter.MyViewHolder>(), CustomClickListner {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<LayoutCategorySelectionItemBinding>(
            LayoutInflater.from(parent.context)
            , R.layout.layout_category_selection_item, parent, false
        )
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return categoryData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.cuisineItemBinding.tvCategoryName.text =
            categoryData.get(position).store_category_name
        holder.cuisineItemBinding.constraintLayout.setOnClickListener {
            viewModel.categoryModel.value = categoryData.get(position)
        }
        holder.cuisineItemBinding.rbCategory.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            if (b) {
                viewModel.categoryModel.value = categoryData.get(position)
            }
        }
    }

    inner class MyViewHolder(itemView: LayoutCategorySelectionItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val cuisineItemBinding = itemView
        fun bind() {

        }

    }

    override fun onListClickListner() {

    }
}