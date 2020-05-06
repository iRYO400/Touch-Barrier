package workshop.akbolatss.tools.barrier.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import workshop.akbolatss.tools.barrier.base.resources.*

abstract class BaseViewModel : ViewModel() {

    val viewState: LiveData<ViewState>
        get() = _viewState
    val failure: LiveData<Failure>
        get() = _failure

    private val _failure = MutableLiveData<Failure>()

    private val _viewState = MutableLiveData<ViewState>()

    private val handleViewState: (ViewState) -> Unit = {
        _viewState.value = it
    }

    private val handleFailure: (Failure) -> Unit = {
        Timber.e("ViewModel failure: ${it.errorMessage} by $it")
        _failure.value = it
    }

    protected fun <T> executeUseCase(
        viewState: (ViewState) -> Unit = handleViewState,
        action: suspend (CoroutineScope) -> Either<Failure, T>
    ): Job {
        return viewModelScope.launch {
            viewState.invoke(Loading)
            action(this).fold(fnL = {
                viewState.invoke(Error(it))
            }, fnR = {
                viewState.invoke(Success(it))
            })
        }
    }

}
