fun main() {
    val line = readLine()!!
    for (c in line) if (c.isDigit()) {
        println(c)
        break
    }
}