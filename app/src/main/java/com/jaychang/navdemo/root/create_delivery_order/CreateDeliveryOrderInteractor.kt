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

package com.jaychang.navdemo.root.create_delivery_order

import com.jaychang.navdemo.root.create_delivery_order.delivery_order_refinement.DeliveryOrderRefinementInteractor
import com.jaychang.navdemo.root.create_delivery_order.delivery_select_time.DeliverySelectTimeInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.EmptyPresenter
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

@RibInteractor
class CreateDeliveryOrderInteractor : Interactor<EmptyPresenter, CreateDeliveryOrderRouter>() {

    @Inject
    lateinit var listener: Listener

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        router.attachDeliverySelectTime()
    }

    override fun willResignActive() {
        super.willResignActive()
        println("${javaClass.simpleName} willResignActive")
    }

    interface Listener {
        fun detachCreateDeliveryOrder()
    }
    
    inner class DeliverySelectTimeListener : DeliverySelectTimeInteractor.Listener {
        override fun onBackButtonClicked() {
            router.detachDeliverySelectTime()
            listener.detachCreateDeliveryOrder()
        }

        override fun onNextButtonClicked() {
            router.attachDeliveryOrderRefinement()
        }
    }

    inner class DeliveryOrderRefinementListener: DeliveryOrderRefinementInteractor.Listener {
        override fun detachDeliveryOrderRefinement() {
            router.detachDeliveryOrderRefinement()
        }
    }
}
