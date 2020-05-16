package workshop.akbolatss.tools.barrier.di.module

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import workshop.akbolatss.tools.barrier.preference.*

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

    single {
        PermissionPreferences(
            context = androidContext()
        )
    }

    single {
        VfxPreferences(
            context = androidContext(),
            sharedPreferences = get()
        )
    }
}
