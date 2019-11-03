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

package com.jaychang.navdemo.root.payment.add_credit_card

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

class AddCreditCardBuilder(
    dependency: ParentComponent
) : ViewBuilder<AddCreditCardView, AddCreditCardRouter, AddCreditCardBuilder.ParentComponent>(dependency) {

    fun build(parentViewGroup: ViewGroup): AddCreditCardRouter {
        val view = createView(parentViewGroup)
        val interactor = AddCreditCardInteractor()
        val component = DaggerAddCreditCardBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.deliverySelectTimeRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): AddCreditCardView? {
        return inflater.inflate(R.layout.rib_add_credit_card, parentViewGroup, false) as AddCreditCardView
    }

    interface ParentComponent {
        fun addCreditCardListener(): AddCreditCardInteractor.Listener
    }

    @dagger.Module
    abstract class Module {

        @AddCreditCardScope
        @Binds
        abstract fun presenter(view: AddCreditCardView): AddCreditCardInteractor.Presenter

        @dagger.Module
        companion object {

            @AddCreditCardScope
            @Provides
            @JvmStatic
            fun router(
                component: Component,
                view: AddCreditCardView,
                interactor: AddCreditCardInteractor
            ): AddCreditCardRouter {
                return AddCreditCardRouter(view, interactor, component)
            }
        }
    }

    @AddCreditCardScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<AddCreditCardInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: AddCreditCardInteractor): Builder

            @BindsInstance
            fun view(view: AddCreditCardView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun deliverySelectTimeRouter(): AddCreditCardRouter
    }

    @Scope
    @kotlin.annotation.Retention(BINARY)
    annotation class AddCreditCardScope

    @Qualifier
    @kotlin.annotation.Retention(BINARY)
    annotation class AddCreditCardInternal
}
