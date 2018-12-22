package wei.mark.standouttest.ui.lock_screen

enum class ScreenLockType(private val typeName: String,
                          private val position: Int) {
    NONE("none", 0),
    PIN("pinCode", 1),
    PATTERN("pattern", 2);

    override fun toString(): String {
        return typeName
    }

    public fun getPosition(): Int {
        return position
    }
}