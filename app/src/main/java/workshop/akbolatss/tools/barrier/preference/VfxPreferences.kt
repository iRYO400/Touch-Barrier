package workshop.akbolatss.tools.barrier.preference

import android.content.Context
import android.content.SharedPreferences
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfx
import workshop.akbolatss.tools.barrier.ui.effects.enter.default.DefaultEnterVfx
import workshop.akbolatss.tools.barrier.ui.effects.enter.fade.FadeEnterVfx
import workshop.akbolatss.tools.barrier.ui.effects.enter.transition.ColorTransitionEnterVfx
import workshop.akbolatss.tools.barrier.ui.effects.enter.tv_off.TvOffEnterVfx

class VfxPreferences(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        const val SHARED_SELECTED_ENTER_VFX = "_selectedEnterVfx"
    }

    fun getEnterVfx(): IEnterVfx {
        val storedId = sharedPreferences.getInt(SHARED_SELECTED_ENTER_VFX, -1)
        getAll().forEach { enterVfx ->
            if (storedId == enterVfx.id)
                return enterVfx
        }
        return DefaultEnterVfx()
    }

    fun setEnterVfx(enterVfx: IEnterVfx) {
        sharedPreferences.edit().apply {
            putInt(SHARED_SELECTED_ENTER_VFX, enterVfx.id)
            apply()
        }
    }

    fun getAll(): List<IEnterVfx> {
        return listOf(
            FadeEnterVfx(),
            ColorTransitionEnterVfx(),
            TvOffEnterVfx()
        )
    }
}
