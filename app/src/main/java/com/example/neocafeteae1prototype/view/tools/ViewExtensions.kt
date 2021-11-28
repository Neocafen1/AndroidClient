package com.example.neocafeteae1prototype.view.tools

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.neocafeteae1prototype.R
import com.google.android.material.snackbar.Snackbar


fun String.showToast(context: Context, duration: Int) {
    Toast.makeText(context, this, duration).show()
}

fun String.logging() {
    Log.i("TAG", this)
}

fun String.mainLogging() {
    Log.i("Main", this)
}

fun String.firebaseLogging() {
    Log.i("Firebase", this)
}

fun String.bearerToken(): String {
    return "Bearer $this"
}


fun View.notVisible(){
    this.visibility = View.GONE
}


fun View.visible(){
    this.visibility = View.VISIBLE
}

fun String.showSnackBar(view: View, duration: Int) {
    Snackbar.make(view, this, duration).show()
}

fun String.showSnackBarWithButton(view: View){
    Snackbar.make(view, this, Snackbar.LENGTH_INDEFINITE).apply {
        setBackgroundTint(Color.parseColor("#11A818"))
        setAction("Ок"){
            dismiss()
        }
    }.show()
}


infix fun TextView.text(text: String) {
    this.text = text
}


fun ImageView.loadWithGlide(image: String) {
    Glide.with(this.context)
        .load(image)
        .timeout(5000)
        .thumbnail(Glide.with(this.context).load(R.drawable.progress_custom).optionalCenterCrop())
        .fitCenter()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .into(this)

}


fun TextView.setWhiteColor(context: Context) {
    this.setTextColor(ContextCompat.getColor(context, R.color.white))
}

fun CardView.cardActivate(textView: TextView){
    this.setCardBackgroundColor(
        ContextCompat.getColor(
            this.context,
            R.color.main_purple_enable_color
        )
    )
    this.cardElevation = 10F
    this.radius = 10F
    textView.setTextColor(ContextCompat.getColor(this.context, R.color.white))
}

fun CardView.cardNotActive(textView: TextView){
    this.setCardBackgroundColor(ContextCompat.getColor(this.context, R.color.white))
    this.cardElevation = 0F
    this.radius = 0F
    textView.setTextColor(ContextCompat.getColor(this.context, R.color.main_purple_enable_color))
}



