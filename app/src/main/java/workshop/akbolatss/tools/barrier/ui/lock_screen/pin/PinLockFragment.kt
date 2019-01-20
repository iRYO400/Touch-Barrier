package workshop.akbolatss.tools.barrier.ui.lock_screen.pin

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andrognito.pinlockview.view.IndicatorDots
import com.andrognito.pinlockview.view.PinLockListener
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_setup_pin.*
import kotlinx.android.synthetic.main.fragment_setup_pin.view.*
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.ui.common.PinLockViewImpl
import workshop.akbolatss.tools.barrier.ui.lock_screen.ScreenLockListActivity
import workshop.akbolatss.tools.barrier.ui.lock_screen.ScreenLockType
import workshop.akbolatss.tools.barrier.utils.HawkKeys
import workshop.akbolatss.tools.barrier.utils.showSnackbarDelayAction
import workshop.akbolatss.tools.barrier.utils.showSnackbarError

class PinLockFragment : Fragment() {
    companion object {
        fun newInstance() = PinLockFragment()
    }

    private lateinit var callback: PinLockCallback

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = (context as ScreenLockListActivity)
    }

    private lateinit var pinListener: PinLockListener

    private var pinLock: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setup_pin, container, false)

        initView(view)
        return view
    }

    private fun initView(view: View) {
//        view.indicator_dots.indicatorType = IndicatorDots.IndicatorType.FILL_WITH_ANIMATION
        view.pin_lock_view.attachIndicatorDots(view.indicator_dots)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
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
            pin_lock_view.adapter.resetPinLockView()
            status.text = getString(R.string.pin_lock_submit)
        } else {
            var hasDifference = false

            if (!TextUtils.equals(pin, pinLock))
                hasDifference = true


            if (hasDifference) {
                showSnackbarError(coordinator, getString(R.string.error_set_pin_check))
                pin_lock_view.adapter.resetPinLockView()
                return
            }

            Hawk.put(HawkKeys.PIN_CODE, pinLock)
            Hawk.put(HawkKeys.LOCK_TYPE_INDEX, ScreenLockType.PIN)

            showSnackbarDelayAction(coordinator,
                    getString(R.string.set_pin_success),
                    getString(R.string.close)) {
                callback.onBack(ScreenLockType.PIN)
            }
        }
    }
}