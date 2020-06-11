package workshop.akbolatss.tools.barrier.di.module

import androidx.fragment.app.FragmentActivity
import org.koin.dsl.module
import workshop.akbolatss.tools.barrier.ui.Navigator

val navigationModule = module {
    single { (activity: FragmentActivity) ->
        Navigator(activity)
    }
}
