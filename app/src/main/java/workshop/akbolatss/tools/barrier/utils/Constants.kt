package workshop.akbolatss.tools.barrier.utils

interface IntentKeys {
    companion object {
        const val INTENT_FILTER_ACCESSIBILITY = "barrierServiceEnabled"
        const val INTENT_ACTION_TOGGLE = "workshop.akbolatss.app.barrier.action.toggle"
        const val INTENT_TOGGLE_BARRIER = "toggleBarrier"

        const val SCREEN_LOCK_TYPE = "screenLockType"
    }
}

interface NotificationKeys {
    companion object {
        const val NOTIFICATION_ID = 400
    }
}

interface HawkKeys {

    companion object {

        const val IS_FIRST_START = "ShowTutorialFragment"

        const val LOCK_TYPE_INDEX = "LockTypeIndex"

        const val PATTERN_DOTS = "patternDots"
    }
}
