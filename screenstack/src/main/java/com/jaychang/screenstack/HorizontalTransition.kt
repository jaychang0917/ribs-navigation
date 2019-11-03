/*
 * Copyright (C) 2019. Jay Chang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.jaychang.screenstack

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import com.jaychang.screenstack.ScreenStack.Direction

class HorizontalTransition : Transition {
    override fun animate(from: View, to: View, direction: Direction, onAnimationEnd: () -> Unit) {
        val animator = createAnimator(from, to, direction)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onAnimationEnd()
            }
        })
        animator.start()
    }

    private fun createAnimator(from: View, to: View, direction: Direction): AnimatorSet {
        val axis = View.TRANSLATION_X
        val value = direction.value
        val fromTranslation = (value * -from.width).toFloat()
        val toTransition = (value * to.width).toFloat()
        val set = AnimatorSet()
        set.play(ObjectAnimator.ofFloat(from, axis, 0f, fromTranslation))
        set.play(ObjectAnimator.ofFloat(to, axis, toTransition, 0f))
        return set
    }
}