package workshop.akbolatss.tools.barrier.preference

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import workshop.akbolatss.tools.barrier.utils.NotificationExt.createNotificationChannel
import workshop.akbolatss.tools.barrier.utils.NotificationExt.disableNotification
import workshop.akbolatss.tools.barrier.utils.NotificationExt.enableNotification
import workshop.akbolatss.tools.barrier.utils.NotificationKeys

class NotificationPreferences(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        const val SHARED_NOTIFICATION_ENABLED = "_notificationPanelEnabled"

    }

    fun isEnabled(): Boolean {
        val isEnabled = sharedPreferences.getBoolean(SHARED_NOTIFICATION_ENABLED, false)
        val isReallyEnabled = isNotificationActive()


        if (isEnabled && isReallyEnabled)
            return true

        return toggle(isEnabled)
    }

    private fun isNotificationActive(): Boolean {
        var active = false
        val manager = context.getSystemService(NotificationManager::class.java)
        for (statusBarNotification in manager!!.activeNotifications) {
            if (statusBarNotification.id == NotificationKeys.NOTIFICATION_ID) {
                active = true
                break
            }
        }
        return active
    }

    fun toggle(enable: Boolean): Boolean {
        if (enable) {
            context.createNotificationChannel()
            context.enableNotification()
        } else
            context.disableNotification()

        sharedPreferences.edit().apply {
            putBoolean(SHARED_NOTIFICATION_ENABLED, enable)
            apply()
        }
        return enable
    }

}
