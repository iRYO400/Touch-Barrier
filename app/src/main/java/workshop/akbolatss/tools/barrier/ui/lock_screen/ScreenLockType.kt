package workshop.akbolatss.tools.barrier.ui.lock_screen

import workshop.akbolatss.tools.barrier.R

enum class ScreenLockType(private val resourceName: Int) {
    NONE(R.string.none),
    PIN(R.string.pin),
    PATTERN(R.string.pattern);

    public fun getName(): Int {
        return resourceName
    }
}