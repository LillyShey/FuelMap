package com.hpk.domain.usecases.base

class LocationResultCallbacks<T>(
    val onSuccess: ((T) -> Unit)? = null,
    val onError: ((String) -> Unit)? = null,
    val onNoAnyLocationProvider: ((Unit) -> Unit)? = null,
    val isLoading: ((Boolean) -> Unit)? = null
)
