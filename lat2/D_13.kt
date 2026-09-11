data class User(
    val id: Int,
    val name: String,
    val email: String
)

val user1 = User(1, "Budi", "budi@email.com")
val user2 = user1.copy(name = "Ali")

println(user1 == user2)
println(user1)

val (id, name, email) = user1
