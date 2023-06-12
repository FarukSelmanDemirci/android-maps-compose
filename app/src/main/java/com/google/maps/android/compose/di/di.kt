package com.google.maps.android.compose.di

import com.google.maps.android.compose.theme.LocationDao
import com.google.maps.android.compose.theme.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AnalyticsModule {

    @Provides
    fun providesLocationRepository(
        dao: LocationDao
    ): LocationRepository = LocationRepository(dao)


}
