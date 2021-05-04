fun identity(a: Int) = a
fun half(a: Int) = a / 2
fun zero(a: Int) = 0
fun generate(functionName: String): (Int) -> Int {
    return when (functionName) {
        "identity" -> ::identity
        "half" -> ::half
        else -> ::zero
    }
}