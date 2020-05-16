package workshop.akbolatss.tools.barrier.ui.effects.enter

import workshop.akbolatss.tools.barrier.domain.usecase.GetEnterVfxList
import workshop.akbolatss.tools.barrier.domain.usecase.GetSelectedEnterVfx
import workshop.akbolatss.tools.barrier.domain.usecase.SelectEnterVfx

data class EnterVfxInteractors(
    val getEnterVfxList: GetEnterVfxList,
    val getSelectedEnterVfx: GetSelectedEnterVfx,
    val selectEnterVfx: SelectEnterVfx
)
