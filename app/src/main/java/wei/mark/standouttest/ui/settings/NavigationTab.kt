package wei.mark.standouttest.ui.settings

import android.support.v4.app.Fragment
import wei.mark.standouttest.ui.intro.IntroFragment

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
