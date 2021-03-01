package com.pectolabs.todo.hilt

import android.content.Context
import androidx.room.Room
import com.pectolabs.todo.db.TaskDAO
import com.pectolabs.todo.db.TodoDatabase
import com.pectolabs.todo.repositories.MainRepository
import com.pectolabs.todo.ui.viewmodels.DashboardViewModel
import com.pectolabs.todo.ui.viewmodels.MainViewModel
import com.pectolabs.todo.utils.Constants.TODO_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideTodoDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, TodoDatabase::class.java, TODO_DATABASE_NAME).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideTaskDao(db: TodoDatabase) = db.getTaskDao()

    @Singleton
    @Provides
    fun provideMainRepository(taskDAO: TaskDAO) = MainRepository(taskDAO)

    @Singleton
    @Provides
    fun provideMainViewModel(mainRepository: MainRepository) = MainViewModel(mainRepository)

    @Singleton
    @Provides
    fun provideDashboardViewModel(mainRepository: MainRepository) =
        DashboardViewModel(mainRepository)
}