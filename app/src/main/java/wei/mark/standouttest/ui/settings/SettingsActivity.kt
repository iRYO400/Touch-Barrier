package wei.mark.standouttest.ui.settings

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_settings.*
import wei.mark.standouttest.R
import wei.mark.standouttest.ui.intro.IntroFragmentCallback
import wei.mark.standouttest.utils.HawkKeys.Companion.IS_FIRST_START

class SettingsActivity : AppCompatActivity(), IntroFragmentCallback {

    private var currentTabTag: MutableLiveData<String> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (Hawk.get(IS_FIRST_START, true))
            navigateToTab(NavigationTab.TUTORIAL)
        else
            navigateToTab(NavigationTab.SETTINGS)


//        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
//            /** if not construct intent to request permission  */
//            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:$packageName"))
//            /** request permission via start activity for result  */
//            startActivityForResult(intent, REQUEST_CODE)
//            return
//        }
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

    override fun onCloseIntro() {
        navigateToTab(NavigationTab.SETTINGS)
    }
}

