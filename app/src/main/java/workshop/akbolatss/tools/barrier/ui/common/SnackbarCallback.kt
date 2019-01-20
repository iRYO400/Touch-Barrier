package workshop.akbolatss.tools.barrier.ui.common

import workshop.akbolatss.tools.barrier.ui.intro.adapter.ActionType

interface SnackbarCallback {
    fun showSnackbar(actionType: ActionType, string: String)
}