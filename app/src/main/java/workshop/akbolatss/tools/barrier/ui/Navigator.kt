package workshop.akbolatss.tools.barrier.ui

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import workshop.akbolatss.tools.barrier.R

class Navigator(activity: FragmentActivity) {

    private var fragmentManager: FragmentManager = activity.supportFragmentManager

    private var previousNavigationStateTag: String? = null

    fun toSelectEnterVfx() {
        loadFragmentByNavigationState(NavigatorState.EnterVfxSelector)
    }

    fun toSettings() {
        loadFragmentByNavigationState(NavigatorState.Settings)
    }

    private fun loadFragmentByNavigationState(state: NavigatorState) {
        detachCurrentFragment()

        val newFragment = fragmentManager.findFragmentByTag(state.fragmentTag)
        if (newFragment == null) {
            val newFragmentInstance = state.javaClass.newInstance()
            fragmentManager.beginTransaction().apply {
                add(R.id.container, newFragmentInstance, state.fragmentTag)
                commit()
            }
        } else {
            fragmentManager.beginTransaction().apply {
                attach(newFragment)
                commit()
            }
        }
        ::previousNavigationStateTag.set(state.fragmentTag)
    }

    private fun detachCurrentFragment() {
        val currentFragment =
            fragmentManager.findFragmentByTag(previousNavigationStateTag)
        currentFragment?.let {
            fragmentManager.beginTransaction().apply {
                detach(it)
                commit()
            }
        }
    }
}
