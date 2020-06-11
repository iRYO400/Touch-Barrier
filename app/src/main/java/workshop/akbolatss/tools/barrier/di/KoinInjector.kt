package workshop.akbolatss.tools.barrier.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import workshop.akbolatss.tools.barrier.di.module.*

object KoinInjector {
    fun init(application: Application) {
        startKoin {
            androidContext(application)
            loadKoinModules(
                listOf(
                    viewModelModule,
                    useCaseModule,
                    preferenceModule,
                    persistenceModule,
                    navigationModule
                )
            )
        }
    }
}
