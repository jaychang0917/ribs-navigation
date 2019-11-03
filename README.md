# ribs-navigation

This repo aims to demostrate a solution to handle navigation with RIBs for Android. 

| Demo | RIB Tree |
| --- | --- |
|<img src="https://github.com/jaychang0917/ribs-navigation/blob/master/arts/demo.gif" width="296" height="414">|<img src="https://github.com/jaychang0917/ribs-navigation/blob/master/arts/rib-tree.png" width="500" height="300">|

## Table of Contents
* [Push / pop screen(s)](#push_pop)
* [Present / dismiss screen(s)](#present_dismiss)
* [Back Navigation](#back_nav)
* [Transition](#transition)

### <a name=push_pop>Push / pop screen(s)</a>
#### Push a screen
```kotlin
fun attachDeliverySelectTime() {
    val router = deliverySelectTimeBuilder.build(view)
    val screen = DeliverySelectTimeScreen(router.view)
    // a Router extension to observe screen event to invoke attachChild() / detachChild()
    observeScreenEvent(screen, router)
    screenStack.pushScreen(screen)
}
```
#### Pop a screen
```kotlin
fun detachDeliverySelectTime() {
    screenStack.popScreen()
}
```
#### Pop to a specified screen
```kotlin
fun detachDeliveryOrderRefinement() {
    val router = deliveryOrderRefinementRouter ?: return
    detachChild(router)
    screenStack.popToScreen(DeliveryPackageInfoScreen::class, inclusive = true)
    deliveryOrderRefinementRouter = null
}
```

### <a name=present_dismiss>Present / dismiss screen(s)</a>
In comparision to push/pop mechanism, `presentScreen()` doesn't remove the current view from parent ViewGroup, but `pushScreen()` does. Use this if you want to stack a screen on top of current screen.

### <a name=back_nav>Back Navigation</a>
To support back navigation, 

1. Observes the screen event using `observeScreenEvent()` so that `detachChild()` is called when back pressed.
2. Override the `RootRouter.handleBackPress()` to propagate the event to screens. The `ViewProvider.onBackPress()` callbacks follow the Chain of Responsibility pattern. Each callback in the chain is invoked only if the preceding callback return `false`.
```kotlin
override fun handleBackPress(): Boolean {
    for (transaction in screenStack) {
        val screen = transaction.screen
        if (screen.onBackPress() || screenStack.handleBackPress()) {
           return true
        }
    }
    return super.handleBackPress()
}
```
```kotlin
// SelectPaymentScreen is the first screen of payment flow, we should detach the viewless payment RIB along with this screen when back pressed.
class SelectPaymentScreen(
    private val view: SelectPaymentView,
    private val listener: PaymentInteractor.Listener
) : ViewProvider() {
    override fun buildView(parentView: ViewGroup): View = view

    override fun onBackPress(): Boolean {
        listener.detachPayment()
        return true
    }
}
```

### <a name=transition>Transition</a>
Each push/pop/present/dismiss function accepts a `Transiton` as an argument. If it is not provided, the default transition provided in `ScreenStack` will be used. No transition will be used by default.
```kotlin
@RootScope
@Provides
fun screenStack(view: RootView) = ScreenStack(view) { HorizontalTransition() }
```

To create a custom transtion, implement the `Transition` interface.
```kotlin
class AwesomeTransition : Transition {
    override fun animate(from: View, to: View, direction: ScreenStack.Direction, onAnimationEnd: () -> Unit) {
        // your imagination here
    }
}
```
```kotlin
screenStack.pushScreen(screen, AwesomeTransition())
```

## References
* [Provide custom back navigation](https://developer.android.com/guide/navigation/navigation-custom-back)
