package workshop.akbolatss.tools.barrier.di.module

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import workshop.akbolatss.tools.barrier.notification.NotificationPreferences

val preferenceModule = module {
    single {
        NotificationPreferences(
            context = androidContext(),
            sharedPreferences = get()
        )
    }
}
