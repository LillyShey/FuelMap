package com.hpk.domain.repositories

import com.hpk.domain.models.common.Coordinates
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun subscribeOnLocationChanges(): Flow<Coordinates>

    suspend fun getLastKnownLocation(): Coordinates
}
