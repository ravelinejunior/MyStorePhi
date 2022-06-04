package br.com.raveline.mystorephi.presentation.listener
 open class UiState {
    object Success : UiState()
    object Error : UiState()
    object NoConnection : UiState()
    object Loading : UiState()
    object Initial : UiState()
}