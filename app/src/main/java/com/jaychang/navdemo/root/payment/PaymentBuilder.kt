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
import com.jaychang.navdemo.root.payment.add_credit_card.AddCreditCardInteractor
import com.jaychang.navdemo.root.payment.select_payment.SelectPaymentBuilder
import com.jaychang.navdemo.root.payment.select_payment.SelectPaymentInteractor
import com.jaychang.navigation.ScreenStack
import com.uber.rib.core.Builder
import com.uber.rib.core.EmptyPresenter
import com.uber.rib.core.InteractorBaseComponent
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.BINARY

class PaymentBuilder(
    dependency: ParentComponent
) : Builder<PaymentRouter, PaymentBuilder.ParentComponent>(dependency) {

    fun build(): PaymentRouter {
        val interactor = PaymentInteractor()
        val component = DaggerPaymentBuilder_Component.builder()
            .parentComponent(dependency)
            .interactor(interactor)
            .build()
        return component.paymentFlowRouter()
    }

    interface ParentComponent {
        fun rootView(): RootView

        fun screenStack(): ScreenStack

        fun paymentFlowListener(): PaymentInteractor.Listener
    }

    @dagger.Module
    abstract class Module {

        @dagger.Module
        companion object {

            @PaymentFlowScope
            @Provides
            @JvmStatic
            fun presenter() = EmptyPresenter()

            @PaymentFlowScope
            @Provides
            @JvmStatic
            fun selectPaymentListener(interactor: PaymentInteractor): SelectPaymentInteractor.Listener {
                return interactor.SelectPaymentListener()
            }

            @PaymentFlowScope
            @Provides
            @JvmStatic
            fun addCreditCardListener(interactor: PaymentInteractor): AddCreditCardInteractor.Listener {
                return interactor.AddCreditCardListener()
            }

            @PaymentFlowScope
            @Provides
            @JvmStatic
            fun router(
                rootView: RootView,
                component: Component,
                interactor: PaymentInteractor,
                selectPaymentListener: SelectPaymentInteractor.Listener,
                screenStack: ScreenStack
            ): PaymentRouter {
                val selectPaymentBuilder = SelectPaymentBuilder(component)
                val addCreditCardBuilder = AddCreditCardBuilder(component)
                return PaymentRouter(
                    interactor,
                    component,
                    rootView,
                    selectPaymentListener,
                    selectPaymentBuilder,
                    addCreditCardBuilder,
                    screenStack
                )
            }
        }
    }

    @PaymentFlowScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<PaymentInteractor>,
        AddCreditCardBuilder.ParentComponent,
        SelectPaymentBuilder.ParentComponent,
        BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: PaymentInteractor): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun paymentFlowRouter(): PaymentRouter
    }

    @Scope
    @kotlin.annotation.Retention(BINARY)
    annotation class PaymentFlowScope

    @Qualifier
    @kotlin.annotation.Retention(BINARY)
    annotation class PaymentFlowInternal
}
