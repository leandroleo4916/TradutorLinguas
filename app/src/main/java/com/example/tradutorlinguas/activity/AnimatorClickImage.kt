package com.example.tradutorlinguas.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.ImageView
import java.util.ArrayList

class AnimatorClickImage {

    fun animationImage(image: ImageView) {
        val animatorSet = AnimatorSet()
        animatorSet.duration = 300
        val animatorList = ArrayList<Animator>()
        val scaleXAnimator = ObjectAnimator.ofFloat(
                image, "ScaleX", 1f, 1.2f, 1f)
        animatorList.add(scaleXAnimator)
        val scaleYAnimator = ObjectAnimator.ofFloat(
                image, "ScaleY", 1f, 1.2f, 1f)
        animatorList.add(scaleYAnimator)
        animatorSet.playTogether(animatorList)
        animatorSet.start()
    }
}