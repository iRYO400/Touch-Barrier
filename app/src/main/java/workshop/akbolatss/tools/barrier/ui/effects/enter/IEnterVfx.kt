package workshop.akbolatss.tools.barrier.ui.effects.enter

interface IEnterVfx {

    val id: Int

    val nameRes: Int

    val descriptionRes: Int

    fun getAction(): IEnterVfxAction
}
