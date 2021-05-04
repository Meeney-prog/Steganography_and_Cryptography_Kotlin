import java.io.File

fun main() {
    val file = File("new.txt")
    print("${file.length()} ${file.readLines().size}")
}