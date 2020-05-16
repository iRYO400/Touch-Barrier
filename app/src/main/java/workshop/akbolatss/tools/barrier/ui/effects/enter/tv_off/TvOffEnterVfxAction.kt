package workshop.akbolatss.tools.barrier.ui.effects.enter.tv_off

import android.view.View
import android.widget.FrameLayout
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfxAction

class TvOffEnterVfxAction(
) : IEnterVfxAction {

    private lateinit var targetView: View

    private val context by lazy {
        targetView.context
    }

    override fun prepare(targetView: View) {
        this.targetView = targetView
    }

    override fun apply(onStart: () -> Unit, onEnd: () -> Unit) {
        (targetView as FrameLayout).addView(TvOffView(context))
    }

}
