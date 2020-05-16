package workshop.akbolatss.tools.barrier.ui.effects.enter.fade

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import workshop.akbolatss.tools.barrier.ui.effects.enter.IEnterVfxAction

class FadeEnterVfxAction : IEnterVfxAction {

    private lateinit var viewOnAnimate: View

    private val alphaAnimation = AlphaAnimation(1f, 0f).apply {
        duration = 1500
    }

    override fun prepare(targetView: View) {
        viewOnAnimate = targetView
    }

    override fun apply(onStart: () -> Unit, onEnd: () -> Unit) {
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) = Unit
            override fun onAnimationEnd(animation: Animation?) {
                onEnd.invoke()
            }

            override fun onAnimationStart(animation: Animation?) {
                onStart.invoke()
            }
        })
        viewOnAnimate.startAnimation(alphaAnimation)
    }

}
