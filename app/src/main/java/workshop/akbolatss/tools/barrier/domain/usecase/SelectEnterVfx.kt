package workshop.akbolatss.tools.barrier.domain.usecase

import kotlinx.coroutines.CoroutineScope
import workshop.akbolatss.tools.barrier.base.BaseUseCase
import workshop.akbolatss.tools.barrier.base.None
import workshop.akbolatss.tools.barrier.base.resources.Either
import workshop.akbolatss.tools.barrier.base.resources.Failure
import workshop.akbolatss.tools.barrier.preference.VfxPreferences
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfx

class SelectEnterVfx(
    private val vfxPreferences: VfxPreferences
) : BaseUseCase<SelectEnterVfx.Params, None>() {

    override suspend fun run(params: Params, scope: CoroutineScope): Either<Failure, None> {
        vfxPreferences.setEnterVfx(params.enterVfxApi)
        return Either.Right(None())
    }

    data class Params(val enterVfxApi: IEnterVfx)
}
