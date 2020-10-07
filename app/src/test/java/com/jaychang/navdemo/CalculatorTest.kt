package com.jaychang.navdemo

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

class CalculatorTest {
    @Test
    fun add() {
       val calculator = Calculator()

       Truth.assertThat(calculator.add(1, 1)).isEqualTo(2)
    }
}
