package workshop.akbolatss.tools.barrier.ui.effects.enter.tv_off

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import workshop.akbolatss.tools.barrier.ui.effects.enter.EnterVfxApi

class TvOffEnterVfx(
    private val context: Context,
    private val targetView: View
) : EnterVfxApi {

    override fun apply(onStart: () -> Unit, onEnd: () -> Unit) {
        (targetView as FrameLayout).addView(TvOffView(context))
    }
}
