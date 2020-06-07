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

import com.jaychang.navdemo.root.RootRouter
import com.jaychang.navdemo.root.RootView
import com.jaychang.navdemo.root.delivery_order.dropoff.DropoffBuilder
import com.jaychang.navdemo.root.delivery_order.package_info.PackageInfoBuilder
import com.jaychang.navdemo.root.delivery_order.pickup.PickupBuilder
import com.jaychang.navdemo.root.delivery_order.schedule.ScheduleBuilder
import com.jaychang.navdemo.root.delivery_order.summary.SummaryBuilder
import com.jaychang.navigation.ScreenStack
import com.uber.rib.core.Builder
import com.uber.rib.core.EmptyPresenter
import com.uber.rib.core.InteractorBaseComponent
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.BINARY

class DeliveryOrderBuilder(
    dependency: ParentComponent
) : Builder<DeliveryOrderRouter, DeliveryOrderBuilder.ParentComponent>(dependency) {

    fun build(): DeliveryOrderRouter {
        val interactor = DeliveryOrderInteractor()
        val component = DaggerDeliveryOrderBuilder_Component.builder()
            .parentComponent(dependency)
            .interactor(interactor)
            .build()
        return component.deliveryOrderRouter()
    }

    interface ParentComponent {
        fun rootView(): RootView

        fun rootRouter(): RootRouter

        fun screenStack(): ScreenStack
    }

    @dagger.Module
    abstract class Module {

        @dagger.Module
        companion object {

            @DeliveryOrderScope
            @Provides
            @JvmStatic
            fun presenter() = EmptyPresenter()

            @DeliveryOrderScope
            @Provides
            @JvmStatic
            fun router(
                rootView: RootView,
                component: Component,
                interactor: DeliveryOrderInteractor,
                screenStack: ScreenStack
            ): DeliveryOrderRouter {
                val pickupBuilder = PickupBuilder(component)
                val dropoffBuilder = DropoffBuilder(component)
                val scheduleBuilder = ScheduleBuilder(component)
                val packageInfoBuilder = PackageInfoBuilder(component)
                val summaryBuilder = SummaryBuilder(component)
                return DeliveryOrderRouter(
                    interactor,
                    component,
                    rootView,
                    pickupBuilder,
                    dropoffBuilder,
                    scheduleBuilder,
                    packageInfoBuilder,
                    summaryBuilder,
                    screenStack
                )
            }
        }
    }

    @DeliveryOrderScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<DeliveryOrderInteractor>,
        PickupBuilder.ParentComponent,
        DropoffBuilder.ParentComponent,
        ScheduleBuilder.ParentComponent,
        PackageInfoBuilder.ParentComponent,
        SummaryBuilder.ParentComponent,
        BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: DeliveryOrderInteractor): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun deliveryOrderRouter(): DeliveryOrderRouter
    }
}

@Scope
@kotlin.annotation.Retention(BINARY)
annotation class DeliveryOrderScope

@Qualifier
@kotlin.annotation.Retention(BINARY)
annotation class DeliveryOrderInternal
