for (i in 1..5) println(i)
for (i in 1 until 5) println(i)
for (i in 5 downTo 1) println(i)
for (i in 0..10 step 2) println(i)

val names = listOf("Ali", "Budi", "Cici")
for (name in names) println(name)
names.forEachIndexed { index, name -> println("$index: $name") }

while (condition) { ... }
do { ... } while (condition)
