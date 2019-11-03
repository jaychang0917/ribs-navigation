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

package com.jaychang.navdemo.root.create_delivery_order

import com.jaychang.navdemo.RouterEx
import com.jaychang.navdemo.root.RootView
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.DeliveryOrderRefinementBuilder
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.DeliveryOrderRefinementRouter
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.delivery_package_info.DeliveryPackageInfoScreen
import com.jaychang.navdemo.root.create_delivery_order.delivery_select_time.DeliverySelectTimeBuilder
import com.jaychang.navdemo.root.create_delivery_order.delivery_select_time.DeliverySelectTimeInteractor
import com.jaychang.navdemo.root.create_delivery_order.delivery_select_time.DeliverySelectTimeScreen
import com.jaychang.screenstack.ScreenStack

class CreateDeliveryOrderRouter(
    interactor: CreateDeliveryOrderInteractor,
    component: CreateDeliveryOrderBuilder.Component,
    private val rootView: RootView,
    private val deliverySelectTimeListener: DeliverySelectTimeInteractor.Listener,
    private val deliverySelectTimeBuilder: DeliverySelectTimeBuilder,
    private val deliveryOrderRefinementBuilder: DeliveryOrderRefinementBuilder,
    private val screenStack: ScreenStack
) : RouterEx<CreateDeliveryOrderInteractor, CreateDeliveryOrderBuilder.Component>(interactor, component) {
    private var deliveryOrderRefinementRouter: DeliveryOrderRefinementRouter? = null

    fun attachDeliverySelectTime() {
        val router = deliverySelectTimeBuilder.build(rootView)
        val screen = DeliverySelectTimeScreen(router.view, deliverySelectTimeListener)
        observeScreenEvent(screen, router)
        screenStack.pushScreen(screen)
    }

    fun detachDeliverySelectTime() {
        screenStack.popScreen()
    }

    fun attachDeliveryOrderRefinement() {
        val router = deliveryOrderRefinementBuilder.build()
        attachChild(router)
        deliveryOrderRefinementRouter = router
    }

    fun detachDeliveryOrderRefinement() {
        val router = deliveryOrderRefinementRouter ?: return
        detachChild(router)
        screenStack.popToScreen(DeliveryPackageInfoScreen::class, true)
        deliveryOrderRefinementRouter = null
    }
}
