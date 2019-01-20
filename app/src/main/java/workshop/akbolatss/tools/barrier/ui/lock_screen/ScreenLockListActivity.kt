package workshop.akbolatss.tools.barrier.ui.lock_screen

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_screen_lock_list.*
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.ui.lock_screen.pin.PinLockCallback
import workshop.akbolatss.tools.barrier.ui.lock_screen.setup.SetupFragmentCallback
import workshop.akbolatss.tools.barrier.utils.HawkKeys
import workshop.akbolatss.tools.barrier.utils.IntentKeys.Companion.SCREEN_LOCK_TYPE

class ScreenLockListActivity : AppCompatActivity(), SetupFragmentCallback, PinLockCallback {

    private var currentTabTag: MutableLiveData<String> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_lock_list)

        initToolbar()
        navigateToTab(ScreenLockTab.SETUP)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun navigateToTab(screenLockTab: ScreenLockTab) {
        hideCurrentTab()

        val currentTabTag = screenLockTab.fragmentTag
        val fragmentManager = supportFragmentManager
        var fragment = fragmentManager.findFragmentByTag(currentTabTag)
        val transaction = fragmentManager.beginTransaction()
        if (fragment == null) {
            fragment = screenLockTab.navigationTabFactory.newInstance()
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

    override fun onSelectedNone() {
        Hawk.put(HawkKeys.LOCK_TYPE_INDEX, ScreenLockType.NONE)
        onBackSuccess(ScreenLockType.NONE)
    }

    override fun onSelectedPin() {
        navigateToTab(ScreenLockTab.PIN)
    }

    override fun onSelectedPattern() {
        navigateToTab(ScreenLockTab.PATTERN)
    }

    override fun onSelectedPassword() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSelectedFingerprint() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBack(screenLockType: ScreenLockType) {
        when (screenLockType) {
            ScreenLockType.NONE -> {

            }
            ScreenLockType.PIN -> {

            }
            ScreenLockType.PATTERN -> {

            }
        }
        onBackSuccess(screenLockType)
    }

    override fun onBackPressed() {
        onBackError()
    }

    private fun onBackSuccess(screenLockType: ScreenLockType) {
        intent.putExtra(SCREEN_LOCK_TYPE, screenLockType)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun onBackError() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackError()
        }
        return super.onOptionsItemSelected(item)
    }
}