package workshop.akbolatss.tools.barrier.domain.usecase

import kotlinx.coroutines.CoroutineScope
import workshop.akbolatss.tools.barrier.base.BaseUseCase
import workshop.akbolatss.tools.barrier.base.resources.Either
import workshop.akbolatss.tools.barrier.base.resources.Failure
import workshop.akbolatss.tools.barrier.preference.AdditionalPreferences

class ToggleCloseOnActivation(
    private val additionalPreferences: AdditionalPreferences
) : BaseUseCase<ToggleCloseOnActivation.Params, Boolean>() {

    override suspend fun run(params: Params, scope: CoroutineScope): Either<Failure, Boolean> {
        return Either.Right(additionalPreferences.toggleCloseOnActivation(params.enable))
    }

    data class Params(val enable: Boolean)
}
