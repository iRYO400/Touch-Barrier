package workshop.akbolatss.tools.barrier.ui.settings

import android.annotation.TargetApi
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.settings_fragment.*
import workshop.akbolatss.tools.barrier.BarrierApplication
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.accessibility.AccessibilityServiceHelper.isAccessibilityServiceEnabled
import workshop.akbolatss.tools.barrier.accessibility.BarrierAccessibilityService
import workshop.akbolatss.tools.barrier.ui.SettingsActivity
import workshop.akbolatss.tools.barrier.ui.intro.adapter.ActionType
import workshop.akbolatss.tools.barrier.ui.lock_screen.ScreenLockListActivity
import workshop.akbolatss.tools.barrier.ui.lock_screen.ScreenLockType
import workshop.akbolatss.tools.barrier.utils.HawkKeys
import workshop.akbolatss.tools.barrier.utils.IntentKeys
import workshop.akbolatss.tools.barrier.utils.IntentKeys.Companion.INTENT_FILTER_ACCESSIBILITY
import workshop.akbolatss.tools.barrier.utils.IntentKeys.Companion.INTENT_TOGGLE_BARRIER
import workshop.akbolatss.tools.barrier.utils.NotificationExt.createDefaultNotificationChannel
import workshop.akbolatss.tools.barrier.utils.NotificationExt.disableNotification
import workshop.akbolatss.tools.barrier.utils.NotificationExt.enableNotification
import workshop.akbolatss.tools.barrier.utils.NotificationKeys.Companion.NOTIFICATION_ID
import workshop.akbolatss.tools.barrier.utils.showSnackbarError

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()

        private const val REQUEST_SET_LOCK = 102
    }

    private lateinit var viewModel: SettingsViewModel

    private lateinit var callback: SettingsFragmentCallback

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = (context as SettingsActivity)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)

        setDefault()
        setListeners()
        setObservers()
    }

    /**
     * Observers
     */
    private fun setObservers() {
        BarrierAccessibilityService.isBarrierEnabled.observe(this, Observer {
            var barrierState = it
            if (barrierState == null)
                barrierState = false

            switch_barrier.isChecked = barrierState
        })
    }

    /**
     * Init default values
     */
    private fun setDefault() {
        if (Hawk.contains(HawkKeys.LOCK_TYPE_INDEX)) {
            val type = Hawk.get(HawkKeys.LOCK_TYPE_INDEX, ScreenLockType.NONE)
            selected_lock.text = getString(type.getName())
        } else {
            Hawk.put(HawkKeys.LOCK_TYPE_INDEX, ScreenLockType.NONE)
        }

        if (Hawk.contains(HawkKeys.CLOSE_ON_ACTIVATION))
            switch_close_on_activation.isChecked = Hawk.get(HawkKeys.CLOSE_ON_ACTIVATION)
        else
            Hawk.put(HawkKeys.CLOSE_ON_ACTIVATION, false)


        if (Hawk.contains(HawkKeys.CLOSE_ON_UNLOCK))
            switch_close_on_unlock.isChecked = Hawk.get(HawkKeys.CLOSE_ON_UNLOCK)
        else
            Hawk.put(HawkKeys.CLOSE_ON_UNLOCK, true)

        if (!Hawk.contains(HawkKeys.NOTIFY_CHANNEL_CREATED)) {
            Hawk.put(HawkKeys.NOTIFY_CHANNEL_CREATED, true)
            createDefaultNotificationChannel(activity!!)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            switch_notify_bar.isChecked = isNotificationActive()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun isNotificationActive(): Boolean {
        var active = false
        val manager = activity!!.getSystemService(NotificationManager::class.java)
        for (statusBarNotification in manager.activeNotifications) {
            if (statusBarNotification.id == NOTIFICATION_ID) {
                active = true
                break
            }
        }
        return active
    }

    private fun setListeners() {
        switch_barrier.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            if (isChecked)
                enableBarrier()
            else
                disableBarrier()
        }

        switch_notify_bar.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            if (isChecked)
                showNotificationPanel()
            else
                hideNotificationPanel()
        }

        switch_close_on_activation.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            Hawk.put(HawkKeys.CLOSE_ON_ACTIVATION, isChecked)
        }

        switch_close_on_unlock.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            Hawk.put(HawkKeys.CLOSE_ON_UNLOCK, isChecked)
        }

        switch_perm_draw_over.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            if (!isChecked && BarrierApplication.instance.canDrawOverApps()) {
                callback.showSnackbar(ActionType.OPEN_DRAW_OVER_SETTINGS, getString(R.string.try_deny_permission))
                switch_perm_draw_over.isChecked = !isChecked
            } else
                openDrawOverSettings()
        }

        switch_perm_accessibil.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            if (!isChecked && isAccessibilityServiceEnabled(activity, BarrierAccessibilityService::class.java)) {
                callback.showSnackbar(ActionType.OPEN_ACCESSIBILITY_SETTINGS, getString(R.string.try_deny_permission))
                switch_perm_accessibil.isChecked = !isChecked
            } else
                openAccessibilitySettings()
        }

        screen_lock.setOnClickListener {
            val intent = Intent(activity, ScreenLockListActivity::class.java) //TODO: There is hidden old lock settings
            startActivityForResult(intent, REQUEST_SET_LOCK)
        }
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    @Deprecated("It's not needed")
    private fun openDrawOverSettings() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${activity!!.packageName}"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    private fun showNotificationPanel() {
        enableNotification(activity!!)
    }

    private fun hideNotificationPanel() {
        disableNotification(activity!!)
    }

    private fun clearPreviousLockProperties() {
        Hawk.delete(HawkKeys.PATTERN_DOTS)
        Hawk.delete(HawkKeys.PIN_CODE)
    }

    private fun enableBarrier() {
        if (!isAccessibilityServiceEnabled(activity, BarrierAccessibilityService::class.java)
        ) {
            showPermissionSettings()
        } else {
            val intent = Intent(INTENT_FILTER_ACCESSIBILITY)
            intent.putExtra(INTENT_TOGGLE_BARRIER, true)
            LocalBroadcastManager.getInstance(activity!!)
                    .sendBroadcast(intent)

            if (switch_close_on_activation.isChecked)
                activity!!.finish()
        }
    }

    private fun disableBarrier() {
        val intent = Intent(INTENT_FILTER_ACCESSIBILITY)
        intent.putExtra(INTENT_TOGGLE_BARRIER, false)
        LocalBroadcastManager.getInstance(activity!!)
                .sendBroadcast(intent)
    }

    private fun showPermissionSettings() {
        switch_barrier.isChecked = false
        callback.scrollView()
        val animationShake = AnimationUtils.loadAnimation(activity!!, R.anim.anim_shake)
        perm_info.startAnimation(animationShake)
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23)
            switch_perm_draw_over.isChecked = BarrierApplication.instance.canDrawOverApps()
        else
            switch_perm_draw_over.visibility = View.GONE

        switch_perm_accessibil.isChecked = isAccessibilityServiceEnabled(activity, BarrierAccessibilityService::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SET_LOCK) {
            if (resultCode == Activity.RESULT_OK) {
                val screenLockType: ScreenLockType = data!!.getSerializableExtra(IntentKeys.SCREEN_LOCK_TYPE) as ScreenLockType
                selected_lock.text = getString(screenLockType.getName())
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showSnackbarError(coordinator, getString(R.string.error_did_not_set_lock))
            }
        }
    }
}
