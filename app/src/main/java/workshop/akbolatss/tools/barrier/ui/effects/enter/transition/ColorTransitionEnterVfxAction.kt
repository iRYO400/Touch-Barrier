package workshop.akbolatss.tools.barrier.ui.effects.enter.transition

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import workshop.akbolatss.tools.barrier.R
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfxAction

class ColorTransitionEnterVfxAction : IEnterVfxAction {

    private lateinit var targetView: View

    private val context by lazy {
        targetView.context
    }

    private val argbAnimator = ValueAnimator.ofArgb(
        ContextCompat.getColor(context, R.color.transparent),
        ContextCompat.getColor(context, R.color.colorBlack)
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

    override fun prepare(targetView: View) {
        this.targetView = targetView
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
