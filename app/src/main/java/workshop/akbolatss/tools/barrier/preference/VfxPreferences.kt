package workshop.akbolatss.tools.barrier.preference

import android.content.Context
import android.view.View
import workshop.akbolatss.tools.barrier.ui.effects.enter.EnterVfxApi
import workshop.akbolatss.tools.barrier.ui.effects.enter.transition.ColorTransitionEnterVfx

class VfxPreferences(
    private val context: Context
) {

    fun getEnterVfx(targetView: View): EnterVfxApi {
        return ColorTransitionEnterVfx(context, targetView)
    }

}
