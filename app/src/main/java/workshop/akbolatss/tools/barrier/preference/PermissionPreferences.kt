package workshop.akbolatss.tools.barrier.preference

import android.content.Context
import android.content.Intent
import android.provider.Settings
import workshop.akbolatss.tools.barrier.accessibility.AccessibilityServiceHelper
import workshop.akbolatss.tools.barrier.accessibility.BarrierAccessibilityService

class PermissionPreferences(
    private val context: Context
) {

    fun toggleAccessibility(enable: Boolean): Boolean {
        if (enable && isAccessibleServiceEnabled())
            return true
        if (enable && !isAccessibleServiceEnabled()) {
            openAccessibilitySettings()
            return false
        }

        if (!enable && isAccessibleServiceEnabled()) {
            openAccessibilitySettings()
            return false
        }
        if (!enable && !isAccessibleServiceEnabled())
            return false
        return false
    }

    fun isAccessibleServiceEnabled(): Boolean {
        return AccessibilityServiceHelper.isAccessibilityServiceEnabled(
            context, BarrierAccessibilityService::class.java
        )
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
        context.startActivity(intent)
    }

}
