package com.example.presentation.util

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.annotation.RequiresPermission

class Network {
    companion object Utils{
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        private fun getNetworkInfo(context: Context): NetworkInfo?{
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo
        }

        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isConnected(context: Context): Boolean {
            val info = getNetworkInfo(context)
            return info != null && info.isConnected
        }
    }
}