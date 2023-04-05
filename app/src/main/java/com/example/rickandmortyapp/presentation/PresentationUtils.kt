package com.example.rickandmortyapp.presentation

import android.app.AlertDialog
import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.example.rickandmortyapp.R

object PresentationUtils {
    const val BASE_URL = "https://rickandmortyapi.com/api/"


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