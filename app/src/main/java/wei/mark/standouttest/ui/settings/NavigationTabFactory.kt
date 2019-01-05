package wei.mark.standouttest.ui.settings

import android.support.v4.app.Fragment

interface NavigationTabFactory {

    fun newInstance(): Fragment
}