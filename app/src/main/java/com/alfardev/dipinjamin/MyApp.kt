package com.alfardev.dipinjamin

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.core.logger.Level

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            modules(listOf(repositoryModules, viewModelModules, retrofitModule))
        }
    }
}

val retrofitModule = module {
    //single { ApiClient.instance() }
//    single { FirebaseRepository() }
}

val repositoryModules = module {
    //factory { CycleRepository(get()) }
}

val viewModelModules = module {
    //viewModel { LoginViewModel(get()) }
}