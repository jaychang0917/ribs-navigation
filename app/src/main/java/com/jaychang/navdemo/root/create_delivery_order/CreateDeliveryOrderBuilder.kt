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

import com.jaychang.navdemo.root.RootRouter
import com.jaychang.navdemo.root.RootView
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.DeliveryOrderRefinementBuilder
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.DeliveryOrderRefinementInteractor
import com.jaychang.navdemo.root.create_delivery_order.delivery_select_time.DeliverySelectTimeBuilder
import com.jaychang.navdemo.root.create_delivery_order.delivery_select_time.DeliverySelectTimeInteractor
import com.jaychang.navigation.ScreenStack
import com.uber.rib.core.Builder
import com.uber.rib.core.EmptyPresenter
import com.uber.rib.core.InteractorBaseComponent
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.BINARY

class CreateDeliveryOrderBuilder(
    dependency: ParentComponent
) : Builder<CreateDeliveryOrderRouter, CreateDeliveryOrderBuilder.ParentComponent>(dependency) {

    fun build(): CreateDeliveryOrderRouter {
        val interactor = CreateDeliveryOrderInteractor()
        val component = DaggerCreateDeliveryOrderBuilder_Component.builder()
            .parentComponent(dependency)
            .interactor(interactor)
            .build()
        return component.createDeliveryOrderRouter()
    }

    interface ParentComponent {
        fun rootView(): RootView

        fun rootRouter(): RootRouter

        fun screenStack(): ScreenStack

        fun listener(): CreateDeliveryOrderInteractor.Listener
    }

    @dagger.Module
    abstract class Module {

        @dagger.Module
        companion object {

            @CreateDeliveryOrderScope
            @Provides
            @JvmStatic
            fun presenter() = EmptyPresenter()

            @CreateDeliveryOrderScope
            @Provides
            @JvmStatic
            fun deliverySelectTimeListener(interactor: CreateDeliveryOrderInteractor): DeliverySelectTimeInteractor.Listener {
                return interactor.DeliverySelectTimeListener()
            }

            @CreateDeliveryOrderScope
            @Provides
            @JvmStatic
            fun deliveryOrderRefinementListener(interactor: CreateDeliveryOrderInteractor): DeliveryOrderRefinementInteractor.Listener {
                return interactor.DeliveryOrderRefinementListener()
            }

            @CreateDeliveryOrderScope
            @Provides
            @JvmStatic
            fun router(
                rootView: RootView,
                component: Component,
                interactor: CreateDeliveryOrderInteractor,
                deliverySelectTimeListener: DeliverySelectTimeInteractor.Listener,
                screenStack: ScreenStack
            ): CreateDeliveryOrderRouter {
                val deliverySelectTimeBuilder = DeliverySelectTimeBuilder(component)
                val deliveryOrderRefinementBuilder = DeliveryOrderRefinementBuilder(component)
                return CreateDeliveryOrderRouter(
                    interactor,
                    component,
                    rootView,
                    deliverySelectTimeListener,
                    deliverySelectTimeBuilder,
                    deliveryOrderRefinementBuilder,
                    screenStack
                )
            }
        }
    }

    @CreateDeliveryOrderScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<CreateDeliveryOrderInteractor>,
        DeliverySelectTimeBuilder.ParentComponent,
        DeliveryOrderRefinementBuilder.ParentComponent,
        BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: CreateDeliveryOrderInteractor): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun createDeliveryOrderRouter(): CreateDeliveryOrderRouter
    }

    @Scope
    @kotlin.annotation.Retention(BINARY)
    annotation class CreateDeliveryOrderScope

    @Qualifier
    @kotlin.annotation.Retention(BINARY)
    annotation class CreateDeliveryOrderInternal
}
