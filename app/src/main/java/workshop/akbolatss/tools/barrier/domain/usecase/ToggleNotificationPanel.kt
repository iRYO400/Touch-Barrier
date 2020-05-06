package workshop.akbolatss.tools.barrier.domain.usecase

import kotlinx.coroutines.CoroutineScope
import workshop.akbolatss.tools.barrier.base.BaseUseCase
import workshop.akbolatss.tools.barrier.base.resources.Either
import workshop.akbolatss.tools.barrier.base.resources.Failure
import workshop.akbolatss.tools.barrier.preference.NotificationPreferences

class ToggleNotificationPanel(
    private val notificationPreferences: NotificationPreferences
) : BaseUseCase<ToggleNotificationPanel.Params, Boolean>() {

    override suspend fun run(params: Params, scope: CoroutineScope): Either<Failure, Boolean> {
        return Either.Right(notificationPreferences.toggle(params.enable))
    }

    data class Params(val enable: Boolean)
}
