package workshop.akbolatss.tools.barrier.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import workshop.akbolatss.tools.barrier.R

class NavigationActivity : FragmentActivity() {

    private val navigator by inject<Navigator> {
        parametersOf(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        navigator.toSettings()
    }
}

