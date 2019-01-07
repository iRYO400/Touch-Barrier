package wei.mark.standouttest.ui.settings

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_settings.*
import wei.mark.standout.StandOutWindow
import wei.mark.standouttest.FullScreenWindow
import wei.mark.standouttest.R
import wei.mark.standouttest.ui.common.SpinnerItemSelectedImpl
import wei.mark.standouttest.ui.lock_screen.ScreenLockActivity
import wei.mark.standouttest.ui.lock_screen.ScreenLockType
import wei.mark.standouttest.utils.HawkKeys.Companion.CLOSE_ON_ACTIVATION
import wei.mark.standouttest.utils.HawkKeys.Companion.CLOSE_ON_UNLOCK
import wei.mark.standouttest.utils.HawkKeys.Companion.LOCK_TYPE_INDEX
import wei.mark.standouttest.utils.HawkKeys.Companion.PATTERN_DOTS
import wei.mark.standouttest.utils.HawkKeys.Companion.PIN_CODE
import wei.mark.standouttest.utils.IntentKeys.Companion.LOCK_TYPE_NEW
import wei.mark.standouttest.utils.WindowKeys.Companion.MAIN_WINDOW_ID
import wei.mark.standouttest.utils.showSnackbarError

class SettingsActivity : AppCompatActivity() {

    private var isSpinnerInitializing = true

    private lateinit var spinnerListener: AdapterView.OnItemSelectedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            /** if not construct intent to request permission  */
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName"))
            /** request permission via start activity for result  */
            startActivityForResult(intent, REQUEST_CODE)
            return
        }

        setDefault()
        setListeners()
        setObservers()
    }

    private fun setDefault() {
        isSpinnerInitializing = true
        if (Hawk.contains(LOCK_TYPE_INDEX)) {
            val type = Hawk.get(LOCK_TYPE_INDEX, ScreenLockType.NONE)
            spinner.setSelection(type.getPosition(), false)
        }

        if (Hawk.contains(CLOSE_ON_ACTIVATION))
            switch_close_on_activation.isChecked = Hawk.get(CLOSE_ON_ACTIVATION)
        else
            Hawk.put(CLOSE_ON_ACTIVATION, false)


        if (Hawk.contains(CLOSE_ON_UNLOCK))
            switch_close_on_unlock.isChecked = Hawk.get(CLOSE_ON_UNLOCK)
        else
            Hawk.put(CLOSE_ON_UNLOCK, true)
    }

    private fun setListeners() {
        switch_barrier.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            if (isChecked)
                showVisibleBarrier()
            else
                closeBarrier()
        }

        switch_notify_bar.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            if (isChecked)
                showInvisibleBarrier()
            else
                closeBarrier()
        }

        switch_close_on_activation.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            Hawk.put(CLOSE_ON_ACTIVATION, isChecked)
        }

        switch_close_on_unlock.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            Hawk.put(CLOSE_ON_UNLOCK, isChecked)
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
            val intent = Intent(this, ScreenLockActivity::class.java)
            intent.putExtra(LOCK_TYPE_NEW, spinner.selectedItemPosition)
            startActivityForResult(intent, REQUEST_SET_LOCK)
        }
    }

    private fun clearPreviousLockProperties() {
        Hawk.delete(PATTERN_DOTS)
        Hawk.delete(PIN_CODE)
    }

    private fun setObservers() {
        FullScreenWindow.isShown.observe(this, Observer {
            var boolRef = it
            if (boolRef == null)
                boolRef = false

            switch_barrier.isChecked = boolRef
            switch_notify_bar.isChecked = boolRef
        })

        FullScreenWindow.isHidden.observe(this, Observer {
            var bool = it
            if (bool == null)
                bool = false

            switch_barrier.isChecked = !bool
        })
    }

    private fun showVisibleBarrier() {
        StandOutWindow.show(this, FullScreenWindow::class.java, MAIN_WINDOW_ID)

        if (switch_close_on_activation.isChecked)
            finish()
    }

    private fun showInvisibleBarrier() {
        StandOutWindow.showInInvisible(this, FullScreenWindow::class.java, MAIN_WINDOW_ID)
    }

    private fun closeBarrier() {
        if (isWindowServiceActive())
            StandOutWindow.close(this, FullScreenWindow::class.java, MAIN_WINDOW_ID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
            // if so check once again if we have permission */
                if (Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(this)) {
                    setListeners()
                    setObservers()
                } else
                    finish()
            else {
                finish()
            }
        }
        if (requestCode == REQUEST_SET_LOCK) {
            if (resultCode == Activity.RESULT_OK) {
                Hawk.put(LOCK_TYPE_INDEX, getLockTypeByPosition(spinner.selectedItemPosition))
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

    private fun isWindowServiceActive(): Boolean {
        return (StandOutWindow.isMyServiceRunning(this, FullScreenWindow::class.java))
    }

    companion object {
        private const val REQUEST_CODE = 101
        private const val REQUEST_SET_LOCK = 102
    }
}
