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

package com.jaychang.navdemo.root.create_delivery_order.delivery_select_time

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

class DeliverySelectTimeBuilder(dependency: ParentComponent) : ViewBuilder<DeliverySelectTimeView, DeliverySelectTimeRouter, DeliverySelectTimeBuilder.ParentComponent>(dependency) {

  fun build(parentViewGroup: ViewGroup): DeliverySelectTimeRouter {
    val view = createView(parentViewGroup)
    val interactor = DeliverySelectTimeInteractor()
    val component = DaggerDeliverySelectTimeBuilder_Component.builder()
        .parentComponent(dependency)
        .view(view)
        .interactor(interactor)
        .build()
    return component.deliverySelectTimeRouter()
  }

  override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): DeliverySelectTimeView? {
    return inflater.inflate(R.layout.rib_delivery_select_time, parentViewGroup, false) as DeliverySelectTimeView
  }

  interface ParentComponent {
    fun deliverySelectTimeListener(): DeliverySelectTimeInteractor.Listener
  }

  @dagger.Module
  abstract class Module {

    @DeliverySelectTimeScope
    @Binds
    abstract fun presenter(view: DeliverySelectTimeView): DeliverySelectTimeInteractor.Presenter

    @dagger.Module
    companion object {

      @DeliverySelectTimeScope
      @Provides
      @JvmStatic
      fun router(
          component: Component,
          view: DeliverySelectTimeView,
          interactor: DeliverySelectTimeInteractor): DeliverySelectTimeRouter {
        return DeliverySelectTimeRouter(view, interactor, component)
      }
    }
  }

  @DeliverySelectTimeScope
  @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
  interface Component : InteractorBaseComponent<DeliverySelectTimeInteractor>, BuilderComponent {

    @dagger.Component.Builder
    interface Builder {
      @BindsInstance
      fun interactor(interactor: DeliverySelectTimeInteractor): Builder

      @BindsInstance
      fun view(view: DeliverySelectTimeView): Builder

      fun parentComponent(component: ParentComponent): Builder

      fun build(): Component
    }
  }

  interface BuilderComponent {
    fun deliverySelectTimeRouter(): DeliverySelectTimeRouter
  }

  @Scope
  @kotlin.annotation.Retention(BINARY)
  annotation class DeliverySelectTimeScope

  @Qualifier
  @kotlin.annotation.Retention(BINARY)
  annotation class DeliverySelectTimeInternal
}
