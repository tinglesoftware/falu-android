package io.falu.android.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import java.io.File

internal fun getMediaType(context: Context, file: File): MediaType {
    val uri = Uri.fromFile(file)

    val mimeType: String? = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
        val resolver = context.contentResolver
        resolver.getType(uri)
    } else {
        val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
    }

    return if (!mimeType.isNullOrEmpty()) {
        mimeType.toMediaType()
    } else {
        "*/*".toMediaType()
    }
}