package workshop.akbolatss.tools.barrier.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_SECRET
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.accessibility.NotificationBroadcastReceiver
import workshop.akbolatss.tools.barrier.ui.RootActivity
import workshop.akbolatss.tools.barrier.utils.IntentKeys.Companion.INTENT_ACTION_TOGGLE
import workshop.akbolatss.tools.barrier.utils.IntentKeys.Companion.INTENT_TOGGLE_BARRIER
import workshop.akbolatss.tools.barrier.utils.NotificationKeys.Companion.NOTIFICATION_ID

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
            channel.lockscreenVisibility = VISIBILITY_SECRET
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
        return NOTIFICATION_ID
    }

    fun Context.enableNotification() {
        val icon = BitmapFactory.decodeResource(resources, R.drawable.ic_stat_name)

        val notificationBuilder = NotificationCompat.Builder(this, getNotificationChannelId(this))
            .setContentTitle(getString(R.string.title_notification))
            .setLargeIcon(icon)
            .setSound(null)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setVisibility(VISIBILITY_SECRET)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(getToggleAction(this))
            .addAction(getSettingsAction(this))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(getNotificationId(), notificationBuilder.build())
    }

    private fun getToggleAction(context: Context): NotificationCompat.Action? {
        val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            action = INTENT_ACTION_TOGGLE
            putExtra(INTENT_TOGGLE_BARRIER, true)
        }

        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Action.Builder(
            R.drawable.ic_layers_active_24dp,
            context.getString(R.string.notification_panel_toggle), pendingIntent
        )
        return builder.build()
    }

    private fun getSettingsAction(context: Context): NotificationCompat.Action? {
        val intent = Intent(context, RootActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val builder = NotificationCompat.Action.Builder(
            R.drawable.ic_settings_24dp,
            context.getString(R.string.notification_panel_settings),
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        )
        return builder.build()
    }

    fun Context.disableNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(getNotificationId())
    }
}
