package com.turkcell.ticketapp.util

import com.turkcell.data.network.ApiException
import com.turkcell.data.network.NetworkException

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
