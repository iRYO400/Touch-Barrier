package workshop.akbolatss.tools.barrier.di.module

import org.koin.dsl.module
import workshop.akbolatss.tools.barrier.domain.usecase.*
import workshop.akbolatss.tools.barrier.ui.settings.SettingsInteractors

val useCaseModule = module {
    factory {
        SettingsInteractors(
            toggleBarrier = ToggleBarrier(get(), get()),
            getNotificationPanelState = GetNotificationPanelState(get()),
            toggleNotificationPanel = ToggleNotificationPanel(get()),
            getCloseOnActivationState = GetCloseOnActivationState(get()),
            toggleCloseOnActivation = ToggleCloseOnActivation(get()),
            getAccessibilityServiceState = GetAccessibilityServiceState(get()),
            toggleAccessibilityService = ToggleAccessibilityService(get())
        )
    }
}
