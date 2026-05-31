package com.turkcell.ticketapp.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

fun generateQrCode(content: String, size: Int = 400): ImageBitmap? {
    return try {
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(content, BarcodeFormat.QR_CODE, size, size)
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}
