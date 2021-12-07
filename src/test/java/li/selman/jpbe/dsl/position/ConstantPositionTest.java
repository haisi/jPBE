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
package li.selman.jpbe.dsl.position;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author Hasan Selman Kara
 */
class ConstantPositionTest {

    @Nested
    @DisplayName("Positive values")
    class PositiveValues {

        @Test
        @DisplayName("Indices in bounds")
        void positiveValues() {
            String input = "ABCDE";
            int wholeString = input.length();

            assertAll(
                    () -> assertThat(actualOutput(input, 0)).isEqualTo(""),
                    () -> assertThat(actualOutput(input, 1)).isEqualTo("A"),
                    () -> assertThat(actualOutput(input, 2)).isEqualTo("AB"),
                    () -> assertThat(actualOutput(input, 3)).isEqualTo("ABC"),
                    () -> assertThat(actualOutput(input, wholeString)).isEqualTo(input)
            );
        }

        String actualOutput(String input, int constant) {
            var cpos = new ConstantPosition(constant);
            int actualPos = cpos.evalToPosition(input);
            return input.substring(0, actualPos);
        }

    }

    @Nested
    @DisplayName("Negative values")
    class NegativeValues {

        @Test
        @DisplayName("Last element from the right, i.e. Integer.MIN_VALUE")
        void lastElementFromTheRight() {
            int constant = Integer.MIN_VALUE;

            assertAll(
                    () -> assertThat(actualOutput("Some Text", constant)).isEqualTo(""),
                    () -> assertThat(actualOutput("", constant)).isEqualTo("")
            );
        }

        @Test
        @DisplayName("Indices in bounds")
        void negativeValues() {
            String input = "ABCDE";
            int wholeString = input.length() * (-1);

            assertAll(
                    () -> assertThat(actualOutput(input, -1)).isEqualTo("E"),
                    () -> assertThat(actualOutput(input, -2)).isEqualTo("DE"),
                    () -> assertThat(actualOutput(input, -3)).isEqualTo("CDE"),
                    () -> assertThat(actualOutput(input, wholeString)).isEqualTo(input)
            );
        }

        @Test
        @DisplayName("Values out of bounds")
        void negativeValuesOutOfBounds() {
            String input = "ABCDE";
            int bigNumber = 100;

            var exception = assertThrows(StringIndexOutOfBoundsException.class,
                    () -> actualOutput(input, input.length() * (-1) - bigNumber));
            assertThat(exception).isNotNull();
        }

        String actualOutput(String input, int constant) {
            var cpos = new ConstantPosition(constant);
            int actualPos = cpos.evalToPosition(input);
            return input.substring(actualPos);
        }

    }

}
