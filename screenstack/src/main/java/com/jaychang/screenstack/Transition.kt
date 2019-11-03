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

import android.view.View

interface Transition {
    /**
     * To swap Views from one screen to another.
     *
     * @param from The previous View in container.
     * @param to The next View that should put in the container.
     * @param onAnimationEnd Callback to remove view from parent ViewGroup.
     * */
    fun animate(from: View, to: View, direction: ScreenStack.Direction, onAnimationEnd: () -> Unit)
}