package wei.mark.standouttest.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import wei.mark.standouttest.R
import wei.mark.standouttest.accessibility.BarrierAccessibilityService
import wei.mark.standouttest.accessibility.BarrierAccessibilityService.*
import wei.mark.standouttest.ui.settings.SettingsActivity

object NotificationExt {

    /**
     * Create default notification channel
     */
    fun createDefaultNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    getNotificationChannelId(context),
                    getNotificationChannelName(context),
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableLights(false)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300)
            channel.importance = NotificationManager.IMPORTANCE_LOW
            val manager = context.getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(channel)
        }
    }

    private fun getNotificationChannelName(context: Context): CharSequence {
        return context.getString(R.string.notification_panel_name)
    }

    private fun getNotificationChannelId(context: Context): String {
        return context.packageName
    }

    private fun getNotificationId(): Int {
        return 1001
    }

    fun enableNotification(context: Context) {
        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_stat_name)

        val notificationBuilder = NotificationCompat.Builder(context, getNotificationChannelId(context))
                .setContentTitle(context.getString(R.string.title_notification))
                .setLargeIcon(icon)
                .setSound(null)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(getToggleAction(context))
                .addAction(getSettingsAction(context))

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(getNotificationId(), notificationBuilder.build())
    }

    private fun getToggleAction(context: Context): NotificationCompat.Action? {
        val isActive = BarrierAccessibilityService.isActive
        val intent = Intent(INTENT_FILTER_ACCESSIBILITY)
        intent.putExtra(INTENT_ENABLE, !isActive)
        LocalBroadcastManager.getInstance(context)
                .sendBroadcast(intent)

//        intent = Intent(INTENT_FILTER_ACTIVITY)
//        intent.putExtra(INTENT_ENABLE, !isActive)
//        LocalBroadcastManager.getInstance(context)
//                .sendBroadcast(intent)

        val builder = NotificationCompat.Action.Builder(R.drawable.ic_layers_active_24dp,
                context.getString(R.string.notification_panel_toggle),
                PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
        return builder.build()
    }

    private fun getSettingsAction(context: Context): NotificationCompat.Action? {
        val intent = Intent(context, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val builder = NotificationCompat.Action.Builder(R.drawable.ic_settings_black_24dp,
                context.getString(R.string.notification_panel_settings),
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
        return builder.build()
    }

    fun disableNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(getNotificationId())
    }
}
