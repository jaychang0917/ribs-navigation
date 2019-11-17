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

package com.jaychang.navdemo.root

import com.jaychang.navdemo.root.create_delivery_order.CreateDeliveryOrderBuilder
import com.jaychang.navdemo.root.create_delivery_order.CreateDeliveryOrderRouter
import com.jaychang.navdemo.root.create_delivery_order.delivery_select_time.DeliverySelectTimeScreen
import com.jaychang.navdemo.root.payment.PaymentBuilder
import com.jaychang.navdemo.root.payment.PaymentRouter
import com.jaychang.navdemo.root.payment.select_payment.SelectPaymentScreen
import com.jaychang.navigation.ScreenStack
import com.uber.rib.core.ViewRouter

class RootRouter(
    view: RootView,
    interactor: RootInteractor,
    component: RootBuilder.Component,
    private val createDeliveryOrderBuilder: CreateDeliveryOrderBuilder,
    private val paymentBuilder: PaymentBuilder,
    private val screenStack: ScreenStack
) : ViewRouter<RootView, RootInteractor, RootBuilder.Component>(view, interactor, component) {
    private var createDeliveryOrderRouter: CreateDeliveryOrderRouter? = null
    private var paymentRouter: PaymentRouter? = null

    fun attachCreateDeliveryOrder() {
        val router = createDeliveryOrderBuilder.build()
        attachChild(router)
        createDeliveryOrderRouter = router
    }

    fun detachCreateDeliveryOrder() {
        val router = createDeliveryOrderRouter ?: return
        detachChild(router)
        screenStack.popToScreen(DeliverySelectTimeScreen::class, true)
        createDeliveryOrderRouter = null
    }

    fun attachPayment() {
        val router = paymentBuilder.build()
        attachChild(router)
        paymentRouter = router
    }

    fun detachPayment() {
        val router = paymentRouter ?: return
        detachChild(router)
        screenStack.dismissToScreen(SelectPaymentScreen::class, true)
        paymentRouter = null
    }

    override fun handleBackPress(): Boolean {
        if (screenStack.isRoot()) return false

        for (transaction in screenStack) {
            val screen = transaction.screen
            if (screen.onBackPress() || screenStack.handleBackPress()) {
                return true
            }
        }

        return super.handleBackPress()
    }
}
