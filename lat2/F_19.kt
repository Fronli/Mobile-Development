fun loadData() {
    val data = networkCall()
    updateUI(data)
}

fun loadData() {
    networkCall(
        onSuccess = { data ->
            parseData(data,
                onSuccess = { parsed ->
                    saveToDb(parsed,
                        onSuccess = { updateUI(it) },
                        onError = { handleError(it) }
                    )
                }
            )
        },
        onError = { handleError(it) }
    )
}

suspend fun loadData() {
    val data = networkCall()
    val parsed = parseData(data)
    saveToDb(parsed)
    updateUI(parsed)
}
