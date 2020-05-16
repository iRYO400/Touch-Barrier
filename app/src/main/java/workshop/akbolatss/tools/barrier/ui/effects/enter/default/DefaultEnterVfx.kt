package workshop.akbolatss.tools.barrier.ui.effects.enter.default

import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfx
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfxAction

data class DefaultEnterVfx(
    override val id: Int = -1,
    override val nameRes: Int = R.string.enter_vfx_transparent_name,
    override val descriptionRes: Int = R.string.enter_vfx_transparent_desc
) : IEnterVfx {

    override fun getAction(): IEnterVfxAction {
        TODO("Not yet implemented")
    }
}
