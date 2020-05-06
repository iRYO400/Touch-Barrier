package workshop.akbolatss.tools.barrier.preference

import android.content.SharedPreferences

class AdditionalPreferences(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val SHARED_CLOSE_ON_ACTIVATION = "_closeOnActivation"
    }

    //region Close on Activation
    fun isCloseOnActivationEnabled(): Boolean {
        return sharedPreferences.getBoolean(SHARED_CLOSE_ON_ACTIVATION, false)
    }

    fun toggleCloseOnActivation(enable: Boolean): Boolean {
        sharedPreferences.edit().apply {
            putBoolean(SHARED_CLOSE_ON_ACTIVATION, enable)
            apply()
        }
        return enable
    }
    //endregion

}
