val name: String? = getUserName()

val length = name?.length

val length = name?.length ?: 0

val length = name!!.length

if (name != null) {
    println(name.length)
}
