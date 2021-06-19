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
import com.watirna.shop.databinding.LayoutProductListItemBinding
import com.watirna.shop.datamodel.product.ProductItemsModel
import com.watirna.shop.utils.CommanMethods
import com.watirna.shop.views.product.productDML.ProductListViewModel
import javax.inject.Inject

class ProductListAdapter(
    val ctx: Context,
    var viewModel: ProductListViewModel,
    val responseData: List<ProductItemsModel.ResponseData.Data>,
    val loadingObservable: MutableLiveData<*>
) :
    RecyclerView.Adapter<ProductListAdapter.MyViewHolder>(), CustomClickListner {
    @Inject
    lateinit var commanMethods: CommanMethods

    init {
        AppController.appComponent.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<LayoutProductListItemBinding>(
            LayoutInflater.from(parent.context)
            , R.layout.layout_product_list_item, parent, false
        )
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return responseData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.productListItemBinding.tvProductName.text = responseData.get(position).item_name
        holder.productListItemBinding.tvProductDesc.text =
            responseData.get(position).item_description
        Glide.with(ctx).load(responseData.get(position).picture)
            .placeholder(R.drawable.ic_image_upload).dontAnimate()
            .into(holder.productListItemBinding.productImg);
        holder.productListItemBinding.closeButton.setOnClickListener {
            val dialogClickListener =
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            loadingObservable.value = true
                            viewModel.deleteProduct(responseData.get(position).id)
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
        holder.productListItemBinding.tvProductName.setOnClickListener {
            viewModel.openEditProductActivity(responseData.get(position).id)
        }
        holder.productListItemBinding.lvParent.setOnClickListener {
            viewModel.openEditProductActivity(responseData.get(position).id)
        }
    }

    inner class MyViewHolder(itemView: LayoutProductListItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val productListItemBinding = itemView
        fun bind() {

        }

    }

    override fun onListClickListner() {

    }
}