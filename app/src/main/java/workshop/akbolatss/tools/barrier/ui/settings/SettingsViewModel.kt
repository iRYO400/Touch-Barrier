package workshop.akbolatss.tools.barrier.ui.settings

import androidx.lifecycle.MutableLiveData
import workshop.akbolatss.tools.barrier.base.BaseViewModel
import workshop.akbolatss.tools.barrier.base.resources.Failure
import workshop.akbolatss.tools.barrier.base.resources.onFailure
import workshop.akbolatss.tools.barrier.base.resources.onSuccess
import workshop.akbolatss.tools.barrier.domain.usecase.*
import workshop.akbolatss.tools.barrier.ui.SettingsInteractors
import workshop.akbolatss.tools.barrier.utils.livedata.Event

class SettingsViewModel(
    private val interactors: SettingsInteractors
) : BaseViewModel() {

    val isBarrierEnabled = MutableLiveData<Boolean>()
    val toggleBarrierError = MutableLiveData<Event<Boolean>>()
    val isNotificationPanelEnabled = MutableLiveData<Boolean>()

    val isAccessibilityServiceEnabled = MutableLiveData<Boolean>()

    val isCloseOnActivationEnabled = MutableLiveData<Boolean>()

    init {
//        getBarrierState()
        getNotificationState()
        getAccessibilityServiceState()
        getCloseOnActivationState()
    }

//    private fun getBarrierState() {
//        executeUseCase { scope ->
//            interactors.getBarrierState(scope, GetBarrierState.Params())
//                .onSuccess {
//                    isBarrierEnabled.value = it
//                }
//        }
//    }

    private fun getNotificationState() {
        executeUseCase { scope ->
            interactors.getNotificationPanelState(scope, GetNotificationPanelState.Params())
                .onSuccess {
                    isNotificationPanelEnabled.value = it
                }
        }
    }

    private fun getAccessibilityServiceState() {
        executeUseCase { scope ->
            interactors.getAccessibilityServiceState(scope, GetAccessibilityServiceState.Params())
                .onSuccess {
                    isAccessibilityServiceEnabled.value = it
                }
        }
    }

    private fun getCloseOnActivationState() {
        executeUseCase { scope ->
            interactors.getCloseOnActivationState(scope, GetCloseOnActivationState.Params())
                .onSuccess {
                    isCloseOnActivationEnabled.value = it
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
                        toggleBarrierError.value = Event(true)

                    isBarrierEnabled.value = false
                }
        }
    }

    fun toggleCloseOnActivation(isEnabled: Boolean) {
        executeUseCase { scope ->
            interactors.toggleCloseOnActivation(scope, ToggleCloseOnActivation.Params(isEnabled))
                .onSuccess {
                    isCloseOnActivationEnabled.value = it
                }
        }
    }

    fun toggleAccessibilityService(isEnabled: Boolean) {
        executeUseCase { scope ->
            interactors.toggleAccessibilityService(
                scope,
                ToggleAccessibilityService.Params(isEnabled)
            ).onSuccess {
                isAccessibilityServiceEnabled.value = it
            }
        }
    }

}
