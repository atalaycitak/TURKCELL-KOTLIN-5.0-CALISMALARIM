package com.turkcell.ticketapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.turkcell.core.util.DateFormatter
import com.turkcell.ticketapp.viewmodel.EventDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    viewModel: EventDetailViewModel,
    onBack: () -> Unit,
    onPurchaseSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.purchaseSuccess) {
        if (state.purchaseSuccess) {
            viewModel.consumeSuccess()
            onPurchaseSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.event?.name ?: "Etkinlik Detay") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.event == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage ?: "Etkinlik bulunamadi",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> {
                val event = state.event!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = event.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(text = event.description, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = event.venue,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = DateFormatter.format(event.startsAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Bilet Turu Secin",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))

                    event.ticketTypes.forEach { ticketType ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.selectedTicketTypeId == ticketType.id,
                                onClick = { viewModel.selectTicketType(ticketType.id) },
                                enabled = ticketType.remaining > 0
                            )
                            Spacer(Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = ticketType.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "Kalan: ${ticketType.remaining}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = "${ticketType.priceCents / 100}.${"%02d".format(ticketType.priceCents % 100)} TL",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Adet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FilledTonalButton(
                            onClick = { viewModel.setQuantity(state.quantity - 1) },
                            enabled = state.quantity > 1
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Azalt")
                        }
                        Text(
                            text = "${state.quantity}",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        FilledTonalButton(
                            onClick = { viewModel.setQuantity(state.quantity + 1) },
                            enabled = state.quantity < 20
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Artir")
                        }
                    }

                    if (state.errorMessage != null) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = state.errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { showConfirmDialog = true },
                        enabled = state.canPurchase,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (state.isPurchasing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = LocalContentColor.current
                            )
                        } else {
                            Text("Satin Al")
                        }
                    }
                }

                if (showConfirmDialog) {
                    val selectedType = event.ticketTypes.find { it.id == state.selectedTicketTypeId }
                    val totalCents = (selectedType?.priceCents ?: 0) * state.quantity
                    AlertDialog(
                        onDismissRequest = { showConfirmDialog = false },
                        title = { Text("Satin Alma Onayi") },
                        text = {
                            Text(
                                "${selectedType?.name ?: ""} x${state.quantity}\n" +
                                "Toplam: ${totalCents / 100}.${"%02d".format(totalCents % 100)} TL"
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                showConfirmDialog = false
                                viewModel.purchase()
                            }) {
                                Text("Onayla")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showConfirmDialog = false }) {
                                Text("Iptal")
                            }
                        }
                    )
                }
            }
        }
    }
}
