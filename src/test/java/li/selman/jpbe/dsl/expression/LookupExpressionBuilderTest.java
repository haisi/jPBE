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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import li.selman.jpbe.dsl.Expression;
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
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> new LookupExpressionBuilder(List.of()));
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
