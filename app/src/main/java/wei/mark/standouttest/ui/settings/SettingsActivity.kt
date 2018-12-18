package wei.mark.standouttest.ui.settings

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_settings.*
import wei.mark.standout.StandOutWindow
import wei.mark.standouttest.FullScreenWindow
import wei.mark.standouttest.R
import wei.mark.standouttest.utils.HawkKeys.Companion.CLOSE_ON_ACTIVE
import wei.mark.standouttest.utils.WindowKeys.Companion.MAIN_WINDOW_ID

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            /** if not construct intent to request permission  */
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName"))
            /** request permission via start activity for result  */
            startActivityForResult(intent, REQUEST_CODE)
            return
        }

        setListeners()
        setObservers()
    }

    private fun setListeners() {
        switch_barrier.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            Log.d("DEBUG_TAG", "switchBarrier $isChecked")
            if (isChecked)
                showVisibleBarrier()
            else
                closeBarrier()
        }

        switch_notify_bar.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            Log.d("DEBUG_TAG", "switchNotifyBar $isChecked")
            if (isChecked)
                showInvisibleBarrier()
            else
                closeBarrier()
        }

        switch_quick_settings.setOnCheckedChangeListener { buttonView, isChecked ->

        }

        switch_close_on.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!Hawk.contains(CLOSE_ON_ACTIVE)) {
                Hawk.put(CLOSE_ON_ACTIVE, true)
                return@setOnCheckedChangeListener
            }

            if (isChecked)
                Hawk.put(CLOSE_ON_ACTIVE, true)
            else
                Hawk.put(CLOSE_ON_ACTIVE, false)
        }

        button2.setOnClickListener {
            switch_barrier.isChecked = !switch_barrier.isChecked
        }
    }

    private fun setObservers() {
        FullScreenWindow.isShown.observe(this, Observer {
            var boolRef = it
            if (boolRef == null)
                boolRef = false

            switch_barrier.isChecked = boolRef
            switch_notify_bar.isChecked = boolRef
        })

        FullScreenWindow.isHidden.observe(this, Observer {
            var bool = it
            if (bool == null)
                bool = false

            switch_barrier.isChecked = !bool
        })
    }

    private fun showVisibleBarrier() {
        StandOutWindow.show(this, FullScreenWindow::class.java, MAIN_WINDOW_ID)

        if (Hawk.contains(CLOSE_ON_ACTIVE))
            if (Hawk.get(CLOSE_ON_ACTIVE))
                finish()
    }

    private fun showInvisibleBarrier() {
        if (isWindowServiceActive())
            StandOutWindow.showInInvisible(this, FullScreenWindow::class.java, MAIN_WINDOW_ID)
    }

    private fun closeBarrier() {
        if (isWindowServiceActive())
            StandOutWindow.close(this, FullScreenWindow::class.java, MAIN_WINDOW_ID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK)
            // if so check once again if we have permission */
                if (Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(this)) {
                    setListeners()
                    setObservers()
                } else
                    finish()
            else {
                finish()
            }
        }
    }

    private fun isWindowServiceActive(): Boolean {
        return (FullScreenWindow.isShown != null
                && FullScreenWindow.isHidden != null)
    }

    companion object {
        private const val REQUEST_CODE = 101
    }
}
