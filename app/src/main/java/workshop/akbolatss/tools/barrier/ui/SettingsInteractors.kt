package workshop.akbolatss.tools.barrier.ui

import workshop.akbolatss.tools.barrier.domain.usecase.*

data class SettingsInteractors(
    val getBarrierState: GetBarrierState,
    val toggleBarrier: ToggleBarrier,
    val getNotificationPanelState: GetNotificationPanelState,
    val toggleNotificationPanel: ToggleNotificationPanel,
    val getCloseOnActivationState: GetCloseOnActivationState,
    val toggleCloseOnActivation: ToggleCloseOnActivation,
    val getAccessibilityServiceState: GetAccessibilityServiceState,
    val toggleAccessibilityService: ToggleAccessibilityService
)
