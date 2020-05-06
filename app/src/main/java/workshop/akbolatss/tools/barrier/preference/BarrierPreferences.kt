package workshop.akbolatss.tools.barrier.preference

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import workshop.akbolatss.tools.barrier.utils.IntentKeys

class BarrierPreferences(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    fun toggle(enable: Boolean): Boolean {
        if (enable)
            enableBarrier()
        else
            disableBarrier()

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
