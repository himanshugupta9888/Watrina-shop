package com.watirna.shop.views.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import com.watirna.shop.databinding.LayoutCategoryListItemBinding
import com.watirna.shop.datamodel.category.CategoryItemsModel
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.views.product.category.CategoriesViewModel
import javax.inject.Inject

class CategoryAdapter(
    val ctx: Context,
    var viewModel: CategoriesViewModel,
    val categoryResponseData: List<CategoryItemsModel.Category>,
    val loadingObservable: MutableLiveData<*>
) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>(), CustomClickListner {
    @Inject
    lateinit var commanMethods: CommanMethods

    init {
        AppController.appComponent.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<LayoutCategoryListItemBinding>(
            LayoutInflater.from(parent.context)
            , R.layout.layout_category_list_item, parent, false
        )
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return categoryResponseData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.categoryItemBinding.tvCategoryName.text =
            categoryResponseData.get(position).store_category_name
        Glide.with(ctx).load(categoryResponseData.get(position).picture)
            .placeholder(R.drawable.ic_image_upload).dontAnimate()
            .into(holder.categoryItemBinding.imageCategory);
        holder.categoryItemBinding.closeButton.setOnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            loadingObservable.value = true
                             viewModel.deleteAddOn(categoryResponseData.get(position).id)
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
        holder.categoryItemBinding.tvCategoryName.setOnClickListener {
              viewModel.openEditActivity(categoryResponseData.get(position).id)
        }
        holder.categoryItemBinding.parentView.setOnClickListener{

        }
    }

    inner class MyViewHolder(itemView: LayoutCategoryListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val categoryItemBinding = itemView
        fun bind() {

        }

    }

    override fun onListClickListner() {

    }
}