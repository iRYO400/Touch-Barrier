package workshop.akbolatss.tools.barrier.ui.intro.adapter

data class IntroAction(
        val titleResourceId: Int,
        val descriptionResourceId: Int,
        val imageResourceId: Int?,
        val actionType: ActionType
)

enum class ActionType {
    NON_ACTION,
    OPEN_DRAW_OVER_SETTINGS,
    OPEN_ACCESSIBILITY_SETTINGS
}