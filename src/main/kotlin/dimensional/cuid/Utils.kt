package dimensional.cuid

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import java.net.InetAddress
import kotlin.math.pow

internal object Utils {
    val PATTERN = """[^\w\d]""".toRegex()

    const val BASE = 36
    const val BLOCK_SIZE = 4

    private val counter = atomic(0)
    private val DISCRETE_VALUES = BASE.toDouble().pow(BLOCK_SIZE.toDouble()).toInt()

    /**
     * This system's fingerprint.
     */
    val fingerprint: String by lazy {
        val (user, host) = getHostInfo()
            .split('@')

        var acc = host.length + BASE
        for (char in host.asIterable()) {
            acc += acc + char.code
        }

        "${"${user.toInt(BASE)}".zeroPad(2)}${"$acc".zeroPad(2)}"
    }

    /**
     * Returns a random block of text.
     */
    fun getRandomBlock(): String {
        return (Math.random() * DISCRETE_VALUES)
            .toInt()
            .toString(BASE)
            .zeroPad(BLOCK_SIZE)
    }

    /**
     * Safely increments the counter and returns it.
     */
    fun incrementSafely(): Int = counter.getAndUpdate {
        (if (it < DISCRETE_VALUES) it else 0) + 1
    }

    /**
     * Pads the current string with the number of zeros supplied.
     *
     * @param amount
     *   The number of zeros to pad.
     */
    fun String.zeroPad(amount: Int): String {
        val padded = "0".repeat(amount) + this
        return padded.substring(padded.length - amount)
    }

    /**
     * Returns the host info for this system.
     */
    fun getHostInfo(): String {
        return "${System.getProperty("user.name")}@${InetAddress.getLocalHost().hostName}"
    }

}
