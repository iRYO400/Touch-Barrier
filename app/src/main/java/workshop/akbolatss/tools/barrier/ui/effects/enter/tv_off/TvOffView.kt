package workshop.akbolatss.tools.barrier.ui.effects.enter.tv_off

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import timber.log.Timber

class TvOffView : View {

    private val blackPaint = Paint().apply {
        color = Color.rgb(0, 0, 0)
    }

    private val topRect = Rect()
    private val bottomRect = Rect()
    private val leftRect = Rect()
    private val rightRect = Rect()

    private val topBottomAnimator = ValueAnimator.ofFloat().apply {
        duration = 300
    }

    private val leftRightAnimator = ValueAnimator.ofInt().apply {
        duration = 150
    }

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)
    constructor(ctx: Context, attrs: AttributeSet, defStyle: Int) : super(ctx, attrs, defStyle)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Timber.d("onSizeChanged w $w h $h oldw $oldw oldh $oldh")
        topBottomAnimator.setValues(
            PropertyValuesHolder.ofFloat(
                "",
                0f,
                (h / 2).toFloat()
            )
        )
        leftRightAnimator.setValues(PropertyValuesHolder.ofInt("", 0, w / 2))
        collapseTopAndBottom()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(topRect, blackPaint)

        canvas.drawRect(bottomRect, blackPaint)

        canvas.drawRect(leftRect, blackPaint)

        canvas.drawRect(rightRect, blackPaint)
    }

    private fun collapseTopAndBottom() {
        topBottomAnimator.apply {
            if (isRunning) cancel()
            removeAllUpdateListeners()

            addUpdateListener {
                drawTopBottomProgress(
                    animatedValue as Float
                )
            }
            doOnEnd {
                collapseLeftAndRight()
            }
            start()
        }
    }

    private fun drawTopBottomProgress(
        progress: Float
    ) {
        topRect.top = 0
        topRect.left = 0
        topRect.right = width
        topRect.bottom = progress.toInt() - 1

        bottomRect.left = 0
        bottomRect.right = width
        bottomRect.bottom = height
        bottomRect.top = height - progress.toInt() + 1
        invalidate()
    }

    private fun collapseLeftAndRight() {
        leftRightAnimator.apply {
            if (isRunning) cancel()
            removeAllUpdateListeners()

            addUpdateListener {
                drawLeftRightProgress(
                    animatedValue as Int
                )
            }
            start()
        }
    }

    private fun drawLeftRightProgress(progress: Int) {
        leftRect.left = 0
        leftRect.top = (height / 2) - 1
        leftRect.bottom = (height / 2) + 1
        leftRect.right = progress

        rightRect.right = width
        rightRect.top = (height / 2) - 1
        rightRect.bottom = (height / 2) + 1
        rightRect.left = width - progress + 1

        invalidate()
    }


}
