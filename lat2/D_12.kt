class Car(val brand: String, var speed: Int = 0) {
    fun accelerate(amount: Int) {
        speed += amount
    }

    fun brake() {
        speed = 0
    }

    override fun toString(): String = "$brand at $speed km/h"
}

val car = Car("Toyota")
car.accelerate(60)
println(car)
