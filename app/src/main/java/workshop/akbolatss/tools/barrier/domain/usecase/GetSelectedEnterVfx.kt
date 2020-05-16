package workshop.akbolatss.tools.barrier.domain.usecase

import kotlinx.coroutines.CoroutineScope
import workshop.akbolatss.tools.barrier.base.BaseUseCase
import workshop.akbolatss.tools.barrier.base.None
import workshop.akbolatss.tools.barrier.base.resources.Either
import workshop.akbolatss.tools.barrier.base.resources.Failure
import workshop.akbolatss.tools.barrier.preference.VfxPreferences
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfx

class GetSelectedEnterVfx(
    private val vfxPreferences: VfxPreferences
) : BaseUseCase<GetSelectedEnterVfx.Params, IEnterVfx>() {

    override suspend fun run(params: Params, scope: CoroutineScope): Either<Failure, IEnterVfx> {
        return Either.Right(vfxPreferences.getEnterVfx())
    }

    data class Params(val none: None = None())
}
