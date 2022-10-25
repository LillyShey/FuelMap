package com.hpk.domain.exceptions

import com.hpk.domain.models.errors.ApiError

class ApiErrorException(val apiError: ApiError?) : Throwable()
