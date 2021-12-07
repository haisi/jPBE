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

package li.selman.jpbe.dsl.position;

import static li.selman.jpbe.dsl.token.Token.ALPHA;
import static li.selman.jpbe.dsl.token.Token.COMMA;
import static li.selman.jpbe.dsl.token.Token.END;
import static li.selman.jpbe.dsl.token.Token.SPACE;
import static li.selman.jpbe.dsl.token.Token.START;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.List;
import java.util.Map;
import java.util.Set;
import li.selman.jpbe.dsl.token.TokenSequence;
import li.selman.jpbe.dsl.token.TokenSequenceBuilder;
import li.selman.jpbe.dsl.token.Tokens;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DynamicPositionBuilderTest {

    /**
     * Given a string {@code input} and an index {@code k} on said input,
     * a {@link DynamicPositionBuilder} will compute a set {@code P} of positions.
     * <p>
     * Then for all {@code pos} in {@code P} | {@code pos(input) == k}
     *
     * @param input the input string used to compute the positions for
     * @param k     the index to compote position DSL elements to and to evaluate to
     */
    @ParameterizedTest
    @CsvSource(value = {
            "Simple Space:7",
            "Multiple Space In One:15",
//        "Multiple Space In One More:15", // No matches!
            "Start:0",
            "End:3",
            "Com,ma:3",
            "Com,ma:4",
            "D:1"
    }, delimiter = ':')
    void sanityTest(String input, int k) {
        // given
        Tokens supportedTokens = new Tokens(List.of(START, END, ALPHA, COMMA, SPACE));
        int maxTokenSeqLength = 999;
        var posBuilder = new DynamicPositionBuilder(new TokenSequenceBuilder(maxTokenSeqLength, supportedTokens));

        // when
        Set<Position> computePositions = posBuilder.computePositions(input, k);

        // then
        assertThat(computePositions).allMatch(position -> position.evalToPosition(input) == k);
    }

    @Test
    void computeLeftAndRightTokenSeqTest() {
        // given
        Tokens supportedTokens = new Tokens(List.of(START, END, ALPHA, SPACE));
        int maxTokenSeqLength = 999;
        var posBuilder = new DynamicPositionBuilder(new TokenSequenceBuilder(maxTokenSeqLength, supportedTokens));

        // when
        String input = "AB CDE";
        int k = 3;
        Map<Integer, TokenSequence> left = posBuilder.computeLeftTokenSeq(input, k);
        Map<Integer, TokenSequence> right = posBuilder.computeRightTokenSeq(input, k);

        // then
        assertThat(left).containsOnly(
                entry(0, TokenSequence.of(START, ALPHA, SPACE)),
                entry(1, TokenSequence.of(ALPHA, SPACE)),
                entry(2, TokenSequence.of(SPACE))
        );
        assertThat(right).containsOnly(
                entry(4, TokenSequence.of(ALPHA)),
                entry(5, TokenSequence.of(ALPHA)),
                entry(6, TokenSequence.of(ALPHA, END))
        );
    }

}
