package com.watirna.shop.utils

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.watirna.shop.R
import com.watirna.shop.application.AppController
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

open class ImageUtils {
    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var commonMethods: CommanMethods

    init {
        AppController.appComponent.inject(this)
    }

    fun loadProfileImage(context: Context, imageView: CircleImageView, url: String) {
        try {
            if (!TextUtils.isEmpty(url)) {

                val thumbnailRequest = Glide
                    .with(context)
                    .load(url)

                val options = RequestOptions()
                    .format(DecodeFormat.PREFER_RGB_565)
                    .centerCrop()
                    .placeholder(R.color.colorGray)
                    .error(R.color.colorGray)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)

                Glide.with(context)
                    .load(url)
                    .apply(options)
                    .thumbnail(thumbnailRequest)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.color.white)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadImage(context: Context, imageView: ImageView, uri: Uri) {
        try {
            if (uri != null) {
                val options = RequestOptions()
                    .format(DecodeFormat.PREFER_RGB_565)
                    .centerCrop()
                    .placeholder(R.color.colorGray)
                    .error(R.color.colorGray)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)

                Glide.with(context)
                    .load(uri.path)
                    .apply(options)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.color.white)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadReminderImage(context: Context, imageView: ImageView, url: String, isVideo: Boolean) {
        try {
            val thumbnailRequest = Glide
                .with(context)
                .load(url)

            val options = RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .centerCrop()
                .placeholder(if (isVideo) R.color.colorGray else R.color.colorGray)
                .error(if (isVideo) R.color.colorGray else R.color.colorGray)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

            Glide.with(context)
                .load(url)
                .apply(options)
                .thumbnail(thumbnailRequest)
                .into(imageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
