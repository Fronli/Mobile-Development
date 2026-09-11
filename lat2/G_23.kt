val uiState by viewModel.uiState.collectAsStateWithLifecycle()

when (uiState) {
    is UiState.Loading -> CircularProgressIndicator()
    is UiState.Success -> UserCard((uiState as UiState.Success).data)
    is UiState.Error -> ErrorMessage((uiState as UiState.Error).message)
}
