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

package com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.delivery_package_info

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

class DeliveryPackageInfoBuilder(dependency: ParentComponent) : ViewBuilder<DeliveryPackageInfoView, DeliveryPackageInfoRouter, DeliveryPackageInfoBuilder.ParentComponent>(dependency) {

  fun build(parentViewGroup: ViewGroup): DeliveryPackageInfoRouter {
    val view = createView(parentViewGroup)
    val interactor =
      DeliveryPackageInfoInteractor()
    val component = DaggerDeliveryPackageInfoBuilder_Component.builder()
        .parentComponent(dependency)
        .view(view)
        .interactor(interactor)
        .build()
    return component.deliveryPackageInfoRouter()
  }

  override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): DeliveryPackageInfoView? {
    return inflater.inflate(R.layout.rib_delivery_package_info, parentViewGroup, false) as DeliveryPackageInfoView
  }

  interface ParentComponent {
    fun deliveryPackageInfoListener(): DeliveryPackageInfoInteractor.Listener
  }

  @dagger.Module
  abstract class Module {

    @DeliveryPackageInfoScope
    @Binds
    abstract fun presenter(view: DeliveryPackageInfoView): DeliveryPackageInfoInteractor.Presenter

    @dagger.Module
    companion object {

      @DeliveryPackageInfoScope
      @Provides
      @JvmStatic
      fun router(
        component: Component,
        view: DeliveryPackageInfoView,
        interactor: DeliveryPackageInfoInteractor
      ): DeliveryPackageInfoRouter {
        return DeliveryPackageInfoRouter(
          view,
          interactor,
          component
        )
      }
    }
  }

  @DeliveryPackageInfoScope
  @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
  interface Component : InteractorBaseComponent<DeliveryPackageInfoInteractor>,
    BuilderComponent {

    @dagger.Component.Builder
    interface Builder {
      @BindsInstance
      fun interactor(interactor: DeliveryPackageInfoInteractor): Builder

      @BindsInstance
      fun view(view: DeliveryPackageInfoView): Builder

      fun parentComponent(component: ParentComponent): Builder

      fun build(): Component
    }
  }

  interface BuilderComponent {
    fun deliveryPackageInfoRouter(): DeliveryPackageInfoRouter
  }

  @Scope
  @kotlin.annotation.Retention(BINARY)
  annotation class DeliveryPackageInfoScope

  @Qualifier
  @kotlin.annotation.Retention(BINARY)
  annotation class DeliveryPackageInfoInternal
}
