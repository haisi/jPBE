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

class PositionsBuilderTest {

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Test
    @Disabled
    void fooTest() {
        // given
        String input = "AB CDE";
        int expectedPosition = 3;

        // Token-Sequences S: Space, A: Alphanumeric, s: Start, e: End
        TokenSequence S = TokenSequence.of(SPACE);
        TokenSequence A = TokenSequence.of(ALPHA);
        TokenSequence e = TokenSequence.of(END);
        TokenSequence Ae = TokenSequence.of(ALPHA, END);
        TokenSequence AS = TokenSequence.of(ALPHA, SPACE);
        TokenSequence s = TokenSequence.of(START);
        TokenSequence sA = TokenSequence.of(START, ALPHA);
        TokenSequence sAS = TokenSequence.of(START, ALPHA, SPACE);
        TokenSequence sASA = TokenSequence.of(START, ALPHA, SPACE, ALPHA);
        TokenSequence sASAe = TokenSequence.of(START, ALPHA, SPACE, ALPHA, END);
        TokenSequence ASAe = TokenSequence.of(ALPHA, SPACE, ALPHA, END);
        TokenSequence SAe = TokenSequence.of(SPACE, ALPHA, END);

        Tokens supportedTokens = new Tokens(List.of(START, END, ALPHA, SPACE));
        int maxTokenSeqLength = 999;
        var positionBuilder = PositionsBuilder.of(
            new ConstantPositionBuilder(),
            new DynamicPositionBuilder(new TokenSequenceBuilder(maxTokenSeqLength, supportedTokens)) // Buggy
        );

        // when
        Set<Position> actual = positionBuilder.computePositions(input, expectedPosition);

        // then
        // s: "AB CDE", k: 3
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

        // TODO(#bug): PositionBuilder generates positions which do not eval to any position on the same input!
        actual.forEach((pos) -> {
            int actualPosition = pos.evalToPosition(input);
            assertThat(actualPosition).isEqualTo(expectedPosition);
        });
//        assertThat(actual).containsAll(expected);
//        assertThat(actual).containsExactlyElementsOf(expected);
    }

}
