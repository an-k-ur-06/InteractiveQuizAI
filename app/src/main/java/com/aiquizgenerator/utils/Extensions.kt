package com.aiquizgenerator.utils
import android.content.Context
import android.view.View
import android.widget.Toast
fun View.visible() { visibility = View.VISIBLE }
fun View.gone() { visibility = View.GONE }
fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
fun Int.formatTimer(): String = String.format("%02d:%02d", this / 60, this % 60)
