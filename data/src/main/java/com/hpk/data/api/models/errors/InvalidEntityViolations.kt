package com.hpk.data.api.models.errors

import com.google.gson.annotations.SerializedName
import com.hpk.data.api.models.errors.Violation

class InvalidEntityViolations(
    @SerializedName("title")
    val title: String?,
    @SerializedName("detail")
    val detail: String?,
    @SerializedName("violations")
    val violations: List<Violation>?
)
