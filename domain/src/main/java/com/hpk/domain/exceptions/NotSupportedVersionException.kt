package com.hpk.domain.exceptions

import com.hpk.domain.models.errors.ApiError


class NotSupportedVersionException(val apiError: ApiError?): Throwable()