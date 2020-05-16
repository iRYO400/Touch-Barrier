package workshop.akbolatss.tools.barrier.ui.effects.enter.fade

import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfx
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfxAction

data class FadeEnterVfx(
    override val id: Int = 0,
    override val nameRes: Int = R.string.enter_vfx_fade_name,
    override val descriptionRes: Int = R.string.enter_vfx_fade_desc
) : IEnterVfx {

    override fun getAction(): IEnterVfxAction {
        return FadeEnterVfxAction()
    }

}
