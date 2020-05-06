package workshop.akbolatss.tools.barrier.ui.settings

import androidx.lifecycle.MutableLiveData
import workshop.akbolatss.tools.barrier.base.BaseViewModel
import workshop.akbolatss.tools.barrier.base.resources.onSuccess
import workshop.akbolatss.tools.barrier.domain.usecase.GetNotificationPanelState
import workshop.akbolatss.tools.barrier.domain.usecase.ToggleNotificationPanel
import workshop.akbolatss.tools.barrier.ui.SettingsInteractors

class SettingsViewModel(
    private val interactors: SettingsInteractors
) : BaseViewModel() {

    val isNotificationPanelEnabled = MutableLiveData<Boolean>()

    init {
        getNotificationState()
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

}
