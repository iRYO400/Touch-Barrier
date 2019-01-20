package workshop.akbolatss.tools.barrier.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_settings.*
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.ui.intro.IntroFragmentCallback
import workshop.akbolatss.tools.barrier.ui.intro.adapter.ActionType
import workshop.akbolatss.tools.barrier.ui.intro.adapter.ActionType.OPEN_ACCESSIBILITY_SETTINGS
import workshop.akbolatss.tools.barrier.ui.intro.adapter.ActionType.OPEN_DRAW_OVER_SETTINGS
import workshop.akbolatss.tools.barrier.ui.settings.SettingsFragmentCallback
import workshop.akbolatss.tools.barrier.utils.HawkKeys.Companion.IS_FIRST_START
import workshop.akbolatss.tools.barrier.utils.showSnackbarAction

class SettingsActivity : AppCompatActivity(), IntroFragmentCallback, SettingsFragmentCallback {

    private var currentTabTag: MutableLiveData<String> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (Hawk.get(IS_FIRST_START, true))
            navigateToTab(NavigationTab.TUTORIAL)
        else
            navigateToTab(NavigationTab.SETTINGS)
    }

    private fun navigateToTab(navigationTab: NavigationTab) {
        hideCurrentTab()

        val currentTabTag = navigationTab.fragmentTag
        val fragmentManager = supportFragmentManager
        var fragment = fragmentManager.findFragmentByTag(currentTabTag)
        val transaction = fragmentManager.beginTransaction()
        if (fragment == null) {
            fragment = navigationTab.navigationTabFactory.newInstance()
            transaction.add(container.id, fragment, currentTabTag)
                    .commit()
        } else {
            transaction.attach(fragment)
                    .commit()
        }
        this.currentTabTag.value = currentTabTag
    }


    private fun hideCurrentTab() {
        val currentTabTag = this.currentTabTag.value
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentByTag(currentTabTag) ?: return
        fragmentManager.beginTransaction()
                .detach(currentFragment)
                .commitNow()
    }

    override fun showSnackbar(actionType: ActionType, string: String) {
        showSnackbarAction(coordinator,
                string,
                getString(R.string.deny_permission)
        ) {
            when (actionType) {
                OPEN_ACCESSIBILITY_SETTINGS -> {
                    val goToSettings = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    goToSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
                    startActivity(goToSettings)
                }
                OPEN_DRAW_OVER_SETTINGS -> {
                    if (Build.VERSION.SDK_INT >= 23) {
                        val goToSettings = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:$packageName"))
                        goToSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
                        startActivity(goToSettings)
                    }
                }
            }
        }
    }

    override fun onCloseIntro() {
        Hawk.put(IS_FIRST_START, false)
        navigateToTab(NavigationTab.SETTINGS)
    }

    override fun scrollView() {
        scroll_view.post {
            scroll_view.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }
}

