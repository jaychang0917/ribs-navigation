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

package com.jaychang.navdemo.root.delivery_order.summary

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

class SummaryBuilder(
    dependency: ParentComponent
) : ViewBuilder<SummaryView, SummaryRouter, SummaryBuilder.ParentComponent>(dependency) {

    fun build(parentViewGroup: ViewGroup): SummaryRouter {
        val view = createView(parentViewGroup)
        val interactor = SummaryInteractor()
        val component = DaggerSummaryBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.SummaryRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): SummaryView? {
        return inflater.inflate(R.layout.rib_delivery_summary, parentViewGroup, false) as SummaryView
    }

    interface ParentComponent {
    }

    @dagger.Module
    abstract class Module {

        @SummaryScope
        @Binds
        abstract fun presenter(view: SummaryView): SummaryInteractor.Presenter

        @dagger.Module
        companion object {

            @SummaryScope
            @Provides
            @JvmStatic
            fun router(
                component: Component,
                view: SummaryView,
                interactor: SummaryInteractor
            ): SummaryRouter {
                return SummaryRouter(view, interactor, component)
            }
        }
    }

    @SummaryScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<SummaryInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: SummaryInteractor): Builder

            @BindsInstance
            fun view(view: SummaryView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun SummaryRouter(): SummaryRouter
    }
}

@Scope
@kotlin.annotation.Retention(BINARY)
annotation class SummaryScope

@Qualifier
@kotlin.annotation.Retention(BINARY)
annotation class SummaryInternal
