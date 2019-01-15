package wei.mark.standouttest.ui.settings

import android.annotation.TargetApi
import android.app.Activity
import android.app.NotificationManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.settings_fragment.*
import wei.mark.standouttest.BarrierApplication
import wei.mark.standouttest.R
import wei.mark.standouttest.accessibility.AccessibilityServiceHelper.isAccessibilityServiceEnabled
import wei.mark.standouttest.accessibility.BarrierAccessibilityService
import wei.mark.standouttest.ui.common.SpinnerItemSelectedImpl
import wei.mark.standouttest.ui.intro.adapter.ActionType
import wei.mark.standouttest.ui.lock_screen.ScreenLockActivity
import wei.mark.standouttest.ui.lock_screen.ScreenLockType
import wei.mark.standouttest.utils.HawkKeys
import wei.mark.standouttest.utils.IntentKeys
import wei.mark.standouttest.utils.IntentKeys.Companion.INTENT_FILTER_ACCESSIBILITY
import wei.mark.standouttest.utils.IntentKeys.Companion.INTENT_TOGGLE_BARRIER
import wei.mark.standouttest.utils.NotificationExt.createDefaultNotificationChannel
import wei.mark.standouttest.utils.NotificationExt.disableNotification
import wei.mark.standouttest.utils.NotificationExt.enableNotification
import wei.mark.standouttest.utils.NotificationKeys.Companion.NOTIFICATION_ID
import wei.mark.standouttest.utils.showSnackbarError

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()

        private const val REQUEST_SET_LOCK = 102
    }

    private var isSpinnerInitializing = true

    private lateinit var spinnerListener: AdapterView.OnItemSelectedListener

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
        BarrierAccessibilityService.barrierState.observe(this, Observer {
            var barrierState = it
            if (barrierState == null)
                barrierState = false

            switch_barrier.isChecked = barrierState
        })
    }

    /**
     * Init default values
     * TODO:move to SettingsActivity
     */
    private fun setDefault() {
        isSpinnerInitializing = true
        if (Hawk.contains(HawkKeys.LOCK_TYPE_INDEX)) {
            val type = Hawk.get(HawkKeys.LOCK_TYPE_INDEX, ScreenLockType.NONE)
            spinner.setSelection(type.getPosition(), false)
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

        spinnerListener = object : SpinnerItemSelectedImpl() {

            override fun onItemSelected(position: Int) {
                if (isSpinnerInitializing) {
                    isSpinnerInitializing = false
                    return
                }
                if (position == 0)
                    clearPreviousLockProperties()
                else if (position == 1 || position == 2)
                    screen_lock.performClick()

                screen_lock.isEnabled = position != 0
            }
        }
        spinner.onItemSelectedListener = spinnerListener

        screen_lock.setOnClickListener {
            val intent = Intent(activity, ScreenLockActivity::class.java)
            intent.putExtra(IntentKeys.LOCK_TYPE_NEW, spinner.selectedItemPosition)
            startActivityForResult(intent, REQUEST_SET_LOCK)
        }
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

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
                Hawk.put(HawkKeys.LOCK_TYPE_INDEX, getLockTypeByPosition(spinner.selectedItemPosition))
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showSnackbarError(coordinator, getString(R.string.error_did_not_set_lock))
                setDefault()
            }
        }
    }

    private fun getLockTypeByPosition(position: Int): ScreenLockType {
        var type: ScreenLockType = ScreenLockType.NONE
        when (position) {
            1 -> type = ScreenLockType.PIN
            2 -> type = ScreenLockType.PATTERN
        }
        return type
    }
}
