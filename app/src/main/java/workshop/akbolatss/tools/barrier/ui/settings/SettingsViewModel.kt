package workshop.akbolatss.tools.barrier.ui.settings

import androidx.lifecycle.MutableLiveData
import workshop.akbolatss.tools.barrier.base.BaseViewModel
import workshop.akbolatss.tools.barrier.base.resources.Failure
import workshop.akbolatss.tools.barrier.base.resources.onFailure
import workshop.akbolatss.tools.barrier.base.resources.onSuccess
import workshop.akbolatss.tools.barrier.domain.usecase.GetBarrierState
import workshop.akbolatss.tools.barrier.domain.usecase.GetNotificationPanelState
import workshop.akbolatss.tools.barrier.domain.usecase.ToggleBarrier
import workshop.akbolatss.tools.barrier.domain.usecase.ToggleNotificationPanel
import workshop.akbolatss.tools.barrier.ui.SettingsInteractors
import workshop.akbolatss.tools.barrier.utils.livedata.Event

class SettingsViewModel(
    private val interactors: SettingsInteractors
) : BaseViewModel() {

    val isBarrierEnabled = MutableLiveData<Boolean>()
    val accessibleServiceDisabled = MutableLiveData<Event<Boolean>>()
    val isNotificationPanelEnabled = MutableLiveData<Boolean>()

    init {
        getBarrierState()
        getNotificationState()
    }

    private fun getBarrierState() {
        executeUseCase { scope ->
            interactors.getBarrierState(scope, GetBarrierState.Params())
                .onSuccess {
                    isBarrierEnabled.value = it
                }
        }
    }

    private fun getNotificationState() {
        executeUseCase { scope ->
            interactors.getNotificationPanelState(scope, GetNotificationPanelState.Params())
                .onSuccess {
                    isNotificationPanelEnabled.value = it
                }
        }
    }

    fun toggleNotificationPanel(isEnabled: Boolean) {
        executeUseCase { scope ->
            interactors.toggleNotificationPanel(scope, ToggleNotificationPanel.Params(isEnabled))
                .onSuccess {
                    isNotificationPanelEnabled.value = it
                }
        }
    }

    fun toggleBarrier(isEnabled: Boolean) {
        executeUseCase { scope ->
            interactors.toggleBarrier(scope, ToggleBarrier.Params(isEnabled))
                .onSuccess {
                    isBarrierEnabled.value = it
                }
                .onFailure {
                    if (it is Failure.AccessibleServiceDisabled)
                        accessibleServiceDisabled.value = Event(true)

                    isBarrierEnabled.value = false
                }
        }
    }

}
