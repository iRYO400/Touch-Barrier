package workshop.akbolatss.tools.barrier.ui.common

import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener

public abstract class PatterLockViewImpl : PatternLockViewListener {
    override fun onCleared() {}

    override fun onStarted() {}

    override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {}
}
