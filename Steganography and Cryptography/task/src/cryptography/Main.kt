package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.experimental.xor

fun main() {
    while (true) {
        println("Task (hide, show, exit):")
        when (val line = readLine()!!) {
            "hide" -> hide("png")
            "show" -> show()
            "exit" -> {
                println("Bye!"); break
            }
            else -> println("Wrong task: $line")
        }
    }
}

fun hide(format: String) {
    val input = println("Input image file:").let { readLine()!! }
    val output = println("Output image file:").let { readLine()!! }
    val message = println("Message to hide:").let { readLine()!! }
    val password = println("Password:").let { readLine()!! }
    try {
        val bImage = ImageIO.read(File(input))
        if (bImage.width * bImage.height < (message.encodeToByteArray().size + 3) * 8) {
            println("The input image is not large enough to hold this message.")
            return
        }
        ImageIO.write(proceedImage(bImage, message, password), format, File(output))
        println("Message saved in $output image.")
    } catch (e: Exception) {
        println(e.message)
    }
}

fun proceedImage(bImage: BufferedImage, message: String, password: String): BufferedImage {
    val data = encodeDecodePassword(message.encodeToByteArray(), password.encodeToByteArray()) + byteArrayOf(0, 0, 3)
    val pow = arrayOf(128, 64, 32, 16, 8, 4, 2)
    var i = 0
    var j = 0
    for (byte in data) {
        var tempByte = byte.toInt()
        for (k in pow) {
            if (j == bImage.width) {
                i++; j = 0
            }
            bImage.setRGB(j, i, proceedColor(Color(bImage.getRGB(j++, i)), tempByte / k))
            tempByte %= k
        }
        if (j == bImage.width) {
            i++; j = 0
        }
        bImage.setRGB(j, i, proceedColor(Color(bImage.getRGB(j++, i)), tempByte % 2))
    }
    return bImage
}

fun encodeDecodePassword(message: ByteArray, password: ByteArray): ByteArray {
    for (i in message.indices) message[i] = message[i].xor(password[i % password.size])
    return message
}

fun proceedColor(color: Color, value: Int): Int =
        Color(color.red, color.green,
                if (value == 1) color.blue + 1 - (color.blue % 2) else color.blue - (color.blue % 2)).rgb

fun show() {
    val input = println("Input image file:").let { readLine()!! }
    val password = println("Password:").let { readLine()!! }
    try {
        val bImage = ImageIO.read(File(input))
        println("Message:\n ${getMessage(bImage, password)}")
    } catch (e: Exception) {
        println(e.message)
    }
}

fun getMessage(bImage: BufferedImage, password: String): String {
    val data = mutableListOf<Int>()
    val pow = arrayOf(128, 64, 32, 16, 8, 4, 2, 1)
    var firstZero = false
    var secondZero = false
    var i = 0
    var j = 0
    while (true) {
        var tmp = 0
        for (k in pow) {
            if (j == bImage.width) {
                i++; j = 0
            }
            tmp += (Color(bImage.getRGB(j++, i)).blue % 2) * k
        }
        when {
            firstZero && secondZero && tmp == 3 -> break
            firstZero && tmp == 0 -> secondZero = true
            tmp == 0 -> firstZero = true
            else -> {
                firstZero = false; secondZero = false
            }
        }
        data.add(tmp)
    }
    data.removeAt(data.lastIndex)
    data.removeAt(data.lastIndex)
    return encodeDecodePassword(data.map { it.toByte() }.toByteArray(), password.encodeToByteArray()).toString(Charsets.UTF_8)
}