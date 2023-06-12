package com.google.maps.android.compose

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.maps.android.compose.theme.AppDatabase
import com.google.maps.android.compose.theme.LocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class MapApplication : Application() {

}
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
//@ApplicationContext application: Context
    @Provides
    fun provideDatabase(@ApplicationContext application: Context): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "AppDatabase"
        ).build()
    }

    @Provides
    fun provideLocationDao(database: AppDatabase): LocationDao {
        return database.locationDao()
    }
}
