package workshop.akbolatss.tools.barrier.di.module

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import workshop.akbolatss.tools.barrier.ui.settings.SettingsFragment
import workshop.akbolatss.tools.barrier.ui.settings.SettingsViewModel

val viewModelModule: Module = module {
    scope(named<SettingsFragment>()) {
        viewModel {
            SettingsViewModel(get())
        }
    }
}
