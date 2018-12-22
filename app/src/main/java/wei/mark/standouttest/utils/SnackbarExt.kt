package wei.mark.standouttest.utils

import android.os.Handler
import android.support.design.widget.Snackbar
import android.view.View


fun showSnackbarTimer(view: View,
                      message: String,
                      actionTitle: String?,
                      listener: () -> Unit) {
    val snackbar = Snackbar.make(view,
            message,
            Snackbar.LENGTH_LONG
    )
    if (actionTitle != null) {
        snackbar.setAction(actionTitle) {
            listener()
        }
        val handler = Handler()
        handler.postDelayed({
            listener()
        }, 2500)
    }

    snackbar.show()
}

fun showSnackbarError(view: View, message: String) {
    val snackbar = Snackbar.make(view,
            message,
            Snackbar.LENGTH_LONG
    )
    snackbar.show()
}