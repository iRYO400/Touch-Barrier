package workshop.akbolatss.tools.barrier.di.module

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import workshop.akbolatss.tools.barrier.preference.AdditionalPreferences
import workshop.akbolatss.tools.barrier.preference.BarrierPreferences
import workshop.akbolatss.tools.barrier.preference.NotificationPreferences

val preferenceModule = module {
    single {
        NotificationPreferences(
            context = androidContext(),
            sharedPreferences = get()
        )
    }

    single {
        BarrierPreferences(
            context = androidContext(),
            sharedPreferences = get()
        )
    }

    single {
        AdditionalPreferences(
            sharedPreferences = get()
        )
    }
}
