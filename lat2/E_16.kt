fun String.titleCase(): String =
    split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercase() }
    }

"hello world".titleCase()

fun Int.toRupiah(): String = "Rp ${"%,d".format(this)}"

50000.toRupiah()

fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }
fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
