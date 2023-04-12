package com.example.rickandmortyapp.presentation

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.res.ResourcesCompat
import com.example.rickandmortyapp.R
import java.text.SimpleDateFormat
import java.util.*

object PresentationUtils {

    const val CODE_RESULT = 100

    const val INTENT_DATA = "data"
    const val INTENT_ID = "id"

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

     fun getIdFromUrl(url:String):Int = url.substringAfterLast("/").toInt()


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