package dimensional.cuid

import dimensional.cuid.Utils.zeroPad

/**
 * A CUID, or collision-resistant unique-id, implementation in Kotlin.
 * Read more: [github.com/ericelliott/cuid](https://github.com/ericelliott/cuid)
 */
public object CUID {

    /**
     * Whether the provided String is a valid cuid.
     *
     * @param str The string to validate.
     */
    public fun validate(str: String): Boolean {
        return str.first() == 'c' && !(str matches Utils.PATTERN)
    }

    /**
     * Generates a collision-resistant unique id.
     *
     * @return A collision-resistant unique id.
     */
    public fun generate(): String {
        val timestamp = System.currentTimeMillis().toString(Utils.BASE)
        val counter = Utils.incrementSafely().toString(Utils.BASE).zeroPad(Utils.BLOCK_SIZE)
        val random = Utils.getRandomBlock() + Utils.getRandomBlock()

        return "c$timestamp$counter${Utils.fingerprint}$random"
    }

}
