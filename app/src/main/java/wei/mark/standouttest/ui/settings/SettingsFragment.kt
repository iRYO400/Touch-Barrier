package wei.mark.standouttest.ui.settings

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.settings_fragment.*
import wei.mark.standout.StandOutWindow
import wei.mark.standouttest.FullScreenWindow

import wei.mark.standouttest.R
import wei.mark.standouttest.ui.common.SpinnerItemSelectedImpl
import wei.mark.standouttest.ui.lock_screen.ScreenLockActivity
import wei.mark.standouttest.ui.lock_screen.ScreenLockType
import wei.mark.standouttest.utils.HawkKeys
import wei.mark.standouttest.utils.IntentKeys
import wei.mark.standouttest.utils.WindowKeys
import wei.mark.standouttest.utils.showSnackbarError

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()

        private const val REQUEST_CODE = 101
        private const val REQUEST_SET_LOCK = 102
    }

    private var isSpinnerInitializing = true

    private lateinit var spinnerListener: AdapterView.OnItemSelectedListener

    private lateinit var viewModel: SettingsViewModel

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

            Hawk.put(HawkKeys.CLOSE_ON_ACTIVATION, isChecked)
        }

        switch_close_on_unlock.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            Hawk.put(HawkKeys.CLOSE_ON_UNLOCK, isChecked)
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

    private fun clearPreviousLockProperties() {
        Hawk.delete(HawkKeys.PATTERN_DOTS)
        Hawk.delete(HawkKeys.PIN_CODE)
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
        StandOutWindow.show(activity, FullScreenWindow::class.java, WindowKeys.MAIN_WINDOW_ID)

        if (Hawk.contains(HawkKeys.CLOSE_ON_ACTIVATION))
            if (Hawk.get(HawkKeys.CLOSE_ON_ACTIVATION))
                activity!!.finish()
    }

    private fun showInvisibleBarrier() {
        StandOutWindow.showInInvisible(activity, FullScreenWindow::class.java, WindowKeys.MAIN_WINDOW_ID)
    }

    private fun closeBarrier() {
        if (isWindowServiceActive())
            StandOutWindow.close(activity, FullScreenWindow::class.java, WindowKeys.MAIN_WINDOW_ID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
            // if so check once again if we have permission */
                if (Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(activity)) {
                    setListeners()
                    setObservers()
                } else
                    activity!!.finish()
            else {
                activity!!.finish()
            }
        }
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

    private fun isWindowServiceActive(): Boolean {
        return (StandOutWindow.isMyServiceRunning(activity, FullScreenWindow::class.java))
    }
}
