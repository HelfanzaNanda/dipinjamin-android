package com.alfardev.dipinjamin

import android.app.Application
import com.alfardev.dipinjamin.repositories.*
import com.alfardev.dipinjamin.ui.books.BookViewModel
import com.alfardev.dipinjamin.ui.checkout.CheckoutViewModel
import com.alfardev.dipinjamin.ui.create_update_book.CreateUpdateBookViewModel
import com.alfardev.dipinjamin.ui.detail_book.DetailBookViewModel
import com.alfardev.dipinjamin.ui.fragments.carts.CartViewModel
import com.alfardev.dipinjamin.ui.fragments.categories.CategoryViewModel
import com.alfardev.dipinjamin.ui.fragments.home.HomeViewModel
import com.alfardev.dipinjamin.ui.fragments.profile.ProfileViewModel
import com.alfardev.dipinjamin.ui.login.LoginViewModel
import com.alfardev.dipinjamin.ui.management_address.ManagementAddressViewModel
import com.alfardev.dipinjamin.ui.my_orders.MyOrderViewModel
import com.alfardev.dipinjamin.ui.register.RegisterViewModel
import com.alfardev.dipinjamin.ui.search.SearchViewModel
import com.alfardev.dipinjamin.ui.update_password.UpdatePasswordViewModel
import com.alfardev.dipinjamin.ui.update_profile.UpdateProfileViewModel
import com.alfardev.dipinjamin.webservices.ApiClient
import com.alfardev.dipinjamin.webservices.ApiClientTerritory
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
    single { ApiClientTerritory.instance() }
    single { FirebaseRepository() }
}

val repositoryModules = module {
    factory { CategoryRepository(get()) }
    factory { BookRepository(get()) }
    factory { UserRepository(get()) }
    factory { BannerRepository(get()) }
    factory { CartRepository(get()) }
    factory { DeliveryAddressRepository(get()) }
    factory { TerritoryRepository(get()) }
    factory { CheckoutRepository(get()) }
}

val viewModelModules = module {
    viewModel { BookViewModel(get()) }
    viewModel { DetailBookViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { CreateUpdateBookViewModel(get(), get()) }
    viewModel { ManagementAddressViewModel(get(), get()) }
    viewModel { CartViewModel(get()) }
    viewModel { SearchViewModel(get()) }

    viewModel { UpdatePasswordViewModel(get()) }
    viewModel { UpdateProfileViewModel(get()) }
    viewModel { CheckoutViewModel(get(), get()) }
    viewModel { MyOrderViewModel(get()) }

    viewModel { com.alfardev.dipinjamin.ui.fragments.books.BookViewModel(get()) }
}