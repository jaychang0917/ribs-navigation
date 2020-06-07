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

package com.jaychang.navdemo.root.delivery_order.dropoff

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

class DropoffBuilder(
    dependency: ParentComponent
) : ViewBuilder<DropoffView, DropoffRouter, DropoffBuilder.ParentComponent>(dependency) {

    fun build(parentViewGroup: ViewGroup): DropoffRouter {
        val view = createView(parentViewGroup)
        val interactor = DropoffInteractor()
        val component = DaggerDropoffBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.dropoffRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): DropoffView? {
        return inflater.inflate(R.layout.rib_delivery_dropoff, parentViewGroup, false) as DropoffView
    }

    interface ParentComponent {
    }

    @dagger.Module
    abstract class Module {

        @DropoffScope
        @Binds
        abstract fun presenter(view: DropoffView): DropoffInteractor.Presenter

        @dagger.Module
        companion object {

            @DropoffScope
            @Provides
            @JvmStatic
            fun router(
                component: Component,
                view: DropoffView,
                interactor: DropoffInteractor
            ): DropoffRouter {
                return DropoffRouter(view, interactor, component)
            }
        }
    }

    @DropoffScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<DropoffInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: DropoffInteractor): Builder

            @BindsInstance
            fun view(view: DropoffView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun dropoffRouter(): DropoffRouter
    }
}

@Scope
@kotlin.annotation.Retention(BINARY)
annotation class DropoffScope

@Qualifier
@kotlin.annotation.Retention(BINARY)
annotation class DropoffInternal
