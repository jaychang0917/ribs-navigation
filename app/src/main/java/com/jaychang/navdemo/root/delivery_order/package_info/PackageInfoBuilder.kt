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

package com.jaychang.navdemo.root.delivery_order.package_info

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

class PackageInfoBuilder(
    dependency: ParentComponent
) : ViewBuilder<PackageInfoView, PackageInfoRouter, PackageInfoBuilder.ParentComponent>(dependency) {

    fun build(parentViewGroup: ViewGroup): PackageInfoRouter {
        val view = createView(parentViewGroup)
        val interactor = PackageInfoInteractor()
        val component = DaggerPackageInfoBuilder_Component.builder()
            .parentComponent(dependency)
            .view(view)
            .interactor(interactor)
            .build()
        return component.packageInfoRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): PackageInfoView? {
        return inflater.inflate(R.layout.rib_delivery_package_info, parentViewGroup, false) as PackageInfoView
    }

    interface ParentComponent {
    }

    @dagger.Module
    abstract class Module {

        @PackageInfoScope
        @Binds
        abstract fun presenter(view: PackageInfoView): PackageInfoInteractor.Presenter

        @dagger.Module
        companion object {

            @PackageInfoScope
            @Provides
            @JvmStatic
            fun router(
                component: Component,
                view: PackageInfoView,
                interactor: PackageInfoInteractor
            ): PackageInfoRouter {
                return PackageInfoRouter(view, interactor, component)
            }
        }
    }

    @PackageInfoScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<PackageInfoInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: PackageInfoInteractor): Builder

            @BindsInstance
            fun view(view: PackageInfoView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun packageInfoRouter(): PackageInfoRouter
    }
}

@Scope
@kotlin.annotation.Retention(BINARY)
annotation class PackageInfoScope

@Qualifier
@kotlin.annotation.Retention(BINARY)
annotation class PackageInfoInternal
