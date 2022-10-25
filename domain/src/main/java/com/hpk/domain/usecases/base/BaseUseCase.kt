package com.hpk.domain.usecases.base

import com.hpk.domain.exceptions.ApiErrorException
import com.hpk.domain.exceptions.ConnectionErrorException
import kotlinx.coroutines.*

abstract class BaseUseCase<RESULT, PARAMS> : UseCase<RESULT, PARAMS> {

    operator fun invoke(
        uiDispatcher: CoroutineScope,
        result: ResultCallbacks<RESULT>,
        params: PARAMS? = null
    ): Job {
        return uiDispatcher.launch {
            withContext(Dispatchers.Main) {
                result.onLoading?.invoke(true)
                try {
                    val resultOfWork = remoteWork(params)
                    result.onSuccess?.invoke(resultOfWork)
                    result.onLoading?.invoke(false)
                } catch (e: Throwable) {
                    when (e) {
                        is ApiErrorException -> result.onError?.invoke(e)
                        is ConnectionErrorException -> result.onConnectionError?.invoke(e)
                        else -> result.onUnexpectedError?.invoke(e)
                    }
                    result.onLoading?.invoke(false)
                }
            }
        }
    }

}

class ResultCallbacks<T>(
    val onSuccess: ((T) -> Unit)? = null,
    val onLoading: ((Boolean) -> Unit)? = null,
    val onError: ((ApiErrorException) -> Unit)? = null,
    val onConnectionError: ((Throwable) -> Unit)? = null,
    val onUnexpectedError: ((Throwable) -> Unit)? = null
)

interface UseCase<RESULT, PARAMS> {
    suspend fun remoteWork(params: PARAMS?): RESULT
}
