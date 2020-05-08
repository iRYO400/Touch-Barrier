package workshop.akbolatss.tools.barrier.ui.effects.enter.transition

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.ui.effects.enter.EnterVfxApi

class ColorTransitionEnterVfx(
    context: Context,
    private val targetView: View
) : EnterVfxApi {

    private val argbAnimator = ValueAnimator.ofArgb(
        ContextCompat.getColor(context, R.color.transparent),
        ContextCompat.getColor(context, R.color.black)
    ).apply {
        interpolator = AccelerateDecelerateInterpolator()
        duration = 1500
    }

    init {
        argbAnimator.addUpdateListener {
            val color = it?.animatedValue as? Int
            color?.let {
                targetView.setBackgroundColor(color)
            }
        }
    }

    override fun apply(onStart: () -> Unit, onEnd: () -> Unit) {
        argbAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationCancel(animation: Animator?) = Unit
            override fun onAnimationRepeat(animation: Animator?) = Unit

            override fun onAnimationEnd(animation: Animator?) {
                onEnd.invoke()
            }

            override fun onAnimationStart(animation: Animator?) {
                onStart.invoke()
            }
        })
        argbAnimator.start()
    }

}
