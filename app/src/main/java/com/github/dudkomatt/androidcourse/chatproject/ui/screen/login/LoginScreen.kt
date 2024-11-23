package com.github.dudkomatt.androidcourse.chatproject.ui.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.dudkomatt.androidcourse.chatproject.R
import com.github.dudkomatt.androidcourse.chatproject.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val loginViewModel: LoginViewModel = viewModel()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val textFieldHeight = 56.dp
        val contentWidthModifier = Modifier.fillMaxWidth(0.8f)

        Text(
            modifier = contentWidthModifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 32.dp),
            text = stringResource(R.string.chat_project_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        UsernameTextField(
            modifier = contentWidthModifier
                .height(textFieldHeight),
            login = loginViewModel.username,
            onValueChange = loginViewModel::onUsernameChange
        )
        PasswordTextField(
            modifier = contentWidthModifier
                .height(textFieldHeight),
            password = loginViewModel.password,
            onPasswordValueChange = loginViewModel::onPasswordChange,
            isPasswordVisible = loginViewModel.isPasswordVisible,
            onPasswordVisibilityToggle = loginViewModel::onPasswordVisibilityToggle
        )

        val context = LocalContext.current

        Button(
            modifier = contentWidthModifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            content = { Text(stringResource(R.string.sign_in)) },
            onClick = { loginViewModel.onSignIn(context) }
        )

        Button(
            modifier = contentWidthModifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
            content = { Text(stringResource(R.string.register)) },
            onClick = { loginViewModel.onRegister(context) }
        )
    }
}

@Composable
fun UsernameTextField(
    login: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier,
        value = login,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.username)) },
        singleLine = true
    )
}

@Composable
fun PasswordTextField(
    password: String,
    onPasswordValueChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier,
        value = password,
        onValueChange = onPasswordValueChange,
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (isPasswordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description =
                if (isPasswordVisible) stringResource(R.string.hide_password_hint) else stringResource(
                    R.string.show_password_hint
                )

            IconButton(onClick = onPasswordVisibilityToggle) {
                Icon(imageVector = image, description)
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    LoginScreen()
}
