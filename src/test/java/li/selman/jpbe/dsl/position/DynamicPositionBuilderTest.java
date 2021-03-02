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
import li.selman.jpbe.dsl.token.TokenSequenceBuilder;
import li.selman.jpbe.dsl.token.Tokens;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DynamicPositionBuilderTest {

    @ParameterizedTest
    @CsvSource(value = {
        "Simple Space:7", "B:2", "D:1"
    }, delimiter = ':')
    void sanityTest(String input, int k) {
        // given
        Tokens supportedTokens = new Tokens(List.of(START, END, ALPHA, SPACE));
        int maxTokenSeqLength = 999;
        var posBuilder = new DynamicPositionBuilder(new TokenSequenceBuilder(maxTokenSeqLength, supportedTokens));

        // when
        Set<Position> computePositions = posBuilder.computePositions(input, k);

        // then
        assertThat(computePositions).allMatch(position -> position.evalToPosition(input) == k);
    }

}
