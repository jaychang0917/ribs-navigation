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

package com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.delivery_order_summary

import android.view.LayoutInflater
import android.view.ViewGroup
import com.jaychang.navdemo.R
import com.jaychang.navdemo.root.RootRouter
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.BINARY

class DeliveryOrderSummaryBuilder(dependency: ParentComponent) : ViewBuilder<DeliveryOrderSummaryView, DeliveryOrderSummaryRouter, DeliveryOrderSummaryBuilder.ParentComponent>(dependency) {

  fun build(parentViewGroup: ViewGroup): DeliveryOrderSummaryRouter {
    val view = createView(parentViewGroup)
    val interactor =
      DeliveryOrderSummaryInteractor()
    val component = DaggerDeliveryOrderSummaryBuilder_Component.builder()
        .parentComponent(dependency)
        .view(view)
        .interactor(interactor)
        .build()
    return component.deliveryOrderSummaryRouter()
  }

  override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): DeliveryOrderSummaryView? {
    return inflater.inflate(R.layout.rib_delivery_order_summary, parentViewGroup, false) as DeliveryOrderSummaryView
  }

  interface ParentComponent {
    fun rootRouter(): RootRouter
  }

  @dagger.Module
  abstract class Module {

    @DeliveryOrderSummaryScope
    @Binds
    abstract fun presenter(view: DeliveryOrderSummaryView): DeliveryOrderSummaryInteractor.Presenter

    @dagger.Module
    companion object {

      @DeliveryOrderSummaryScope
      @Provides
      @JvmStatic
      fun router(
        component: Component,
        view: DeliveryOrderSummaryView,
        interactor: DeliveryOrderSummaryInteractor
      ): DeliveryOrderSummaryRouter {
        return DeliveryOrderSummaryRouter(
          view,
          interactor,
          component
        )
      }
    }
  }

  @DeliveryOrderSummaryScope
  @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
  interface Component : InteractorBaseComponent<DeliveryOrderSummaryInteractor>,
    BuilderComponent {

    @dagger.Component.Builder
    interface Builder {
      @BindsInstance
      fun interactor(interactor: DeliveryOrderSummaryInteractor): Builder

      @BindsInstance
      fun view(view: DeliveryOrderSummaryView): Builder

      fun parentComponent(component: ParentComponent): Builder

      fun build(): Component
    }
  }

  interface BuilderComponent {
    fun deliveryOrderSummaryRouter(): DeliveryOrderSummaryRouter
  }

  @Scope
  @kotlin.annotation.Retention(BINARY)
  annotation class DeliveryOrderSummaryScope

  @Qualifier
  @kotlin.annotation.Retention(BINARY)
  annotation class DeliveryOrderSummaryInternal
}
