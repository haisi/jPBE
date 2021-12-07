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

import java.util.Optional;
import li.selman.jpbe.dsl.Expression;
import li.selman.jpbe.dsl.position.ConstantPosition;
import li.selman.jpbe.dsl.position.DynamicPosition;
import li.selman.jpbe.dsl.position.Position;
import li.selman.jpbe.dsl.token.Token;
import li.selman.jpbe.dsl.token.TokenSequence;
import org.junit.jupiter.api.Test;

/**
 * @author Hasan Selman Kara
 */
class SubstringExpressionTest {

    @Test
    void leadingZeroTest() {
        Position start = new DynamicPosition(
                TokenSequence.of(Token.START, Token.LEADING_ZERO), TokenSequence.of(Token.ALPHA_NUM_NO_LEADING_ZEROS), 1);
        Position end = new DynamicPosition(
                TokenSequence.of(Token.ALPHA_NUM_NO_LEADING_ZEROS), TokenSequence.of(Token.DOT, Token.ALPHA_NUM), 1);

        Expression expression = new SubstringExpression(start, end);
        assertThat(expression.apply("0370835.A.00")).contains("370835");
        assertThat(expression.apply("0087145.B.04")).contains("87145");
    }

    @Test
    void getWholeString() {
        String expected = "12345";
        String input = "12345";

        ConstantPosition endPosition = ConstantPosition.lastPosition();

        SubstringExpression subStr = new SubstringExpression(new ConstantPosition(0), endPosition);
        Optional<String> actual = subStr.apply(input);

        assertThat(actual).isNotEmpty().contains(expected);
    }

    @Test
    void constantSubstringTest() {
        // given
        String s = "12345";
        Expression substringExpression = new SubstringExpression(new ConstantPosition(0), new ConstantPosition(-2));

        // when
        Optional<String> actual = substringExpression.apply(s);

        // then
        String expected = "123";
        assertThat(actual).isNotEmpty().contains(expected);
    }

}
