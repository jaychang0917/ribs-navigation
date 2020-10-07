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

package com.jaychang.navdemo.root.delivery_order

import com.jaychang.navdemo.RouterEx
import com.jaychang.navdemo.root.RootView
import com.jaychang.navdemo.root.delivery_order.dropoff.DropoffBuilder
import com.jaychang.navdemo.root.delivery_order.package_info.PackageInfoBuilder
import com.jaychang.navdemo.root.delivery_order.pickup.PickupBuilder
import com.jaychang.navdemo.root.delivery_order.pickup.PickupScreen
import com.jaychang.navdemo.root.delivery_order.schedule.ScheduleBuilder
import com.jaychang.navdemo.root.delivery_order.summary.SummaryBuilder
import com.jaychang.navigation.ScreenStack

class DeliveryOrderRouter(
    interactor: DeliveryOrderInteractor,
    component: DeliveryOrderBuilder.Component,
    private val rootView: RootView,
    private val pickupBuilder: PickupBuilder,
    private val dropoffBuilder: DropoffBuilder,
    private val scheduleBuilder: ScheduleBuilder,
    private val packageInfoBuilder: PackageInfoBuilder,
    private val summaryBuilder: SummaryBuilder,
    private val screenStack: ScreenStack
) : RouterEx<DeliveryOrderInteractor, DeliveryOrderBuilder.Component>(interactor, component) {
    fun attachPickup() {
        val router = pickupBuilder.build(rootView)
        val screen = PickupScreen(router.view)
        observeScreenEvent(screen, router)
        screenStack.pushScreen(screen)
    }

    fun detachPickup() {
        screenStack.popScreen()
    }
}
