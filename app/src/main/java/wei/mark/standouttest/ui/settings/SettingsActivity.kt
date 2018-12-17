package wei.mark.standouttest.ui.settings

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
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

            if (isChecked)
                startBarrier()
            else
                stopBarrier()
        }

        switch_notify_bar.setOnCheckedChangeListener { buttonView, isChecked ->

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
    }

    private fun setObservers() {
        FullScreenWindow.isShown.observe(this, Observer {
            var boolRef = it
            if (boolRef == null)
                boolRef = false

            switch_barrier.isChecked = boolRef
        })
    }

    private fun startBarrier() {


        StandOutWindow.show(this, FullScreenWindow::class.java, MAIN_WINDOW_ID)

        if (Hawk.contains(CLOSE_ON_ACTIVE))
            if (Hawk.get(CLOSE_ON_ACTIVE))
                finish()
    }

    private fun stopBarrier() {
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

    companion object {
        private const val REQUEST_CODE = 101
    }
}
