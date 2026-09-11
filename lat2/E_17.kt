fun doOperation(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    return operation(a, b)
}

val sum = doOperation(5, 3) { x, y -> x + y }
val product = doOperation(5, 3) { x, y -> x * y }

val multiply: (Int, Int) -> Int = { x, y -> x * y }
val greet: (String) -> String = { name -> "Hello, $name!" }
val double: (Int) -> Int = { it * 2 }
