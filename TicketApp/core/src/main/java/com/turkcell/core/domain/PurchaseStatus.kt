package com.turkcell.core.domain

enum class PurchaseStatus {
    PENDING,
    PAID;

    companion object {
        fun fromApi(value: String): PurchaseStatus = when (value.uppercase()) {
            "PAID" -> PAID
            else -> PENDING
        }
    }
}
