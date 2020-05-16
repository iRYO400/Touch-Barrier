package workshop.akbolatss.tools.barrier.ui.effects.enter.tv_off

import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfx
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfxAction

data class TvOffEnterVfx(
    override val id: Int = 2,
    override val nameRes: Int = R.string.enter_vfx_tv_off_name,
    override val descriptionRes: Int = R.string.enter_vfx_tv_off_desc
) : IEnterVfx {

    override fun getAction(): IEnterVfxAction {
        return TvOffEnterVfxAction()
    }
}
