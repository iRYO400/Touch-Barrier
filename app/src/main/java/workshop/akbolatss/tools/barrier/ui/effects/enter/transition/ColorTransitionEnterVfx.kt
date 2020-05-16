package workshop.akbolatss.tools.barrier.ui.effects.enter.transition

import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfx
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfxAction

data class ColorTransitionEnterVfx(
    override val id: Int = 1,
    override val nameRes: Int = R.string.enter_vfx_color_transition_name,
    override val descriptionRes: Int = R.string.enter_vfx_color_transition_desc
) : IEnterVfx {

    override fun getAction(): IEnterVfxAction {
        return ColorTransitionEnterVfxAction()
    }
}
