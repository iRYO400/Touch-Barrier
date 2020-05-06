package workshop.akbolatss.tools.barrier.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.AnimationUtils
import com.orhanobut.hawk.Hawk
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel
import workshop.akbolatss.tools.barrier.BarrierApplication
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.accessibility.AccessibilityServiceHelper.isAccessibilityServiceEnabled
import workshop.akbolatss.tools.barrier.accessibility.BarrierAccessibilityService
import workshop.akbolatss.tools.barrier.base.BaseFragment
import workshop.akbolatss.tools.barrier.databinding.FragmentMainBinding
import workshop.akbolatss.tools.barrier.ui.intro.adapter.ActionType
import workshop.akbolatss.tools.barrier.ui.lock_screen.ScreenLockListActivity
import workshop.akbolatss.tools.barrier.ui.lock_screen.ScreenLockType
import workshop.akbolatss.tools.barrier.utils.HawkKeys
import workshop.akbolatss.tools.barrier.utils.IntentKeys
import workshop.akbolatss.tools.barrier.utils.NotificationExt.createDefaultNotificationChannel
import workshop.akbolatss.tools.barrier.utils.livedata.EventObserver
import workshop.akbolatss.tools.barrier.utils.showSnackbarError

class SettingsFragment(
    override val layoutId: Int = R.layout.fragment_main
) : BaseFragment<FragmentMainBinding>() {

    companion object {
        fun newInstance() = SettingsFragment()

        private const val REQUEST_SET_LOCK = 102
    }

    private val viewModel by lifecycleScope.viewModel<SettingsViewModel>(this)

    private lateinit var callback: SettingsFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SettingsFragmentCallback)
            callback = context
        else
            throw IllegalStateException("Caller must implement ${SettingsFragmentCallback::class.java.simpleName}")
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        setDefault()
    }

    /**
     * Init default values
     */
    private fun setDefault() {
        if (Hawk.contains(HawkKeys.LOCK_TYPE_INDEX)) {
            val type = Hawk.get(HawkKeys.LOCK_TYPE_INDEX, ScreenLockType.NONE)
            binding.selectedLock.text = getString(type.getName())
        } else {
            Hawk.put(HawkKeys.LOCK_TYPE_INDEX, ScreenLockType.NONE)
        }

        if (Hawk.contains(HawkKeys.CLOSE_ON_ACTIVATION))
            binding.switchCloseOnActivation.isChecked = Hawk.get(HawkKeys.CLOSE_ON_ACTIVATION)
        else
            Hawk.put(HawkKeys.CLOSE_ON_ACTIVATION, false)

        if (!Hawk.contains(HawkKeys.NOTIFY_CHANNEL_CREATED)) {
            Hawk.put(HawkKeys.NOTIFY_CHANNEL_CREATED, true)
            createDefaultNotificationChannel(activity!!)
        }
    }

    override fun setObserversListeners() {
        observeViewModel()
        setListeners()
    }

    private fun observeViewModel() {
        viewModel.accessibleServiceDisabled.observe(viewLifecycleOwner, EventObserver {
            if (it)
                showPermissionSettings()
        })
    }

    private fun showPermissionSettings() {
        callback.scrollView()
        val animationShake = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_shake)
        binding.permInfo.startAnimation(animationShake)
    }

    private fun setListeners() {
        binding.switchBarrier.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            viewModel.toggleBarrier(isChecked)
        }

        binding.switchNotifyBar.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            viewModel.toggleNotificationPanel(isChecked)
        }

        binding.switchCloseOnActivation.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            Hawk.put(HawkKeys.CLOSE_ON_ACTIVATION, isChecked)
        }

        binding.switchPermDrawOver.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            if (!isChecked && BarrierApplication.instance.canDrawOverApps()) {
                callback.showSnackbar(
                    ActionType.OPEN_DRAW_OVER_SETTINGS,
                    getString(R.string.try_deny_permission)
                )
                binding.switchPermDrawOver.isChecked = !isChecked
            } else
                openDrawOverSettings()
        }

        binding.switchPermAccessibility.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            if (!isChecked && isAccessibilityServiceEnabled(
                    activity,
                    BarrierAccessibilityService::class.java
                )
            ) {
                callback.showSnackbar(
                    ActionType.OPEN_ACCESSIBILITY_SETTINGS,
                    getString(R.string.try_deny_permission)
                )
                binding.switchPermAccessibility.isChecked = !isChecked
            } else
                openAccessibilitySettings()
        }

        binding.screenLock.setOnClickListener {
            val intent = Intent(
                activity,
                ScreenLockListActivity::class.java
            ) //TODO: There is hidden old lock settings
            startActivityForResult(intent, REQUEST_SET_LOCK)
        }
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    @Deprecated("It's not needed")
    private fun openDrawOverSettings() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${activity!!.packageName}")
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23)
            binding.switchPermDrawOver.isChecked = BarrierApplication.instance.canDrawOverApps()
        else
            binding.switchPermDrawOver.visibility = View.GONE

        binding.switchPermAccessibility.isChecked =
            isAccessibilityServiceEnabled(activity, BarrierAccessibilityService::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SET_LOCK) {
            if (resultCode == Activity.RESULT_OK) {
                val screenLockType: ScreenLockType =
                    data!!.getSerializableExtra(IntentKeys.SCREEN_LOCK_TYPE) as ScreenLockType
                binding.selectedLock.text = getString(screenLockType.getName())
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showSnackbarError(binding.coordinator, getString(R.string.error_did_not_set_lock))
            }
        }
    }
}
