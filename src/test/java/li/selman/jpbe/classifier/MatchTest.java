/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
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
