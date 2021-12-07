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

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import li.selman.jpbe.dsl.position.ConstantPosition;
import li.selman.jpbe.dsl.position.DynamicPosition;
import li.selman.jpbe.dsl.position.Position;
import li.selman.jpbe.dsl.token.Token;
import li.selman.jpbe.dsl.token.TokenSequence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Hasan Selman Kara
 */
class ExpressionsTest {

    @Test
    @DisplayName("Applying the expressions in the correct order")
    void inCorrectOrder() {
        String expected = "ABC";
        var expression = new Expressions(List.of(
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
        var expression = new Expressions(List.of(
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

            Expressions expression =
                    new Expressions(List.of(new SubstringExpression(beforeNumber, endOfString)));

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

            Expressions expression = new Expressions(List.of(new SubstringExpression(left, slash)));

            return expression.apply(input);
        };

        assertThat(applier.apply("Company\\Code\\index.html").get()).isEqualTo("Company\\Code\\");
        assertThat(applier.apply("Company\\Docs\\Spec\\sepcs.doc").get()).isEqualTo("Company\\Docs\\Spec\\");
    }

}
