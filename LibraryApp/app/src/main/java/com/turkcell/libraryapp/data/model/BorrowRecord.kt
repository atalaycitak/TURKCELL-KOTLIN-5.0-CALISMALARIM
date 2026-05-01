package com.turkcell.libraryapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BorrowRecord(
    val id: String = "",
    @SerialName(value = "student_id") val studentId: String,
    @SerialName(value = "book_id") val bookId: String,
    @SerialName(value = "borrowed_at") val borrowedAt: String = "",
    @SerialName(value = "due_date") val dueDate: String = "",
    @SerialName(value = "returned_at") val returnedAt: String? = null
)
