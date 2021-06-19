package com.watirna.shop.views.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.watirna.shop.R
import com.watirna.shop.databinding.LayoutProductMainItemBinding
import com.watirna.shop.views.product.ProductMainModel
import com.watirna.shop.views.product.addons.AddOnListActivity
import com.watirna.shop.views.product.category.CategoriesActivity
import com.watirna.shop.views.product.productDML.ProductListActivity


class ProductMainAdapter(
    var ctx: Context,
    val productMainData: ArrayList<ProductMainModel.ProductMain>
) :
    RecyclerView.Adapter<ProductMainAdapter.MyViewHolder>(), CustomClickListner {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<LayoutProductMainItemBinding>(
            LayoutInflater.from(parent.context)
            , R.layout.layout_product_main_item, parent, false
        )
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return productMainData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
        holder.productMainItemBinding.ivImage.setImageResource(productMainData.get(position).imageViewDrawable)
        holder.productMainItemBinding.tvTitle.text = productMainData.get(position).menuName
        holder.productMainItemBinding.lvParent.setOnClickListener {
            if (productMainData.get(position).menuName.equals("addons", true)) {
                ctx.startActivity(Intent(ctx, AddOnListActivity::class.java))
            } else if (productMainData.get(position).menuName.equals("category", true)) {
                ctx.startActivity(Intent(ctx, CategoriesActivity::class.java))
            } else if (productMainData.get(position).menuName.equals("product", true)) {
                ctx.startActivity(Intent(ctx, ProductListActivity::class.java))
            }
        }
    }

    inner class MyViewHolder(itemView: LayoutProductMainItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val productMainItemBinding = itemView
        fun bind() {

        }

    }

    override fun onListClickListner() {

    }
}