val fruits = listOf("Apple", "Banana", "Cherry")
val uniqueIds = setOf(1, 2, 3, 2)
val scores = mapOf("Ali" to 90, "Budi" to 85)

val mutableFruits = mutableListOf("Apple", "Banana")
mutableFruits.add("Cherry")
mutableFruits.removeAt(0)

val mutableScores = mutableMapOf("Ali" to 90)
mutableScores["Budi"] = 85
