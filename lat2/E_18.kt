val result = user?.let {
    "${it.name} (${it.age} tahun)"
}

val textView = TextView(context).apply {
    text = "Hello"
    textSize = 16f
    setTextColor(Color.BLACK)
}

val greeting = user.run {
    "Selamat datang, $name!"
}

val user = createUser().also {
    Log.d("TAG", "User created: ${it.name}")
}

val message = with(user) {
    "Nama: $name, Email: $email"
}
