import java.io.File

fun main() {
    var count = 0
    File("words_with_numbers.txt").forEachLine { if (it.matches(Regex("[0-9]*"))) count++ }
    println(count)
}