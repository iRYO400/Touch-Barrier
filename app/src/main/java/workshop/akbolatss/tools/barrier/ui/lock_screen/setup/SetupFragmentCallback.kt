package workshop.akbolatss.tools.barrier.ui.lock_screen.setup

import workshop.akbolatss.tools.barrier.ui.lock_screen.BackCallback

interface SetupFragmentCallback : BackCallback {
    fun onSelectedNone()
    fun onSelectedPin()
    fun onSelectedPattern()
    fun onSelectedPassword()
    fun onSelectedFingerprint()
}