package com.loyaltiez.core.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

/*
A ViewAnimator class concerned with UI animation
Create an instance of this class, and call the associated methods, providing the views to animate
to create UI animations
 */
class ViewAnimator {

    // The constants used as default values for animation functions
    companion object Constants {

        private const val BREATHE_DEFAULT_INFLATION_DURATION = 1000L
        private const val BREATHE_DEFAULT_DEFLATION_DURATION = BREATHE_DEFAULT_INFLATION_DURATION
        private const val BREATHE_DEFAULT_LOW_SCALE_X = 1f
        private const val BREATHE_DEFAULT_HIGH_SCALE_X = 1.05f
        private const val BREATHE_DEFAULT_LOW_SCALE_Y = 1f
        private const val BREATHE_DEFAULT_HIGH_SCALE_Y = 1.05f
    }

    // Animate the passed in view with a breathing animation, scaling the view up then down
    fun breathe(
        view: View,
        inflationDuration: Long = BREATHE_DEFAULT_INFLATION_DURATION,
        deflationDuration: Long = BREATHE_DEFAULT_DEFLATION_DURATION,
        lowScaleX: Float = BREATHE_DEFAULT_LOW_SCALE_X,
        highScaleX: Float = BREATHE_DEFAULT_HIGH_SCALE_X,
        lowScaleY: Float = BREATHE_DEFAULT_LOW_SCALE_Y,
        highScaleY: Float = BREATHE_DEFAULT_HIGH_SCALE_Y
    ): AnimatorSet {

        val scaleXInflation = ObjectAnimator.ofFloat(view, "scaleX", highScaleX)
        val scaleYInflation = ObjectAnimator.ofFloat(view, "scaleY", highScaleY)

        val scaleXDeflation = ObjectAnimator.ofFloat(view, "scaleX", lowScaleX)
        val scaleYDeflation = ObjectAnimator.ofFloat(view, "scaleY", lowScaleY)

        val inflation = AnimatorSet().apply {
            duration = inflationDuration
            play(scaleXInflation).with(scaleYInflation)
        }

        val deflation = AnimatorSet().apply {
            duration = deflationDuration
            play(scaleXDeflation).with(scaleYDeflation)
        }

        // Apply the inflation animation followed by the deflation animation
        return AnimatorSet().apply {
            play(inflation).before(deflation)
        }
    }
}