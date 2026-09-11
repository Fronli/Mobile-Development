sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

fun handleState(state: UiState<User>) {
    when (state) {
        is UiState.Loading -> showLoadingSpinner()
        is UiState.Success -> showUser(state.data)
        is UiState.Error -> showError(state.message)
    }
}
