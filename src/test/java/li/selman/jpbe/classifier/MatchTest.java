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

package li.selman.jpbe.classifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import li.selman.jpbe.dsl.token.Token;
import li.selman.jpbe.dsl.token.TokenSequence;
import org.junit.jupiter.api.Test;

class MatchTest {

    @Test
    void normalMatchTest() {
        Predicate p1 = Match.positive(TokenSequence.of(Token.ALPHA), 1);
        assertThat(p1.matches("Hallo Welt")).isTrue();
    }

    @Test
    void tooFewMatchesTest() {
        Predicate p1 = Match.positive(TokenSequence.of(Token.ALPHA), 99);
        assertThat(p1.matches("Hallo Welt")).isFalse();
    }

    @Test
    void negativeNumberOfMatchesIllegalTest() {
        assertThatThrownBy(() -> Match.positive(TokenSequence.of(Token.ALPHA), -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void emptyTokenSequenceIllegalTest() {
        assertThatThrownBy(() -> Match.positive(TokenSequence.of(), 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void toStringTest() {
        assertThat(Match.positive(TokenSequence.of(Token.SPACE), 1).toString()).isEqualTo("(P, 1, {s+})");
        assertThat(Match.negative(TokenSequence.of(Token.SPACE, Token.DOT, Token.ALPHA_NUM), 3).toString())
                .isEqualTo("(N, 3, {s+, ., AN})");
    }

}
