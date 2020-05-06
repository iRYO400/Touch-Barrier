package workshop.akbolatss.tools.barrier.di.module

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val persistenceModule = module {
    single {
        getSharedPreference(androidContext())
    }
}

private fun getSharedPreference(context: Context): SharedPreferences =
    context.getSharedPreferences("_sharedPrefs", Context.MODE_PRIVATE)
