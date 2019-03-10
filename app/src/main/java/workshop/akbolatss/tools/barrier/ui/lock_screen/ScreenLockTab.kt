package workshop.akbolatss.tools.barrier.ui.lock_screen

import androidx.fragment.app.Fragment
import workshop.akbolatss.tools.barrier.ui.NavigationTabFactory
import workshop.akbolatss.tools.barrier.ui.lock_screen.pattern.PatternLockFragment
import workshop.akbolatss.tools.barrier.ui.lock_screen.setup.SetupLockFragment

enum class ScreenLockTab(val fragmentTag: String?, val navigationTabFactory: NavigationTabFactory) {
    PATTERN(PatternLockFragment::class.java.canonicalName,
            object : NavigationTabFactory {
                override fun newInstance(): Fragment {
                    return PatternLockFragment.newInstance()
                }
            }),
    SETUP(SetupLockFragment::class.java.canonicalName,
            object : NavigationTabFactory {
                override fun newInstance(): Fragment {
                    return SetupLockFragment.newInstance()
                }
            })
}
