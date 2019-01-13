package wei.mark.standouttest.ui.intro

import wei.mark.standouttest.ui.common.SnackbarCallback

interface IntroFragmentCallback : SnackbarCallback {
    fun onCloseIntro()
}