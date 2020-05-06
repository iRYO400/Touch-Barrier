package workshop.akbolatss.tools.barrier.ui.lock_screen.setup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_setup_lock.*
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.ui.lock_screen.ScreenLockListActivity

class SetupLockFragment : Fragment() {

    companion object {
        fun newInstance() = SetupLockFragment()
    }


    private lateinit var callback: SetupFragmentCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = (context as ScreenLockListActivity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setup_lock, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        lock_none.setOnClickListener {
            callback.onSelectedNone()
        }

        lock_pin.setOnClickListener {
            callback.onSelectedPin()
        }

        lock_pattern.setOnClickListener {
            callback.onSelectedPattern()
        }

        lock_password.setOnClickListener {
            callback.onSelectedPassword()
        }

        lock_fingerprint.setOnClickListener {
            callback.onSelectedFingerprint()
        }
    }
}
