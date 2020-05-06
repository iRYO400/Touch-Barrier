package workshop.akbolatss.tools.barrier.utils

interface WindowKeys {
    companion object {
        const val MAIN_WINDOW_ID = 101
    }
}

interface IntentKeys {
    companion object {
        const val INTENT_FILTER_ACCESSIBILITY = "barrierServiceEnabled"
        const val INTENT_ACTION_TOGGLE = "workshop.akbolatss.app.barrier.action.toggle"
        const val INTENT_TOGGLE_BARRIER = "toggleBarrier"
        const val LOCK_TYPE_NEW = "lockTypeNew"


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

        const val CLOSE_ON_ACTIVATION = "ShouldCloseOnActivation"

        const val LOCK_TYPE_INDEX = "LockTypeIndex"

        const val NOTIFY_CHANNEL_CREATED = "isNotifyChannelCreated"

        const val PATTERN_DOTS = "patternDots"
    }
}
