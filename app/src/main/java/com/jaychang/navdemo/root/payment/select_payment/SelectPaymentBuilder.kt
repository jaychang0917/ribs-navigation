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

package com.jaychang.navdemo.root.payment.select_payment

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

class SelectPaymentBuilder(dependency: ParentComponent) : ViewBuilder<SelectPaymentView, SelectPaymentRouter, SelectPaymentBuilder.ParentComponent>(dependency) {

  fun build(parentViewGroup: ViewGroup): SelectPaymentRouter {
    val view = createView(parentViewGroup)
    val interactor = SelectPaymentInteractor()
    val component = DaggerSelectPaymentBuilder_Component.builder()
        .parentComponent(dependency)
        .view(view)
        .interactor(interactor)
        .build()
    return component.deliverySelectTimeRouter()
  }

  override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): SelectPaymentView? {
    return inflater.inflate(R.layout.rib_select_payment, parentViewGroup, false) as SelectPaymentView
  }

  interface ParentComponent {
    fun selectPaymentListener(): SelectPaymentInteractor.Listener
  }

  @dagger.Module
  abstract class Module {

    @SelectPaymentScope
    @Binds
    abstract fun presenter(view: SelectPaymentView): SelectPaymentInteractor.Presenter

    @dagger.Module
    companion object {

      @SelectPaymentScope
      @Provides
      @JvmStatic
      fun router(
          component: Component,
          view: SelectPaymentView,
          interactor: SelectPaymentInteractor): SelectPaymentRouter {
        return SelectPaymentRouter(view, interactor, component)
      }
    }
  }

  @SelectPaymentScope
  @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
  interface Component : InteractorBaseComponent<SelectPaymentInteractor>, BuilderComponent {

    @dagger.Component.Builder
    interface Builder {
      @BindsInstance
      fun interactor(interactor: SelectPaymentInteractor): Builder

      @BindsInstance
      fun view(view: SelectPaymentView): Builder

      fun parentComponent(component: ParentComponent): Builder

      fun build(): Component
    }
  }

  interface BuilderComponent {
    fun deliverySelectTimeRouter(): SelectPaymentRouter
  }

  @Scope
  @kotlin.annotation.Retention(BINARY)
  annotation class SelectPaymentScope

  @Qualifier
  @kotlin.annotation.Retention(BINARY)
  annotation class SelectPaymentInternal
}
