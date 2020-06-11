package workshop.akbolatss.tools.barrier.domain.usecase

import kotlinx.coroutines.CoroutineScope
import workshop.akbolatss.tools.barrier.base.BaseUseCase
import workshop.akbolatss.tools.barrier.base.None
import workshop.akbolatss.tools.barrier.base.resources.Either
import workshop.akbolatss.tools.barrier.base.resources.Failure
import workshop.akbolatss.tools.barrier.ui.Navigator

class OpenEnterVfxSelector(
    private val navigator: Navigator
) : BaseUseCase<OpenEnterVfxSelector.Params, None>() {

    override suspend fun run(params: Params, scope: CoroutineScope): Either<Failure, None> {
        navigator.toSelectEnterVfx()
        return Either.Right(None())
    }

    data class Params(val none: None = None())
}
