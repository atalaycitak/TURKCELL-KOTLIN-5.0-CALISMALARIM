package com.turkcell.ticketapp.util

import android.content.Context
import com.turkcell.data.network.ApiException
import com.turkcell.data.network.NetworkException
import com.turkcell.ticketapp.R

fun Throwable.toUserMessage(): String = when (this) {
    is ApiException -> when (code) {
        401 -> "Email veya sifre hatali"
        403 -> "Bu islemi yapmaya yetkiniz yok"
        404 -> "Kayit bulunamadi"
        409 -> resolveConflictMessage()
        in 500..599 -> "Sunucu su anda cevap veremiyor"
        else -> "Beklenmeyen bir hata olustu (HTTP $code)"
    }
    is NetworkException -> "Internet baglantisi yok"
    else -> message ?: "Bilinmeyen bir hata olustu."
}

fun Throwable.toUserMessage(context: Context): String = when (this) {
    is ApiException -> when (code) {
        401 -> context.getString(R.string.error_invalid_credentials)
        403 -> context.getString(R.string.error_forbidden)
        404 -> context.getString(R.string.error_not_found)
        409 -> resolveConflictMessage(context)
        in 500..599 -> context.getString(R.string.error_server)
        else -> context.getString(R.string.error_unexpected, code)
    }
    is NetworkException -> context.getString(R.string.error_no_internet)
    else -> message ?: context.getString(R.string.error_unknown)
}

private fun ApiException.resolveConflictMessage(): String {
    val body = errorMessage?.lowercase() ?: ""
    return when {
        body.contains("email") -> "Bu email zaten kayitli"
        body.contains("capacity") -> "Stok yetersiz, etkinligi yenileyin"
        body.contains("already_paid") || body.contains("paid") -> "Bu satin alim zaten odenmis"
        body.contains("already_used") || body.contains("used") -> "Bu bilet daha once kullanilmis"
        else -> "Islem catismasi olustu"
    }
}

private fun ApiException.resolveConflictMessage(context: Context): String {
    val body = errorMessage?.lowercase() ?: ""
    return when {
        body.contains("email") -> context.getString(R.string.error_email_taken)
        body.contains("capacity") -> context.getString(R.string.error_capacity_exceeded)
        body.contains("already_paid") || body.contains("paid") -> context.getString(R.string.error_already_paid)
        body.contains("already_used") || body.contains("used") -> context.getString(R.string.error_already_used)
        else -> context.getString(R.string.error_conflict)
    }
}
