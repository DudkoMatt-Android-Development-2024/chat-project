package com.github.dudkomatt.androidcourse.chatproject.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.dudkomatt.androidcourse.chatproject.ui.screen.login.LoginScreen

@Composable
fun ChatApp() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LoginScreen()
//            ChatScreen(
//                createNewChannel = {},
//                createNewChat = {}
//            )
        }
    }
}
