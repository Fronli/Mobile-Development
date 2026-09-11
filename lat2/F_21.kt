fun getUserUpdates(): Flow<User> = flow {
    while (true) {
        val user = api.getLatestUser()
        emit(user)
        delay(5000)
    }
}

viewModelScope.launch {
    getUserUpdates()
        .filter { it.isActive }
        .map { it.toDisplayModel() }
        .collect { user ->
            _uiState.value = user
        }
}
