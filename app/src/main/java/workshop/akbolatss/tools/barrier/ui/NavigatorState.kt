package workshop.akbolatss.tools.barrier.ui

import androidx.fragment.app.Fragment
import workshop.akbolatss.tools.barrier.ui.effects.enter.EnterVfxSelectFragment
import workshop.akbolatss.tools.barrier.ui.settings.SettingsFragment

sealed class NavigatorState(
    val fragmentTag: String,
    val javaClass: Class<out Fragment>
) {

    object Settings : NavigatorState(
        SettingsFragment::class.java.simpleName,
        SettingsFragment::class.java
    )

    object EnterVfxSelector : NavigatorState(
        EnterVfxSelectFragment::class.java.simpleName,
        EnterVfxSelectFragment::class.java
    )
}
