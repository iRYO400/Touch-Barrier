package workshop.akbolatss.tools.barrier.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import workshop.akbolatss.tools.barrier.base.resources.Either
import workshop.akbolatss.tools.barrier.base.resources.Failure

abstract class BaseUseCase<in Params, out Type> where Type : Any {

    abstract suspend fun run(params: Params, scope: CoroutineScope): Either<Failure, Type>

    open suspend operator fun invoke(
        scope: CoroutineScope,
        params: Params
    ): Either<Failure, Type> {
        return withContext(scope.coroutineContext + Dispatchers.IO) {
            try {
                run(params, this)
            } catch (e: Exception) {
                e.printStackTrace()
                Either.Left(Failure.UseCaseError)
            }
        }
    }
}

class None
