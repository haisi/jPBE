package li.selman.jpbe.dsl.ConstStr;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
            () -> assertThat(constStrExp.apply("any string")).isNotEmpty().contains(constant),
            () -> assertThat(constStrExp.apply("")).isNotEmpty().contains(constant),
            () -> assertThat(constStrExp.apply(null)).isNotEmpty().contains(constant)
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
