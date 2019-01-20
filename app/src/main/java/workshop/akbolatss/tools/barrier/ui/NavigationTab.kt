package workshop.akbolatss.tools.barrier.ui

import androidx.fragment.app.Fragment
import workshop.akbolatss.tools.barrier.ui.intro.IntroFragment
import workshop.akbolatss.tools.barrier.ui.settings.SettingsFragment

enum class NavigationTab(val fragmentTag: String?, val navigationTabFactory: NavigationTabFactory) {
    TUTORIAL(IntroFragment::class.java.canonicalName,
            object : NavigationTabFactory {
                override fun newInstance(): Fragment {
                    return IntroFragment.newInstance()
                }
            }),
    SETTINGS(SettingsFragment::class.java.canonicalName,
            object : NavigationTabFactory {
                override fun newInstance(): Fragment {
                    return SettingsFragment.newInstance()
                }
            })
}
