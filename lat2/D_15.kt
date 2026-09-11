object AppConfig {
    const val BASE_URL = "https://api.example.com"
    const val TIMEOUT = 30L
}

class UserRepository {
    companion object {
        fun create(): UserRepository = UserRepository()
    }
}

val repo = UserRepository.create()
