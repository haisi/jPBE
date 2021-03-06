/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */

package li.selman.jpbe.dsl.position;

import static li.selman.jpbe.dsl.token.Token.ALPHA;
import static li.selman.jpbe.dsl.token.Token.END;
import static li.selman.jpbe.dsl.token.Token.SPACE;
import static li.selman.jpbe.dsl.token.Token.START;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import li.selman.jpbe.dsl.token.TokenSequence;
import li.selman.jpbe.dsl.token.TokenSequenceBuilder;
import li.selman.jpbe.dsl.token.Tokens;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
class PositionsBuilderTest {

    Tokens supportedTokens = new Tokens(List.of(START, END, ALPHA, SPACE));
    int maxTokenSeqLength = 999;
    PositionsBuilder positionBuilder = PositionsBuilder.of(
        new ConstantPositionBuilder(),
        new DynamicPositionBuilder(new TokenSequenceBuilder(maxTokenSeqLength, supportedTokens))
    );

    // Token-Sequences S: Space, A: Alphanumeric, s: Start, e: End
    private final TokenSequence S = TokenSequence.of(SPACE);
    private final TokenSequence A = TokenSequence.of(ALPHA);
    private final TokenSequence e = TokenSequence.of(END);
    private final TokenSequence Ae = TokenSequence.of(ALPHA, END);
    private final TokenSequence AS = TokenSequence.of(ALPHA, SPACE);
    private final TokenSequence s = TokenSequence.of(START);
    private final TokenSequence sA = TokenSequence.of(START, ALPHA);
    private final TokenSequence sAS = TokenSequence.of(START, ALPHA, SPACE);
    private final TokenSequence sASA = TokenSequence.of(START, ALPHA, SPACE, ALPHA);
    private final TokenSequence sASAe = TokenSequence.of(START, ALPHA, SPACE, ALPHA, END);
    private final TokenSequence ASAe = TokenSequence.of(ALPHA, SPACE, ALPHA, END);
    private final TokenSequence SAe = TokenSequence.of(SPACE, ALPHA, END);

    @Test
    void simple1Test() {
        // given
        String input = "AB CDE";
        int expectedPosition = 3;

        Set<Position> expected = Set.of(
            new ConstantPosition(3),
            new ConstantPosition(-3),
            new DynamicPosition(sAS, A, 1),
            new DynamicPosition(sAS, A, -1),
            new DynamicPosition(AS, A, 1),
            new DynamicPosition(AS, A, -1),
            new DynamicPosition(S, A, 1),
            new DynamicPosition(S, A, -1),
            new DynamicPosition(sAS, Ae, 1),
            new DynamicPosition(sAS, Ae, -1),
            new DynamicPosition(AS, Ae, 1),
            new DynamicPosition(AS, Ae, -1),
            new DynamicPosition(S, Ae, 1),
            new DynamicPosition(S, Ae, -1)
        );

        executeAndAssert(input, expectedPosition, expected);
    }

    @Test
    void simple2Test() {
        // given
        String input = "AB CDE";
        int expectedPosition = 2;

        Set<Position> expected = Set.of(
            new ConstantPosition(2),
            new ConstantPosition(-4),
            new DynamicPosition(sA, SAe, 1),
            new DynamicPosition(sA, SAe, -1),
            new DynamicPosition(A, SAe, 1),
            new DynamicPosition(A, SAe, -1)
        );

        executeAndAssert(input, expectedPosition, expected);
    }

    @Test
    @Disabled
    void lastPositionTest() {
        // given
        String input = "AB CDE";
        int expectedPosition = 6;

        Set<Position> expected = Set.of(
            new ConstantPosition(6),
            ConstantPosition.lastPosition(),
            new DynamicPosition(sASAe, e, 1),
            new DynamicPosition(sASAe, e, -1),
            new DynamicPosition(ASAe, e, 1),
            new DynamicPosition(ASAe, e, -1),
            new DynamicPosition(SAe, e, 1),
            new DynamicPosition(SAe, e, -1),
            new DynamicPosition(Ae, e, 1),
            new DynamicPosition(Ae, e, -1)
        );

        executeAndAssert(input, expectedPosition, expected);
    }

    @Test
    @Disabled
    void firstPositionTest() {
        // given
        String input = "AB CDE";
        int expectedPosition = 0;

        Set<Position> expected = Set.of(
            new ConstantPosition(0),
            new ConstantPosition(-6),
            new DynamicPosition(s, sASAe, 1),
            new DynamicPosition(s, sASAe, -1),
            new DynamicPosition(s, sASA, 1),
            new DynamicPosition(s, sASA, -1),
            new DynamicPosition(s, sAS, 1),
            new DynamicPosition(s, sAS, -1),
            new DynamicPosition(s, sA, 1),
            new DynamicPosition(s, sA, -1)
        );

        executeAndAssert(input, expectedPosition, expected);
    }

    private void executeAndAssert(String input, int k, Set<Position> expected) {
        // when
        Set<Position> actual = positionBuilder.computePositions(input, k);

        // then
        assertThat(actual).allMatch(position -> position.evalToPosition(input) == k);
        assertThat(actual).hasSizeGreaterThan(2);
        assertThat(actual).containsAll(expected);
    }

}
