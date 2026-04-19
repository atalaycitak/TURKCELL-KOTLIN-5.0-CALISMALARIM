package com.example.userdatahomework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.userdatahomework.ui.screen.UserListScreen
import com.example.userdatahomework.ui.theme.UserDataHomeworkTheme
import com.example.userdatahomework.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

// hilt ile bagimliliklarin inject edildigi activity
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // dark mode toggle state
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }

            UserDataHomeworkTheme(darkTheme = isDarkTheme) {
                UserListScreen(
                    viewModel = viewModel,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}