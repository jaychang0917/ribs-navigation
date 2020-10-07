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

package com.jaychang.navdemo.root.delivery_order.pickup

import android.view.LayoutInflater
import android.view.ViewGroup
import com.jaychang.navdemo.R
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.BINARY

class PickupBuilder(
    dependency: ParentComponent
) : ViewBuilder<PickupView, PickupRouter, PickupBuilder.ParentComponent>(dependency) {

    fun build(parentViewGroup: ViewGroup): PickupRouter {
        val view = createView(parentViewGroup)
        val interactor = PickupInteractor()
        val component = DaggerPickupBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.pickupRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): PickupView? {
        return inflater.inflate(R.layout.rib_delivery_pickup, parentViewGroup, false) as PickupView
    }

    interface ParentComponent {
    }

    @dagger.Module
    abstract class Module {

        @PickupScope
        @Binds
        abstract fun presenter(view: PickupView): PickupInteractor.Presenter

        @dagger.Module
        companion object {

            @PickupScope
            @Provides
            @JvmStatic
            fun router(
                component: Component,
                view: PickupView,
                interactor: PickupInteractor
            ): PickupRouter {
                return PickupRouter(view, interactor, component)
            }
        }
    }

    @PickupScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<PickupInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: PickupInteractor): Builder

            @BindsInstance
            fun view(view: PickupView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun pickupRouter(): PickupRouter
    }
}

@Scope
@kotlin.annotation.Retention(BINARY)
annotation class PickupScope

@Qualifier
@kotlin.annotation.Retention(BINARY)
annotation class PickupInternal
