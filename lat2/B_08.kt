val day = 3
val dayName = when (day) {
    1 -> "Senin"
    2 -> "Selasa"
    3 -> "Rabu"
    4, 5 -> "Kamis atau Jumat"
    in 6..7 -> "Weekend"
    else -> "Tidak valid"
}

when {
    score >= 90 -> println("Lulus dengan pujian")
    score >= 70 -> println("Lulus")
    else -> println("Tidak lulus")
}
