package com.turkcell.libraryapp.data.repository

import com.turkcell.libraryapp.data.model.Book
import com.turkcell.libraryapp.data.model.BorrowRecord
import com.turkcell.libraryapp.data.supabase.supabase
import io.github.jan.supabase.postgrest.postgrest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BorrowRepository {

    // Kitap ödünç al
    suspend fun borrowBook(studentId: String, bookId: String): Result<Unit> = runCatching {
        // Bugünün tarihini al
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = Calendar.getInstance()
        val borrowedAt = dateFormat.format(today.time)

        // 5 gün sonrasını hesapla (maks kiralama süresi)
        today.add(Calendar.DAY_OF_MONTH, 5)
        val dueDate = dateFormat.format(today.time)

        // Kiralama kaydı oluştur
        val record = BorrowRecord(
            studentId = studentId,
            bookId = bookId,
            borrowedAt = borrowedAt,
            dueDate = dueDate
        )
        supabase.postgrest["borrow_records"].insert(record)

        // Kitabın available_copies değerini 1 düşür
        val currentBook = supabase.postgrest["books"]
            .select { filter { eq("id", bookId) } }
            .decodeSingle<Book>()

        supabase.postgrest["books"].update(
            {
                set("available_copies", currentBook.avaiableCopies - 1)
            }
        ) {
            filter { eq("id", bookId) }
        }
    }

    // Öğrencinin tüm kiralamalarını getir
    suspend fun getMyBorrows(studentId: String): Result<List<BorrowRecord>> = runCatching {
        supabase.postgrest["borrow_records"]
            .select { filter { eq("student_id", studentId) } }
            .decodeList<BorrowRecord>()
    }

    // Kitap iade et
    suspend fun returnBook(borrowId: String, bookId: String): Result<Unit> = runCatching {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = dateFormat.format(Calendar.getInstance().time)

        // İade tarihini kaydet
        supabase.postgrest["borrow_records"].update(
            {
                set("returned_at", today)
            }
        ) {
            filter { eq("id", borrowId) }
        }

        // Kitabın available_copies değerini 1 artır
        val currentBook = supabase.postgrest["books"]
            .select { filter { eq("id", bookId) } }
            .decodeSingle<Book>()

        supabase.postgrest["books"].update(
            {
                set("available_copies", currentBook.avaiableCopies + 1)
            }
        ) {
            filter { eq("id", bookId) }
        }
    }
}
