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

import com.jaychang.navdemo.root.RootRouter
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import javax.inject.Inject

@RibInteractor
class DeliveryOrderSummaryInteractor : Interactor<DeliveryOrderSummaryInteractor.Presenter, DeliveryOrderSummaryRouter>() {

    @Inject
    lateinit var presenter: Presenter

    @Inject
    lateinit var rootRouter: RootRouter

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.selectPaymentButtonClicks()
            .subscribe {
                rootRouter.attachPayment()
            }
    }

    override fun willResignActive() {
        super.willResignActive()
        println("${javaClass.simpleName} willResignActive")
    }

    interface Presenter {
        fun selectPaymentButtonClicks(): Observable<Unit>
    }
}
