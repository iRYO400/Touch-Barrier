package workshop.akbolatss.tools.barrier.utils

import android.os.Handler
import android.view.View
import com.google.android.material.snackbar.Snackbar


fun showSnackbarDelayAction(view: View,
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

fun showSnackbarAction(view: View,
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