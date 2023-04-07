package com.example.rickandmortyapp.presentation

import android.app.AlertDialog
import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.example.rickandmortyapp.R
import java.text.SimpleDateFormat
import java.util.*

object PresentationUtils {

    const val INTENT_DATA = "data"

    fun getCreated(created:String):String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault())
        val date = inputFormat.parse(created)
        return outputFormat.format(date ?: "")
    }


    fun setupDialogError(context: Context, msg: String): AlertDialog.Builder {
        return AlertDialog.Builder(context).apply {
            setIcon(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.baseline_error_24,
                    null
                )
            )
            setTitle("Error")
            setMessage(msg)
            setCancelable(false)
        }
    }
}