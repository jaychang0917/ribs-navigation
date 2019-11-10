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

import com.jaychang.navdemo.root.RootRouter
import com.jaychang.navdemo.root.RootView
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.delivery_order_summary.DeliveryOrderSummaryBuilder
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.delivery_package_info.DeliveryPackageInfoBuilder
import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.delivery_package_info.DeliveryPackageInfoInteractor
import com.jaychang.navigation.ScreenStack
import com.uber.rib.core.Builder
import com.uber.rib.core.EmptyPresenter
import com.uber.rib.core.InteractorBaseComponent
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.BINARY

class DeliveryOrderRefinementBuilder(
    dependency: ParentComponent
) : Builder<DeliveryOrderRefinementRouter, DeliveryOrderRefinementBuilder.ParentComponent>(dependency) {

    fun build(): DeliveryOrderRefinementRouter {
        val interactor = DeliveryOrderRefinementInteractor()
        val component = DaggerDeliveryOrderRefinementBuilder_Component.builder()
            .parentComponent(dependency)
            .interactor(interactor)
            .build()
        return component.deliveryOrderRefinementRouter()
    }

    interface ParentComponent {
        fun screenStack(): ScreenStack

        fun rootView(): RootView

        fun rootRouter(): RootRouter

        fun listener(): DeliveryOrderRefinementInteractor.Listener
    }

    @dagger.Module
    abstract class Module {

        @dagger.Module
        companion object {

            @DeliveryOrderRefinementScope
            @Provides
            @JvmStatic
            fun presenter() = EmptyPresenter()

            @DeliveryOrderRefinementScope
            @Provides
            @JvmStatic
            fun deliveryPackageInfoListener(interactor: DeliveryOrderRefinementInteractor): DeliveryPackageInfoInteractor.Listener {
                return interactor.DeliveryPackageInfoListener()
            }

            @DeliveryOrderRefinementScope
            @Provides
            @JvmStatic
            fun router(
                component: Component,
                interactor: DeliveryOrderRefinementInteractor,
                rootView: RootView,
                deliveryPackageInfoListener: DeliveryPackageInfoInteractor.Listener,
                screenStack: ScreenStack
            ): DeliveryOrderRefinementRouter {
                val deliveryPackageInfoBuilder = DeliveryPackageInfoBuilder(component)
                val deliveryOrderSummaryBuilder = DeliveryOrderSummaryBuilder(component)
                return DeliveryOrderRefinementRouter(
                    interactor,
                    component,
                    rootView,
                    deliveryPackageInfoListener,
                    deliveryPackageInfoBuilder,
                    deliveryOrderSummaryBuilder,
                    screenStack
                )
            }
        }
    }

    @DeliveryOrderRefinementScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<DeliveryOrderRefinementInteractor>,
        DeliveryPackageInfoBuilder.ParentComponent,
        DeliveryOrderSummaryBuilder.ParentComponent,
        BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: DeliveryOrderRefinementInteractor): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun deliveryOrderRefinementRouter(): DeliveryOrderRefinementRouter
    }

    @Scope
    @kotlin.annotation.Retention(BINARY)
    annotation class DeliveryOrderRefinementScope

    @Qualifier
    @kotlin.annotation.Retention(BINARY)
    annotation class DeliveryOrderRefinementInternal
}
