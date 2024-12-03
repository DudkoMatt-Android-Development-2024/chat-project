package com.github.dudkomatt.androidcourse.chatproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.dudkomatt.androidcourse.chatproject.data.ThemeState
import com.github.dudkomatt.androidcourse.chatproject.ui.ChatApp
import com.github.dudkomatt.androidcourse.chatproject.ui.theme.ChatProjectTheme
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.ThemeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinContext {
                val themeViewModel: ThemeViewModel = koinViewModel()
                val themeUiState by themeViewModel.themeUiState.collectAsState()

                ChatProjectTheme(
                    darkTheme = when (themeUiState) {
                        ThemeState.SystemDefault -> isSystemInDarkTheme()
                        ThemeState.Dark -> true
                        ThemeState.Light -> false
                    }
                ) {
                    ChatApp()
                }
            }
        }
    }
}
