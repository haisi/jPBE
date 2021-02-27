/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.conststr;

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
        String constant  = "SOME CONSTANT STRING";
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
