package com.longyuan.lyerp

import android.app.AlertDialog
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dmax.dialog.SpotsDialog

object GeneralUtils {

    val gson: Gson
        get() = GsonBuilder().serializeNulls().create()

    fun getWaitDialog(context: Context): AlertDialog {
        val loadBuilder = SpotsDialog.Builder()
            .setContext(context)
            .setTheme(R.style.CustomLoadingDialog)
            .build()
        return loadBuilder
    }
}