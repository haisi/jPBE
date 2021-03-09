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

import java.util.List;
import li.selman.jpbe.dsl.token.Token;
import li.selman.jpbe.dsl.token.TokenSequence;
import org.junit.jupiter.api.Test;

class ConjunctTest {

    @Test
    void normalTest() {
        List<Predicate> predicates = List.of(
            Match.positive(TokenSequence.of(Token.START), 1),
            Match.positive(TokenSequence.of(Token.SPACE), 1)
        );

        Conjunct conjunct = new Conjunct(predicates);
        assertThat(conjunct.matches("Hans Peter")).isTrue();
    }

    @Test
    void notAllMatchTest() {
        List<Predicate> predicates = List.of(
            Match.positive(TokenSequence.of(Token.START), 1), // This one matches
            Match.positive(TokenSequence.of(Token.DOT), 1) // This one doesn't match
        );

        Conjunct conjunct = new Conjunct(predicates);
        assertThat(conjunct.matches("Hans Peter")).isFalse();
    }

    @Test
    void notEnoughMatchesTest() {
        List<Predicate> predicates = List.of(
            Match.positive(TokenSequence.of(Token.START), 1),
            Match.positive(TokenSequence.of(Token.SPACE), 999) // Doesn't match the specified number of times
        );

        Conjunct conjunct = new Conjunct(predicates);
        assertThat(conjunct.matches("Hans Peter")).isFalse();
    }

    // TODO(#testing): Add more tests with Match.negative and multiple tokens

    @Test
    void toStringTest() {
        List<Predicate> predicates = List.of(
            Match.positive(TokenSequence.of(Token.START), 1),
            Match.negative(TokenSequence.of(Token.SPACE, Token.ALPHA_NUM), 99),
            new TruePredicate()
        );

        Conjunct conjunct = new Conjunct(predicates);
        assertThat(conjunct.toString()).isEqualTo("((P, 1, {^}) v (N, 99, {s+, AN}) v TRUE)");
    }

}
