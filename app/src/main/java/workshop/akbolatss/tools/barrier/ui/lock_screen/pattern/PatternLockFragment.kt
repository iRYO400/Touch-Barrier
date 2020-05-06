package workshop.akbolatss.tools.barrier.ui.lock_screen.pattern

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.fragment_setup_pattern.*
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.ui.common.PatterLockViewImpl
import workshop.akbolatss.tools.barrier.ui.lock_screen.ScreenLockListActivity
import workshop.akbolatss.tools.barrier.ui.lock_screen.ScreenLockType
import workshop.akbolatss.tools.barrier.utils.HawkKeys
import workshop.akbolatss.tools.barrier.utils.showSnackbarDelayAction
import workshop.akbolatss.tools.barrier.utils.showSnackbarError

class PatternLockFragment : Fragment() {
    companion object {
        fun newInstance() = PatternLockFragment()
    }

    private lateinit var callback: PatternLockCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = (context as ScreenLockListActivity)
    }

    private lateinit var patternListener: PatternLockViewListener
    private var patternLock: ArrayList<PatternLockView.Dot> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setup_pattern, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        patternListener = object : PatterLockViewImpl() {
            override fun onComplete(pattern: MutableList<PatternLockView.Dot>) {
                if (!pattern.isEmpty())
                    trySavePattern(pattern)
            }
        }
        pattern_lock_view.addPatternLockListener(patternListener)
    }

    private fun trySavePattern(pattern: MutableList<PatternLockView.Dot>) {
        if (patternLock.isEmpty()) {
            patternLock.addAll(pattern)
            status.text = getString(R.string.pattern_lock_submit)
        } else {
            var hasDifference = false
            if (patternLock.size != pattern.size)
                hasDifference = true

            for (i in 0 until patternLock.size) {
                if (hasDifference)
                    break
                if (patternLock[i].id != pattern[i].id)
                    hasDifference = true
            }

            if (hasDifference) {
                showSnackbarError(coordinator, getString(R.string.error_set_pattern_check))
                pattern_lock_view.setViewMode(PatternLockView.PatternViewMode.WRONG)
                return
            }

            Hawk.put(HawkKeys.PATTERN_DOTS, patternLock)
            pattern_lock_view.setViewMode(PatternLockView.PatternViewMode.CORRECT)
            showSnackbarDelayAction(coordinator,
                    getString(R.string.set_pattern_success),
                    getString(R.string.close)) {
                callback.onBack(ScreenLockType.PATTERN)
            }
        }
    }
}
