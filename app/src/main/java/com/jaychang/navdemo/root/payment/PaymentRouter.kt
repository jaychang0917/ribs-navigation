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

package com.jaychang.navdemo.root.payment

import com.jaychang.navdemo.root.RootView
import com.jaychang.navdemo.root.payment.add_credit_card.AddCreditCardBuilder
import com.jaychang.navdemo.root.payment.add_credit_card.AddCreditCardScreen
import com.jaychang.navdemo.root.payment.select_payment.SelectPaymentBuilder
import com.jaychang.navdemo.root.payment.select_payment.SelectPaymentScreen
import com.jaychang.navdemo.RouterEx
import com.jaychang.screenstack.ScreenStack
import com.jaychang.screenstack.VerticalTransition

class PaymentRouter(
    private val view: RootView,
    interactor: PaymentInteractor,
    component: PaymentBuilder.Component,
    private val selectPaymentBuilder: SelectPaymentBuilder,
    private val addCreditCardBuilder: AddCreditCardBuilder,
    private val screenStack: ScreenStack
) : RouterEx<PaymentInteractor, PaymentBuilder.Component>(interactor, component) {
    fun attachSelectPayment() {
        val router = selectPaymentBuilder.build(view)
        val screen = SelectPaymentScreen(router.view, interactor.listener)
        observeScreenEvent(screen, router)
        screenStack.presentScreen(screen, VerticalTransition())
    }

    fun detachSelectPayment() {
        screenStack.dismissScreen(VerticalTransition())
    }

    fun attachAddCreditCard() {
        val router = addCreditCardBuilder.build(view)
        val screen = AddCreditCardScreen(router.view)
        observeScreenEvent(screen, router)
        screenStack.pushScreen(screen)
    }

    fun detachAddCreditCard() {
        screenStack.popScreen()
    }
}