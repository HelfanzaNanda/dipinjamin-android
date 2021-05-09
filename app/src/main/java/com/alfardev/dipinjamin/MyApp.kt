package com.alfardev.dipinjamin

import android.app.Application
import com.alfardev.dipinjamin.repositories.BookRepository
import com.alfardev.dipinjamin.repositories.CategoryRepository
import com.alfardev.dipinjamin.repositories.FirebaseRepository
import com.alfardev.dipinjamin.repositories.UserRepository
import com.alfardev.dipinjamin.ui.books.BookViewModel
import com.alfardev.dipinjamin.ui.fragments.categories.CategoryViewModel
import com.alfardev.dipinjamin.ui.fragments.home.HomeViewModel
import com.alfardev.dipinjamin.ui.login.LoginViewModel
import com.alfardev.dipinjamin.ui.register.RegisterViewModel
import com.alfardev.dipinjamin.webservices.ApiClient
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
    single { ApiClient.instance() }
    single { FirebaseRepository() }
}

val repositoryModules = module {
    factory { CategoryRepository(get()) }
    factory { BookRepository(get()) }
    factory { UserRepository(get()) }
}

val viewModelModules = module {
    viewModel { BookViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { com.alfardev.dipinjamin.ui.fragments.books.BookViewModel(get()) }
}