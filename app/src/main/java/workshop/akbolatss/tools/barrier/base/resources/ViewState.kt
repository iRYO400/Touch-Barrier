package workshop.akbolatss.tools.barrier.base.resources

sealed class ViewState
class Success<out T>(val data: T) : ViewState()
class Error(val failure: Failure) : ViewState()
object Loading : ViewState()
class NoInternetState<T : Any> : ViewState()


