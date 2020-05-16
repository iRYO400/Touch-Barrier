package workshop.akbolatss.tools.barrier.domain.usecase

import kotlinx.coroutines.CoroutineScope
import workshop.akbolatss.tools.barrier.base.BaseUseCase
import workshop.akbolatss.tools.barrier.base.None
import workshop.akbolatss.tools.barrier.base.resources.Either
import workshop.akbolatss.tools.barrier.base.resources.Failure
import workshop.akbolatss.tools.barrier.preference.VfxPreferences
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfx

class GetEnterVfxList(
    private val vfxPreferences: VfxPreferences
): BaseUseCase<GetEnterVfxList.Params, List<IEnterVfx>>(){

    override suspend fun run(
        params: Params,
        scope: CoroutineScope
    ): Either<Failure, List<IEnterVfx>> {
        return Either.Right(vfxPreferences.getAll())
    }

    data class Params(val none: None = None())
}
