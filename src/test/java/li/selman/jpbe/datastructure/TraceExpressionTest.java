package li.selman.jpbe.datastructure;

import li.selman.jpbe.dsl.ConstStr.ConstantStringExpression;
import li.selman.jpbe.dsl.expression.SubstringExpression;
import li.selman.jpbe.dsl.position.ConstantPosition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hasan Selman Kara
 */
class TraceExpressionTest {

    @Test
    @DisplayName("Applying the expressions in the correct order")
    void inCorrectOrder() {
        String expected = "ABC";
        var expression = new TraceExpression(List.of(
            new ConstantStringExpression("A"),
            new ConstantStringExpression("B"),
            new ConstantStringExpression("C")
        )
        );

        Optional<String> actual = expression.apply("Some string");
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Failing to apply expression")
    void failingToApplyExpression() {
        String expected = "ABC";
        int hugeEndIndexForSubstring = 100;
        var expression = new TraceExpression(List.of(
            new ConstantStringExpression("A"),
            new SubstringExpression(new ConstantPosition(0), new ConstantPosition(hugeEndIndexForSubstring))
        )
        );

        Optional<String> actual = expression.apply("Some string");
        assertThat(actual).isEmpty();
    }

}
