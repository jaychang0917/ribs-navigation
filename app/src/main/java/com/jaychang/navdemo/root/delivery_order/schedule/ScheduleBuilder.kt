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

package com.jaychang.navdemo.root.delivery_order.schedule

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

class ScheduleBuilder(
    dependency: ParentComponent
) : ViewBuilder<ScheduleView, ScheduleRouter,ScheduleBuilder.ParentComponent>(dependency) {

    fun build(parentViewGroup: ViewGroup): ScheduleRouter {
        val view = createView(parentViewGroup)
        val interactor = ScheduleInteractor()
        val component = DaggerScheduleBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.scheduleRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): ScheduleView? {
        return inflater.inflate(R.layout.rib_delivery_schedule, parentViewGroup, false) as ScheduleView
    }

    interface ParentComponent {
    }

    @dagger.Module
    abstract class Module {

        @ScheduleScope
        @Binds
        abstract fun presenter(view: ScheduleView): ScheduleInteractor.Presenter

        @dagger.Module
        companion object {

            @ScheduleScope
            @Provides
            @JvmStatic
            fun router(
                component: Component,
                view: ScheduleView,
                interactor: ScheduleInteractor
            ): ScheduleRouter {
                return ScheduleRouter(view, interactor, component)
            }
        }
    }

    @ScheduleScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<ScheduleInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: ScheduleInteractor): Builder

            @BindsInstance
            fun view(view: ScheduleView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun scheduleRouter(): ScheduleRouter
    }
}

@Scope
@kotlin.annotation.Retention(BINARY)
annotation class ScheduleScope

@Qualifier
@kotlin.annotation.Retention(BINARY)
annotation class ScheduleInternal
