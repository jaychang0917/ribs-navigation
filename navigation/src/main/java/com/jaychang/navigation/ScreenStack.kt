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

package com.jaychang.navigation

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.doOnLayout
import com.jaychang.navigation.ScreenStack.Direction.BACKWARD
import com.jaychang.navigation.ScreenStack.Direction.FORWARD
import com.jaychang.navigation.ScreenStack.Transaction
import com.jaychang.navigation.ScreenStack.Transaction.Type.PRESENT
import com.jaychang.navigation.ScreenStack.Transaction.Type.PUSH
import com.uber.rib.core.screenstack.ViewProvider
import java.util.*
import kotlin.reflect.KClass

/**
 * An screen stack with support for custom transition.
 * */
class ScreenStack(
    private val parentViewGroup: ViewGroup,
    private val defaultPushTransitionProvider: () -> Transition = { NoTransition() },
    private val defaultPresentTransitionProvider: () -> Transition = { NoTransition() }
) : Iterable<Transaction> {
    private var isDebugModeEnabled = false

    private val backStack: Deque<Transaction> = ArrayDeque()

    override fun iterator(): Iterator<Transaction> = backStack.iterator()

    /**
     * Pushes the [screen] onto the stack. The screen is added to [parentViewGroup] and the current
     * screen will be removed.
     *
     * @param screen The screen to be added to the container.
     * @param shouldAnimate True if should animate using default transition, false otherwise.
     * */
    fun pushScreen(screen: ViewProvider, shouldAnimate: Boolean) {
        pushScreen(screen, defaultOrNoTransition(shouldAnimate, PUSH))
    }

    /**
     * Pushes the [screen] onto the stack. The screen is added to [parentViewGroup] and the current
     * screen is removed.
     *
     * @param screen The screen to be added to the container.
     * @param transition The transition to be used, default one will be used if it is not provided.
     * */
    fun pushScreen(screen: ViewProvider, transition: Transition = defaultPushTransitionProvider()) {
        pushScreenInternal(transition) {
            onCurrentViewHidden()
            backStack.push(Transaction(screen, transition, PUSH))
            onCurrentViewAppeared()
        }
    }

    private fun pushScreenInternal(transition: Transition, backStackOp: () -> Unit) {
        val from = currentScreen()
        backStackOp()
        val to = addCurrentScreen(FORWARD)
        blockUnderneathViewClickEvent(to)
        animate(from, to, FORWARD, transition, true)
    }

    /**
     * Pops the current screen from the stack. The screen is removed from [parentViewGroup] and the
     * previous screen will be added.
     *
     * @param transition The transition to be used, default one will be used if it is not provided.
     * */
    fun popScreen(transition: Transition = defaultPushTransitionProvider()) {
        popScreenInternal(transition) {
            onCurrentViewRemoved()
            backStack.pop()
            onCurrentViewAppeared()
        }
    }

    /**
     * Pops the current screen from the stack. The screen is removed from [parentViewGroup] and the
     * previous screen will be added.
     *
     * @param shouldAnimate True if should animate using default transition, false otherwise.
     * */
    fun popScreen(shouldAnimate: Boolean) {
        popScreen(defaultOrNoTransition(shouldAnimate, PUSH))
    }

    /**
     * Pops screens until the specified screen is at the top of the stack. Those screens are removed
     * from [parentViewGroup] and the previous screen will be added.
     *
     * @param screen Target screen to be popped until.
     * @param inclusive Pops the screen inclusively if true.
     * @param transition The transition to be used, default one will be used if it is not provided.
     * */
    fun popToScreen(
        screen: KClass<out ViewProvider>,
        inclusive: Boolean = false,
        transition: Transition = defaultPushTransitionProvider()
    ) {
        val index = findScreenIndex(screen, inclusive)
        popToIndex(index, transition)
    }

    private fun popToIndex(index: Int, transition: Transition) {
        if (index == -1) return
        popScreenInternal(transition) {
            if (index !in 0..size()) error("invalid index: $index")
            while (size() - 1 > index) {
                onCurrentViewRemoved()
                backStack.pop()
            }
            onCurrentViewAppeared()
        }
    }

    private fun popScreenInternal(transition: Transition, backStackOp: () -> Unit) {
        val from = currentScreen()
        backStackOp()
        val to = addCurrentScreen(BACKWARD)
        animate(from, to, BACKWARD, transition, true)
    }

    /**
     * Pushes the [screen] onto the stack. The screen is added to [parentViewGroup] and the current
     * screen will NOT be removed.
     *
     * @param screen The screen to be added to the container.
     * @param shouldAnimate True if should animate using default transition, false otherwise.
     * */
    fun presentScreen(screen: ViewProvider, shouldAnimate: Boolean) {
        presentScreen(screen, defaultOrNoTransition(shouldAnimate, PRESENT))
    }

    /**
     * Pushes the [screen] onto the stack. The screen is added to [parentViewGroup] and the current
     * screen will NOT be removed.
     *
     * @param screen The screen to be added to the container.
     * @param transition The transition to be used, default one will be used if it is not provided.
     * */
    fun presentScreen(
        screen: ViewProvider,
        transition: Transition = defaultPresentTransitionProvider()
    ) {
        presentScreenInternal(transition) {
            onCurrentViewHidden()
            backStack.push(Transaction(screen, transition, PRESENT))
            onCurrentViewAppeared()
        }
    }

    private fun presentScreenInternal(transition: Transition, backStackOp: () -> Unit) {
        val from = currentScreen()
        backStackOp()
        val to = addCurrentScreen(FORWARD)
        blockUnderneathViewClickEvent(to)
        animate(from, to, FORWARD, transition, false)
    }

    private fun blockUnderneathViewClickEvent(view: View?) {
        view?.setOnClickListener { Unit }
    }

    /**
     * Pops the current screen from the stack. The screen is removed from [parentViewGroup].
     *
     * @param shouldAnimate True if should animate using default transition, false otherwise.
     * */
    fun dimissScreen(shouldAnimate: Boolean) {
        dismissScreen(defaultOrNoTransition(shouldAnimate, PRESENT))
    }

    /**
     * Pops the current screen from the stack. The screen is removed from [parentViewGroup].
     *
     * @param transition The transition to be used, default one will be used if it is not provided.
     * */
    fun dismissScreen(transition: Transition = defaultPresentTransitionProvider()) {
        dismissScreenInternal(transition) {
            onCurrentViewRemoved()
            backStack.pop()
            onCurrentViewAppeared()
        }
    }

    /**
     * Pops screens until the specified screen is at the top of the stack. Those screens are removed
     * from [parentViewGroup].
     *
     * @param screen Target screen to be popped until.
     * @param inclusive Pops the screen inclusively if true.
     * @param transition The transition to be used, default one will be used if it is not provided.
     * */
    fun dismissToScreen(
        screen: KClass<out ViewProvider>,
        inclusive: Boolean = false,
        transition: Transition = defaultPresentTransitionProvider()
    ) {
        val index = findScreenIndex(screen, inclusive)
        dismissToIndex(index, transition)
    }

    private fun dismissToIndex(index: Int, transition: Transition) {
        if (index == -1) return
        dismissScreenInternal(transition) {
            if (index !in 0..size()) error("invalid index: $index")
            while (size() - 1 > index) {
                onCurrentViewRemoved()
                backStack.pop()
            }
            onCurrentViewAppeared()
        }
    }

    private fun dismissScreenInternal(transition: Transition, backStackOp: () -> Unit) {
        val from = currentScreen()
        backStackOp()
        val to = parentViewGroup.getChildAt(parentViewGroup.childCount - 2)
        animate(from, to, BACKWARD, transition, true)
    }

    /**
     * Remove a specified screen from the screen stack.
     *
     * @param screen Target screen to be removed from the stack.
     * */
    fun removeScreen(screen: KClass<out ViewProvider>): Boolean {
        val each = backStack.iterator()
        var removed = false
        var index = 0
        while (each.hasNext()) {
            if (each.next().screen::class == screen) {
                each.remove()
                parentViewGroup.removeViewAt(index)
                removed = true
                break
            }
            index++
        }
        logDebugState()
        return removed
    }

    private fun findScreenIndex(screen: KClass<out ViewProvider>, inclusive: Boolean): Int {
        val indexOffset = if (inclusive) 1 else 0
        return reversed().indexOfLast { it.screen::class == screen } - indexOffset
    }

    private fun addCurrentScreen(direction: Direction): View? {
        val screen = currentViewProvider() ?: return null
        val view = screen.buildView(parentViewGroup)
        val index =
            if (direction == FORWARD) parentViewGroup.childCount else parentViewGroup.childCount - 1
        parentViewGroup.addView(view, index)
        return view
    }

    private fun currentScreen(): View? = parentViewGroup.children.lastOrNull()

    private fun defaultOrNoTransition(shouldAnimate: Boolean, transactionType: Transaction.Type) =
        if (shouldAnimate) {
            if (transactionType == PUSH) {
                defaultPushTransitionProvider()
            } else {
                defaultPresentTransitionProvider()
            }
        } else {
            NoTransition()
        }

    private fun animate(
        from: View?,
        to: View?,
        direction: Direction,
        transition: Transition,
        shouldRemoveCurrentScreen: Boolean
    ) {
        if (from == null) return

        if (to == null) {
            if (direction == BACKWARD) {
                parentViewGroup.removeView(from)
            }
            return
        }

        to.doOnLayout {
            transition.animate(from, to, direction) {
                if (shouldRemoveCurrentScreen) {
                    parentViewGroup.removeView(from)
                }
                logDebugState()
            }
        }
    }

    private fun onCurrentViewAppeared() {
        currentViewProvider()?.onViewAppeared()
    }

    private fun onCurrentViewRemoved() {
        currentViewProvider()?.onViewRemoved()
    }

    private fun onCurrentViewHidden() {
        currentViewProvider()?.onViewHidden()
    }

    private fun currentViewProvider() = backStack.peek()?.screen

    fun isRoot() = size() <= 1

    /**
     * Total number of transactions in the stack.
     * */
    fun size(): Int = backStack.size

    /**
     * Pop or dismiss the current screen.
     * */
    fun handleBackPress(): Boolean {
        if (isRoot()) return false

        val transaction = backStack.peek()
        val transition = transaction.transition
        val type = transaction.type
        if (type == PUSH) popScreen(transition) else dismissScreen(transition)
        return true
    }

    data class Transaction(
        val screen: ViewProvider,
        val transition: Transition,
        val type: Type
    ) {
        enum class Type { PUSH, PRESENT }
    }

    enum class Direction(val value: Int) {
        FORWARD(1), BACKWARD(-1)
    }

    fun enableDebugMode(enable: Boolean) {
        isDebugModeEnabled = enable
    }

    private fun logDebugState() {
        if (!isDebugModeEnabled) return
        Log.d(TAG, "===========================")
        Log.d(TAG, "Stack:")
        backStack.forEachIndexed { index, item ->
            Log.d(TAG, "${item.javaClass.simpleName}[$index]")
        }
        Log.d(TAG, "Views:")
        parentViewGroup.children.forEachIndexed { index, item ->
            Log.d(TAG, "${item.javaClass.simpleName}[$index]")
        }
        Log.d(TAG, "===========================")
    }

    private companion object {
        private val TAG = ScreenStack::class.java.simpleName
    }
}
