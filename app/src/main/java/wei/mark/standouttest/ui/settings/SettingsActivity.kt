package wei.mark.standouttest.ui.settings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_settings.*
import wei.mark.standout.StandOutWindow

import wei.mark.standouttest.R
import wei.mark.standouttest.SimpleWindow
import wei.mark.standouttest.utils.HawkKeys.Companion.CLOSE_ON_ACTIVE
import wei.mark.standouttest.utils.WindowKeys.Companion.MAIN_WINDOW_ID

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setListeners()
    }

    private fun setListeners() {
        switch_barrier.setOnCheckedChangeListener { buttonView, isChecked ->
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

    private fun startBarrier() {
        StandOutWindow.show(this, SimpleWindow::class.java, MAIN_WINDOW_ID)
        if (Hawk.contains(CLOSE_ON_ACTIVE))
            if (Hawk.get(CLOSE_ON_ACTIVE))
                finish()
    }

    private fun stopBarrier() {
        StandOutWindow.close(this, SimpleWindow::class.java, MAIN_WINDOW_ID)
    }
}
