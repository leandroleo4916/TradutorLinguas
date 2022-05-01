package com.example.tradutorlinguas.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.util.ArrayList

class AnimatorView {

    fun animationView(image: View) {
        val animatorSet = AnimatorSet()
        animatorSet.duration = 300
        val animatorList = ArrayList<Animator>()
        val scaleX = ObjectAnimator.ofFloat(image, "ScaleX", 1f, 1.2f, 1f)
        animatorList.add(scaleX)
        val scaleY = ObjectAnimator.ofFloat(image, "ScaleY", 1f, 1.2f, 1f)
        animatorList.add(scaleY)
        animatorSet.playTogether(animatorList)
        animatorSet.start()
    }

}