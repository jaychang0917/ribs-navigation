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

package com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement

import com.jaychang.navdemo.RouterEx
import com.jaychang.navdemo.root.RootView
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.delivery_order_summary.DeliveryOrderSummaryBuilder
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.delivery_order_summary.DeliveryOrderSummaryScreen
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.delivery_package_info.DeliveryPackageInfoBuilder
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.delivery_package_info.DeliveryPackageInfoInteractor
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.delivery_package_info.DeliveryPackageInfoScreen
import com.jaychang.navigation.ScreenStack

class DeliveryOrderRefinementRouter(
    interactor: DeliveryOrderRefinementInteractor,
    component: DeliveryOrderRefinementBuilder.Component,
    private val rootView: RootView,
    private val deliveryPackageInfoListener: DeliveryPackageInfoInteractor.Listener,
    private val deliveryPackageInfoBuilder: DeliveryPackageInfoBuilder,
    private val deliveryOrderSummaryBuilder: DeliveryOrderSummaryBuilder,
    private val screenStack: ScreenStack
) : RouterEx<DeliveryOrderRefinementInteractor, DeliveryOrderRefinementBuilder.Component>(interactor, component) {

    fun attachDeliveryPackageInfo() {
        val router = deliveryPackageInfoBuilder.build(rootView)
        val screen = DeliveryPackageInfoScreen(router.view, deliveryPackageInfoListener)
        observeScreenEvent(screen, router)
        screenStack.pushScreen(screen)
    }

    fun detachDeliveryPackageInfo() {
        screenStack.popScreen()
    }

    fun attachDeliveryOrderSummary() {
        val router = deliveryOrderSummaryBuilder.build(rootView)
        val screen = DeliveryOrderSummaryScreen(router.view)
        observeScreenEvent(screen, router)
        screenStack.presentScreen(screen)
    }

    fun detachDeliveryOrderSummary() {
        screenStack.popScreen()
    }
}
