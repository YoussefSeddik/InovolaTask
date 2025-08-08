package com.youssef.inovolatask.domain

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import android.net.Uri
import java.io.File

class ShareFileUseCase(private val context: Context) {
    operator fun invoke(file: File) {
        val authority = "${context.packageName}.provider"
        val uri: Uri = FileProvider.getUriForFile(context, authority, file)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            clipData = android.content.ClipData.newRawUri("csv", uri)
        }
        context.startActivity(
            Intent.createChooser(intent, "Share CSV")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}

