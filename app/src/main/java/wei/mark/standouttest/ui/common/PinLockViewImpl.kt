package wei.mark.standouttest.ui.common

import com.andrognito.pinlockview.PinLockListener

public abstract class PinLockViewImpl : PinLockListener {
    override fun onEmpty() {}

    override fun onPinChange(pinLength: Int, intermediatePin: String?) {}
}
