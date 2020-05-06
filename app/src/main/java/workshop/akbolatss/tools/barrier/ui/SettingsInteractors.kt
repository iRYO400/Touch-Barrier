package workshop.akbolatss.tools.barrier.ui

import workshop.akbolatss.tools.barrier.domain.usecase.GetBarrierState
import workshop.akbolatss.tools.barrier.domain.usecase.GetNotificationPanelState
import workshop.akbolatss.tools.barrier.domain.usecase.ToggleBarrier
import workshop.akbolatss.tools.barrier.domain.usecase.ToggleNotificationPanel

data class SettingsInteractors(
    val getBarrierState: GetBarrierState,
    val toggleBarrier: ToggleBarrier,
    val getNotificationPanelState: GetNotificationPanelState,
    val toggleNotificationPanel: ToggleNotificationPanel
)
