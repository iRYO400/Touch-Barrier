package workshop.akbolatss.tools.barrier.ui.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import com.orhanobut.hawk.Hawk
import org.koin.androidx.scope.lifecycleScope
import org.koin.androidx.viewmodel.scope.viewModel
import timber.log.Timber
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.accessibility.BarrierAccessibilityService
import workshop.akbolatss.tools.barrier.base.BaseFragment
import workshop.akbolatss.tools.barrier.databinding.FragmentMainBinding
import workshop.akbolatss.tools.barrier.ui.lock_screen.ScreenLockListActivity
import workshop.akbolatss.tools.barrier.ui.lock_screen.ScreenLockType
import workshop.akbolatss.tools.barrier.utils.HawkKeys
import workshop.akbolatss.tools.barrier.utils.IntentKeys
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

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        Timber.d("init SettingsFragment")
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
    }

    override fun setObserversListeners() {
        observeViewModel()
        setListeners()
    }

    private fun observeViewModel() {
        viewModel.toggleBarrierError.observe(viewLifecycleOwner, EventObserver {
            if (it)
                showPermissionSettings()
        })
        BarrierAccessibilityService.isBarrierEnabled.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.switchBarrier.isChecked = it
            }
        })
    }

    private fun showPermissionSettings() {
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

            viewModel.toggleCloseOnActivation(isChecked)
        }

        binding.switchPermAccessibility.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView!!.isPressed)
                return@setOnCheckedChangeListener

            viewModel.toggleAccessibilityService(isChecked)
        }

        binding.screenLock.setOnClickListener {
            val intent = Intent(
                activity,
                ScreenLockListActivity::class.java
            ) //TODO: There is hidden old lock settings
            startActivityForResult(intent, REQUEST_SET_LOCK)
        }

        binding.btnEnterVFX.setOnClickListener {
            openSelectEnterVfx()
        }
        binding.btnIdleVFX.setOnClickListener {
//            openSelectEnterVfx()
        }
    }

    private fun openSelectEnterVfx() {
        viewModel.openEnterVfxSelector()
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
