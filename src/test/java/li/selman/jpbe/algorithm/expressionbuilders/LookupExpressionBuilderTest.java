/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.algorithm.expressionbuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import li.selman.jpbe.dsl.Expression;
import li.selman.jpbe.dsl.lookup.LookupExpressionBuilder;
import org.junit.jupiter.api.Test;

/**
 * @author Hasan Selman Kara
 */
class LookupExpressionBuilderTest {

    LookupExpressionBuilder builder = new LookupExpressionBuilder(List.of(
        List.of("une", "deux", "trois"),
        List.of("eins", "zwei", "drei"),
        List.of("one", "two", "three")
    ));

    @Test
    void illegalInitTest() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new LookupExpressionBuilder(List.of()));
        assertThat(exception).isNotNull();

        exception = assertThrows(IllegalArgumentException.class, () -> {
            new LookupExpressionBuilder(List.of(List.of(), List.of()));
        });
        assertThat(exception).isNotNull();

        exception = assertThrows(IllegalArgumentException.class, () -> {
            new LookupExpressionBuilder(List.of(
                List.of("une", "deux", "trois"),
                List.of("eins", "zwei", "drei"),
                List.of("one", "two")
            ));
        });
        assertThat(exception).isNotNull();
    }

    @Test
    void sameColumnTest() {
        List<Expression> expression = builder.computeExpressions("eins zwei oder drei", "drei");
        assertThat(expression).isEmpty();
    }

    @Test
    void foo() {
        List<Expression> expression = builder.computeExpressions("eins zwei oder drei", "trois");
        assertThat(expression).hasSize(1);

        Optional<String> actual = expression.get(0).apply("drei");
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo("trois");

        actual = expression.get(0).apply("troisssig");
        assertThat(actual).isEmpty();

        actual = expression.get(0).apply("eins zwei oder drei");
        assertThat(actual).isNotEmpty();
        assertThat(actual.get()).isEqualTo("une deux oder trois");
    }
}
