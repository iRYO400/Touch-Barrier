package wei.mark.standouttest.ui.common

import android.view.View
import android.widget.AdapterView

abstract class SpinnerItemSelectedImpl : AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    public abstract fun onItemSelected(position: Int)

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onItemSelected(position)
    }

}