fun greet(name: String): String {
    return "Hello, $name!"
}

fun greet(name: String): String = "Hello, $name!"

fun greet(name: String = "Dunia"): String = "Hello, $name!"

greet(name = "Budi")

fun logMessage(message: String): Unit {
    println(message)
}

fun logMessage(message: String) {
    println(message)
}
