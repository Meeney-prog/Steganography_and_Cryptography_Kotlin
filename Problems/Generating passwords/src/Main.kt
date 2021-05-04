fun main() {
    val letters = "abcdefghijklmnopqrstuvwxyz"
    var letterCount = 0
    var digitCount = 0
    var result = ""
    val input = readLine()!!.split(" ").toTypedArray().map { it.toInt() }.toMutableList()
    input[2] += input[3] - (input[0] + input[1] + input[2])
    for (i in 0 until input[0]) result += letters[letterCount++ % 26].toUpperCase()
    for (i in 0 until input[1]) result += letters[letterCount++ % 26]
    for (i in 0 until input[2]) result += digitCount++ % 10
    println(result)
}