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

package com.jaychang.navdemo

import com.uber.rib.core.Interactor
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.Router
import com.uber.rib.core.screenstack.ViewProvider
import com.uber.rib.core.screenstack.lifecycle.ScreenStackEvent.*
import io.reactivex.disposables.Disposable

abstract class RouterEx<I: Interactor<*, *>, C: InteractorBaseComponent<I>>(
    interactor: I,
    component: C
) : Router<I, C>(interactor, component) {
    fun observeScreenEvent(screen: ViewProvider, router: Router<*, *>) : Disposable {
        return screen.lifecycle().subscribe {
            when(it) {
                APPEARED -> attachChild(router)
                HIDDEN, REMOVED -> detachChild(router)
            }
        }
    }
}