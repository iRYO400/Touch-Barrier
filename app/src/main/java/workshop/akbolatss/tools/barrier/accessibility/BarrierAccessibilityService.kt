package workshop.akbolatss.tools.barrier.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.koin.core.KoinComponent
import org.koin.core.inject
import workshop.akbolatss.tools.barrier.databinding.ViewBarrierHolderBinding
import workshop.akbolatss.tools.barrier.preference.AdditionalPreferences
import workshop.akbolatss.tools.barrier.utils.IntentKeys

class BarrierAccessibilityService :
    AccessibilityService(), KoinComponent {

    companion object {
        val isBarrierEnabled = MutableLiveData<Boolean>()
    }

    private var rootView: FrameLayout? = null

    private val additionalPreferences by inject<AdditionalPreferences>()

    private val shouldCloseOnActivation: Boolean
        get() = additionalPreferences.isCloseOnActivationEnabled()

    override fun onServiceConnected() {
        isBarrierEnabled.value = false
        initBroadcastReceiver()
    }

    private fun initBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            broadcastReceiver, IntentFilter(IntentKeys.INTENT_FILTER_ACCESSIBILITY)
        )
    }

    private val broadcastReceiver: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent
            ) {
                processIntent(intent)
            }
        }

    private fun processIntent(intent: Intent) {
        if (intent.hasExtra(IntentKeys.INTENT_TOGGLE_BARRIER)) {
            val enable = intent.getBooleanExtra(IntentKeys.INTENT_TOGGLE_BARRIER, false)
            if (enable)
                enableBarrier()
            else
                disableBarrier()
        }
    }

    private fun enableBarrier() {
        val layoutParams = createLayoutParams()
        val rootView = initRootView(layoutParams)
        val binding = inflateView(rootView)
        setListeners(binding)
        applyPreferenceSettings()
        isBarrierEnabled.value = true
    }

    private fun createLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams()
            .apply {
                type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
                flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
                format = PixelFormat.TRANSLUCENT
            }
    }

    private fun initRootView(layoutParams: WindowManager.LayoutParams): FrameLayout {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        rootView = FrameLayout(this)
        wm.addView(rootView, layoutParams)
        return rootView as FrameLayout
    }

    private fun inflateView(rootView: ViewGroup): ViewBarrierHolderBinding {
        val inflater = LayoutInflater.from(this)
        return ViewBarrierHolderBinding.inflate(inflater, rootView, true)
    }

    private fun setListeners(binding: ViewBarrierHolderBinding) {
        binding.imgLogo.setOnClickListener {
            disableBarrier()
        }
    }

    private fun applyPreferenceSettings() {
        if (shouldCloseOnActivation)
            goHomeActivity()
    }

    private fun goHomeActivity() {
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
        // close status bar
        sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }

    private fun disableBarrier() {
        removeRootView()
        isBarrierEnabled.value = false
    }

    private fun removeRootView() {
        rootView?.let {
            val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.removeView(rootView)
            rootView = null
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) = Unit
    override fun onInterrupt() = Unit

}
