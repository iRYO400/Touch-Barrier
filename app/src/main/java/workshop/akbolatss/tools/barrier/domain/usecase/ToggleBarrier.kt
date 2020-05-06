package workshop.akbolatss.tools.barrier.domain.usecase

import kotlinx.coroutines.CoroutineScope
import workshop.akbolatss.tools.barrier.base.BaseUseCase
import workshop.akbolatss.tools.barrier.base.resources.Either
import workshop.akbolatss.tools.barrier.base.resources.Failure
import workshop.akbolatss.tools.barrier.notification.BarrierPreferences

class ToggleBarrier(
    private val barrierPreferences: BarrierPreferences
) : BaseUseCase<ToggleBarrier.Params, Boolean>() {

    override suspend fun run(params: Params, scope: CoroutineScope): Either<Failure, Boolean> {
        return if (barrierPreferences.isAccessibleServiceEnabled()) {
            Either.Right(barrierPreferences.toggle(params.enable))
        } else
            Either.Left(Failure.AccessibleServiceDisabled)
    }

    data class Params(val enable: Boolean)
}
