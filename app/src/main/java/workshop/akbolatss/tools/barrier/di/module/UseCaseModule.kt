package workshop.akbolatss.tools.barrier.di.module

import org.koin.dsl.module
import workshop.akbolatss.tools.barrier.domain.usecase.GetBarrierState
import workshop.akbolatss.tools.barrier.domain.usecase.GetNotificationPanelState
import workshop.akbolatss.tools.barrier.domain.usecase.ToggleBarrier
import workshop.akbolatss.tools.barrier.domain.usecase.ToggleNotificationPanel
import workshop.akbolatss.tools.barrier.ui.SettingsInteractors

val useCaseModule = module {
    factory {
        SettingsInteractors(
            getBarrierState = GetBarrierState(get()),
            toggleBarrier = ToggleBarrier(get()),
            getNotificationPanelState = GetNotificationPanelState(get()),
            toggleNotificationPanel = ToggleNotificationPanel(get())
        )
    }
}
