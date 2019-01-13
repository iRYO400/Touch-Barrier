package wei.mark.standouttest.ui.common

import wei.mark.standouttest.ui.intro.adapter.ActionType

interface SnackbarCallback {
    fun showSnackbar(actionType: ActionType, string: String)
}