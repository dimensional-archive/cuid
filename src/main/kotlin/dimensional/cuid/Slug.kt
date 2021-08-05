package dimensional.cuid

public object Slug {

    /**
     * Whether the supplied string is a valid slug.
     *
     * @param slug String to validate.
     */
    public fun validate(slug: String): Boolean {
        return slug.length in 7..10
    }

    /**
     * Generates a random slug.
     */
    public fun generate(): String {
        val timestamp = System.currentTimeMillis().toString(Utils.BASE).takeLast(2)
        val counter = Utils.incrementSafely().toString(Utils.BASE).takeLast(4)
        val print = "${Utils.fingerprint.first()}${Utils.fingerprint.last()}"
        val random = Utils.getRandomBlock().takeLast(2)

        return "$timestamp$counter$print$random"
    }

}
