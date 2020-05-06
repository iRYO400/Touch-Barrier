package workshop.akbolatss.tools.barrier.notification

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import workshop.akbolatss.tools.barrier.accessibility.AccessibilityServiceHelper
import workshop.akbolatss.tools.barrier.accessibility.BarrierAccessibilityService
import workshop.akbolatss.tools.barrier.utils.IntentKeys

class BarrierPreferences(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        const val SHARED_BARRIER_ENABLED = "_barrierEnabled"

    }

    fun isEnabled(): Boolean {
        val isEnabled = sharedPreferences.getBoolean(SHARED_BARRIER_ENABLED, false)
        val isReallyEnabled = isAccessibleServiceEnabled()

        if (isEnabled && isReallyEnabled)
            return true

        return toggle(isEnabled)
    }

    fun isAccessibleServiceEnabled(): Boolean {
        return AccessibilityServiceHelper.isAccessibilityServiceEnabled(
            context, BarrierAccessibilityService::class.java
        )
    }

    fun toggle(enable: Boolean): Boolean {
        if (enable)
            enableBarrier()
        else
            disableBarrier()

        sharedPreferences.edit().apply {
            putBoolean(SHARED_BARRIER_ENABLED, enable)
            apply()
        }
        return enable
    }

    private fun enableBarrier() {
        val intent = Intent(IntentKeys.INTENT_FILTER_ACCESSIBILITY)
        intent.putExtra(IntentKeys.INTENT_TOGGLE_BARRIER, true)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    private fun disableBarrier() {
        val intent = Intent(IntentKeys.INTENT_FILTER_ACCESSIBILITY)
        intent.putExtra(IntentKeys.INTENT_TOGGLE_BARRIER, false)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

}
