package com.turkcell.libraryapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turkcell.libraryapp.data.model.BorrowRecord

@Composable
fun BorrowCard(
    borrow: BorrowRecord,
    onReturnClick: (borrowId: String, bookId: String) -> Unit,
    isReturning: Boolean = false
) {
    // İade edilmiş mi kontrol et
    val isReturned = borrow.returnedAt != null

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isReturned)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Durum göstergesi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Kitap ID: ${borrow.bookId.take(8)}...",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isReturned) "İade Edildi ✓" else "Aktif",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isReturned) Color(0xFF4CAF50) else Color(0xFFFF9800)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Tarih bilgileri
            Text(
                text = "Ödünç Tarihi: ${borrow.borrowedAt}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Son İade Tarihi: ${borrow.dueDate}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (isReturned) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "İade Tarihi: ${borrow.returnedAt}",
                    fontSize = 13.sp,
                    color = Color(0xFF4CAF50)
                )
            }

            // Henüz iade edilmediyse İade Et butonu göster
            if (!isReturned) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onReturnClick(borrow.id, borrow.bookId) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isReturning,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF9800)
                    )
                ) {
                    if (isReturning) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("İade Et", color = Color.White)
                    }
                }
            }
        }
    }
}
