package workshop.akbolatss.tools.barrier.ui

import workshop.akbolatss.tools.barrier.domain.usecase.GetNotificationPanelState
import workshop.akbolatss.tools.barrier.domain.usecase.ToggleNotificationPanel

data class SettingsInteractors(
    val getNotificationPanelState: GetNotificationPanelState,
    val toggleNotificationPanel: ToggleNotificationPanel
)
