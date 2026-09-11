viewModelScope.launch {
    val users = repository.getUsers()
    _uiState.value = users
}

viewModelScope.launch {
    val usersDeferred = async { repository.getUsers() }
    val postsDeferred = async { repository.getPosts() }

    val users = usersDeferred.await()
    val posts = postsDeferred.await()

    updateUI(users, posts)
}

suspend fun fetchAndSave() {
    val data = withContext(Dispatchers.IO) {
        api.fetchData()
    }
    updateUI(data)
}
