package wei.mark.standouttest.utils

interface WindowKeys {
    companion object {
        const val MAIN_WINDOW_ID = 101
    }
}

interface IntentKeys {
    companion object {
        const val LOCK_TYPE_NEW = "lockTypeNew"

    }
}

interface HawkKeys {

    companion object {
        const val CLOSE_ON_ACTIVATION = "ShouldCloseOnActivation"
        const val CLOSE_ON_UNLOCK = "ShouldCloseOnUnlock"

        const val LOCK_TYPE_INDEX = "LockTypeIndex"



        const val PATTERN_DOTS = "patternDots"
        const val PIN_CODE = "pinCode"
    }
}
