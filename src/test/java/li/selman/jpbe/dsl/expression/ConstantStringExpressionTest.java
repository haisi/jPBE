/*
 * (c) Copyright 2021 Hasan Selman Kara. All rights reserved.
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
 */
package li.selman.jpbe.dsl.expression;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author Hasan Selman Kara
 */
class ConstantStringExpressionTest {

    @Test
    @DisplayName("Returns constant")
    void returnsConstant() {
        String constant = "SOME CONSTANT STRING";
        var constStrExp = new ConstantStringExpression(constant);

        assertAll(
                () -> assertThat(constStrExp.apply("any string")).isNotEmpty().containsSame(constant),
                () -> assertThat(constStrExp.apply("")).isNotEmpty().containsSame(constant),
                () -> assertThat(constStrExp.apply(null)).isNotEmpty().containsSame(constant)
        );
    }

    @Nested
    @DisplayName("Constant cannot be blank")
    class ConstantCantBeBlank {

        @Test
        @DisplayName("null")
        void nullValue() {
            titleMustNotBeBlank(null);
        }

        @Test
        @DisplayName("blank")
        void blankValue() {
            titleMustNotBeBlank("");
        }

        void titleMustNotBeBlank(String value) {
            assertThat(assertThrows(IllegalArgumentException.class, () -> {
                new ConstantStringExpression(value);
            })).isNotNull();
        }

    }
}
