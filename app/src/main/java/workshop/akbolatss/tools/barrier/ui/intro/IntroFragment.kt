package workshop.akbolatss.tools.barrier.ui.intro

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.PagerSnapHelper
import kotlinx.android.synthetic.main.intro_fragment.*
import workshop.akbolatss.tools.barrier.BarrierApplication
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.accessibility.AccessibilityServiceHelper.isAccessibilityServiceEnabled
import workshop.akbolatss.tools.barrier.accessibility.BarrierAccessibilityService
import workshop.akbolatss.tools.barrier.ui.intro.adapter.ActionType
import workshop.akbolatss.tools.barrier.ui.intro.adapter.IntroAction
import workshop.akbolatss.tools.barrier.ui.intro.adapter.IntroAdapter
import workshop.akbolatss.tools.barrier.ui.SettingsActivity

class IntroFragment : Fragment() {

    companion object {
        fun newInstance() = IntroFragment()
    }

    private lateinit var viewModel: IntroViewModel

    private lateinit var adapter: IntroAdapter

    private lateinit var callback: IntroFragmentCallback

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = (context as SettingsActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.intro_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(IntroViewModel::class.java)

        initAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ready.setOnClickListener {
            hideIntroFragment()
        }
    }

    private fun hideIntroFragment() {
        callback.onCloseIntro()
    }

    private fun initAdapter() {
        adapter = IntroAdapter(getTutorials()) { introAction, position ->
            when (introAction.actionType) {
                ActionType.OPEN_DRAW_OVER_SETTINGS -> {
                    openDrawOverSettings()
                }
                ActionType.OPEN_ACCESSIBILITY_SETTINGS -> {
                    openAccessibilitySettings()
                }
            }
        }
        recycler_view.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(recycler_view)
    }

    private fun openDrawOverSettings() {
        if (Build.VERSION.SDK_INT >= 23 && !BarrierApplication.instance.canDrawOverApps()) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${activity!!.packageName}"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        } else {
            callback.showSnackbar(ActionType.OPEN_DRAW_OVER_SETTINGS, getString(R.string.permission_already_granted))
        }
    }

    private fun openAccessibilitySettings() {
        if (!isAccessibilityServiceEnabled(activity, BarrierAccessibilityService::class.java)) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        } else {
            callback.showSnackbar(ActionType.OPEN_ACCESSIBILITY_SETTINGS, getString(R.string.permission_already_granted))
        }
    }

    private fun checkPermissions() {
        var allPermissionAccepted = true

//        if (Build.VERSION.SDK_INT >= 23 && !BarrierApplication.instance.canDrawOverApps()) {
//            allPermissionAccepted = false
//        }

        if (!isAccessibilityServiceEnabled(activity, BarrierAccessibilityService::class.java)) {
            allPermissionAccepted = false
        }
        ready.isEnabled = allPermissionAccepted
    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    private fun getTutorials(): List<IntroAction> {
        val tutorials = ArrayList<IntroAction>()
//        tutorials.add(IntroAction(R.string.tutorial_1, R.string.tutorial_1_desc,
//                R.drawable.tutorial1, ActionType.OPEN_DRAW_OVER_SETTINGS))
        tutorials.add(IntroAction(R.string.tutorial_2, R.string.tutorial_2_desc,
                R.drawable.tutorial2, ActionType.OPEN_ACCESSIBILITY_SETTINGS))
        return tutorials
    }
}
