package com.github.dudkomatt.androidcourse.chatproject.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.chat.ChatScreen
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.loading.LoadingScreen
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.login.LoginScreen
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.LoginViewModel
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.RootViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatApp() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val rootViewModel: RootViewModel = koinViewModel()
            val uiState by rootViewModel.uiState.collectAsState()

            if (uiState.isLoggedIn == null) {
                LoadingScreen()
            }
            else if (uiState.isLoggedIn != true) {
                LoginScreen()
            }
            else {
                ChatScreen(
                    onLogoutClick = rootViewModel::logOut,
                    createNewChannel = {},
                    createNewChat = {}
                )
            }
        }
    }
}
