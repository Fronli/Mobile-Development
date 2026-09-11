val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

val evens = numbers.filter { it % 2 == 0 }

val doubled = numbers.map { it * 2 }

val firstEven = numbers.find { it % 2 == 0 }

val hasNegative = numbers.any { it < 0 }
val allPositive = numbers.all { it > 0 }

val students = listOf(
    Student("Ali", "A"),
    Student("Budi", "B"),
    Student("Cici", "A")
)
val byGrade = students.groupBy { it.grade }

val sorted = students.sortedBy { it.name }

val result = numbers
    .filter { it % 2 == 0 }
    .map { it * it }
    .sortedDescending()
    .take(3)
