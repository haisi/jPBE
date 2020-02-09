package li.selman.jpbe.datastructure;

import li.selman.jpbe.dsl.ConstStr.ConstantStringExpression;
import li.selman.jpbe.dsl.expression.SubstringExpression;
import li.selman.jpbe.dsl.position.ConstantPosition;
import li.selman.jpbe.dsl.position.DynamicPosition;
import li.selman.jpbe.dsl.position.Position;
import li.selman.jpbe.dsl.token.Token;
import li.selman.jpbe.dsl.token.TokenSequence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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

    @Test
    @DisplayName("Empty TokenSeq from left")
    void emptyTokenSeqLeft() {
        Function<String, Optional<String>> applier = input -> {
            Position beforeNumber = new DynamicPosition(TokenSequence.of(), TokenSequence.of(Token.NUM), 1);
            Position endOfString = ConstantPosition.lastPosition();

            TraceExpression expression = new TraceExpression(List.of(new SubstringExpression(beforeNumber, endOfString)));

            return expression.apply(input);
        };

        assertThat(applier.apply("BTR KRNL WK CORN 15Z").get()).isEqualTo("15Z");
        assertThat(applier.apply("CAMP DRY DBL NDL 3.6 OZ").get()).isEqualTo("3.6 OZ");
        assertThat(applier.apply("CHROE BOY HD SC SPNG 1 PK").get()).isEqualTo("1 PK");
        assertThat(applier.apply("French Worcester - asdf 5 Z").get()).isEqualTo("5 Z");
    }

    @Test
    @DisplayName("Empty TokenSeq from right")
    void emptyTokenSeqRight() {
        Function<String, Optional<String>> applier = input -> {
            Position left = new ConstantPosition(0);
            Position slash = new DynamicPosition(TokenSequence.of(Token.BACK_SLASH), TokenSequence.of(), -1);

            TraceExpression expression = new TraceExpression(List.of(new SubstringExpression(left, slash)));

            return expression.apply(input);
        };

        assertThat(applier.apply("Company\\Code\\index.html").get()).isEqualTo("Company\\Code\\");
        assertThat(applier.apply("Company\\Docs\\Spec\\sepcs.doc").get()).isEqualTo("Company\\Docs\\Spec\\");
    }

}
