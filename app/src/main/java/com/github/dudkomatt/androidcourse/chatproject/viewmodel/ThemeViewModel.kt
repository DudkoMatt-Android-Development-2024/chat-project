package com.github.dudkomatt.androidcourse.chatproject.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.dudkomatt.androidcourse.chatproject.R
import com.github.dudkomatt.androidcourse.chatproject.data.DataStorePreferencesRepository
import com.github.dudkomatt.androidcourse.chatproject.data.ThemeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val application: Application,
    private val dataStorePreferencesRepository: DataStorePreferencesRepository
) : ViewModel() {

    private val _themeUiState = MutableStateFlow<ThemeState>(ThemeState.SystemDefault)
    val themeUiState: StateFlow<ThemeState> = _themeUiState.asStateFlow()

    init {
        getThemeState()
    }

    fun setThemeState(themeState: ThemeState) {
        viewModelScope.launch {
            dataStorePreferencesRepository.storeTheme(themeState)

            Toast.makeText(
                application.applicationContext,
                application.applicationContext.getString(R.string.theme_changed_to, themeState), Toast.LENGTH_SHORT
            ).show()

            _themeUiState.value = themeState
        }
    }

    private fun getThemeState() {
        viewModelScope.launch {
            dataStorePreferencesRepository.getTheme()
        }
    }
}
