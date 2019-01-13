package wei.mark.standouttest.ui.lock_screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.PatternLockView.PatternViewMode.CORRECT
import com.andrognito.patternlockview.PatternLockView.PatternViewMode.WRONG
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.pinlockview.PinLockListener
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_screen_lock.*
import wei.mark.standouttest.R
import wei.mark.standouttest.ui.common.PatterLockViewImpl
import wei.mark.standouttest.ui.common.PinLockViewImpl
import wei.mark.standouttest.utils.HawkKeys.Companion.PATTERN_DOTS
import wei.mark.standouttest.utils.HawkKeys.Companion.PIN_CODE
import wei.mark.standouttest.utils.IntentKeys.Companion.LOCK_TYPE_NEW
import wei.mark.standouttest.utils.showSnackbarError
import wei.mark.standouttest.utils.showSnackbarDelayAction

class ScreenLockActivity : AppCompatActivity() {

    private var isPatternLockType: Boolean = false

    private var patternLock: ArrayList<PatternLockView.Dot> = ArrayList()
    private var pinLock: String = ""


    private lateinit var patternListener: PatternLockViewListener
    private lateinit var pinListener: PinLockListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_lock)
        initToolbar()
        processIntent(intent)

        initView()
        setListeners()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun processIntent(intent: Intent) {
        if (intent.hasExtra(LOCK_TYPE_NEW)) {
            val type = intent.getIntExtra(LOCK_TYPE_NEW, -1)
            if (type == -1) {
                Toast.makeText(this, R.string.error_occurred, Toast.LENGTH_SHORT).show()
                return
            }

            isPatternLockType = type == 2
        }
    }

    private fun initView() {
        if (isPatternLockType) {
            pattern_lock_view.visibility = View.VISIBLE
            status.text = getString(R.string.pattern_lock_start)
        } else {
            pin_lock_view.visibility = View.VISIBLE
            indicator_dots.visibility = View.VISIBLE
            status.text = getString(R.string.pin_lock_start)
            pin_lock_view.attachIndicatorDots(indicator_dots)
        }
    }

    private fun setListeners() {

        patternListener = object : PatterLockViewImpl() {
            override fun onComplete(pattern: MutableList<PatternLockView.Dot>) {
                if (!pattern.isEmpty())
                    trySavePattern(pattern)
            }
        }
        pattern_lock_view.addPatternLockListener(patternListener)

        pinListener = object : PinLockViewImpl() {
            override fun onComplete(pin: String) {
                if (pin.length < 4)
                    return
                trySavePin(pin)
            }

        }
        pin_lock_view.setPinLockListener(pinListener)
    }

    private fun trySavePin(pin: String) {
        if (TextUtils.isEmpty(pinLock)) {
            pinLock = pin
            pin_lock_view.resetPinLockView()
            status.text = getString(R.string.pin_lock_submit)
        } else {
            var hasDifference = false

            if (!TextUtils.equals(pin, pinLock))
                hasDifference = true


            if (hasDifference) {
                showSnackbarError(coordinator, getString(R.string.error_set_pin_check))
                pin_lock_view.resetPinLockView()
                return
            }

            Hawk.put(PIN_CODE, pinLock)
            showSnackbarDelayAction(coordinator,
                    getString(R.string.set_pin_success),
                    getString(R.string.close)) {
                onBackSuccess()
            }
        }
    }

    private fun trySavePattern(pattern: MutableList<PatternLockView.Dot>) {
        if (patternLock.isEmpty()) {
            patternLock.addAll(pattern)
            pattern_lock_view.clearPattern()
            status.text = getString(R.string.pattern_lock_submit)
        } else {
            var hasDifference = false
            if (patternLock.size != pattern.size)
                hasDifference = true

            for (i in 0 until patternLock.size) {
                if (hasDifference)
                    break
                if (patternLock[i].id != pattern[i].id)
                    hasDifference = true
            }

            if (hasDifference) {
                showSnackbarError(coordinator, getString(R.string.error_set_pattern_check))
                pattern_lock_view.clearPattern()
                pattern_lock_view.setViewMode(WRONG)
                return
            }

            Hawk.put(PATTERN_DOTS, patternLock)
            pattern_lock_view.setViewMode(CORRECT)
            showSnackbarDelayAction(coordinator,
                    getString(R.string.set_pattern_success),
                    getString(R.string.close)) {
                onBackSuccess()
            }
        }
    }

    override fun onBackPressed() {
        onBackError()
    }

    private fun onBackSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun onBackError() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackError()
        }
        return super.onOptionsItemSelected(item)
    }
}
