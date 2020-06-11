package workshop.akbolatss.tools.barrier.ui.effects.enter

import androidx.lifecycle.MutableLiveData
import workshop.akbolatss.tools.barrier.base.BaseViewModel
import workshop.akbolatss.tools.barrier.base.resources.onSuccess
import workshop.akbolatss.tools.barrier.domain.usecase.GetEnterVfxList
import workshop.akbolatss.tools.barrier.domain.usecase.GetSelectedEnterVfx
import workshop.akbolatss.tools.barrier.domain.usecase.OpenSettings
import workshop.akbolatss.tools.barrier.domain.usecase.SelectEnterVfx

class EnterVfxSelectViewModel(
    private val interactor: EnterVfxInteractors
) : BaseViewModel() {

    val enterVfxList = MutableLiveData<List<IEnterVfx>>()
    val selectedEnterVfx = MutableLiveData<IEnterVfx>()

    init {
        loadList()
    }

    private fun loadList() {
        executeUseCase { scope ->
            interactor.getEnterVfxList(scope, GetEnterVfxList.Params())
                .onSuccess {
                    enterVfxList.value = it
                    loadSelected()
                }
        }
    }

    private fun loadSelected() {
        executeUseCase { scope ->
            interactor.getSelectedEnterVfx(scope, GetSelectedEnterVfx.Params())
                .onSuccess {
                    selectedEnterVfx.value = it
                }
        }
    }

    fun enterVfxSelected(enterVfx: IEnterVfx) {
        executeUseCase { scope ->
            interactor.selectEnterVfx(scope, SelectEnterVfx.Params(enterVfx))
                .onSuccess {
                    loadList()
                }
        }
    }

    fun openSettings() {
        executeUseCase { scope ->
            interactor.openSettings(scope, OpenSettings.Params())
        }
    }

}
