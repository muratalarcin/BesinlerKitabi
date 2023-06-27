package com.muratalarcin.besinlerkitabi.util

import android.content.Context
import android.support.v4.widget.CircularProgressDrawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.gorselIndir(url: String?, placeholder: CircularProgressDrawable){

    val options = RequestOptions().placeholder(placeholder).error(android.R.drawable.btn_dialog)

    Glide.with(context).setDefaultRequestOptions(options).load(url).into(this)
}

fun placeholderYap(context : Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f //dönen barın kalınlığı
        centerRadius = 40f //barın yarıçapı
        start()
    }
}

@BindingAdapter("android:downloadImage")
fun downloadImage(view: ImageView, url: String?){
    view.gorselIndir(url, placeholderYap(view.context))

}