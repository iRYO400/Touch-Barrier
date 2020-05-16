package workshop.akbolatss.tools.barrier.ui.effects.enter

import android.view.View

interface IEnterVfxAction {

    fun prepare(targetView: View)

    fun apply(onStart: () -> Unit, onEnd: () -> Unit)
}
