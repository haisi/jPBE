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

import static li.selman.jpbe.dsl.position.Matcher.positionOfRegex;
import static li.selman.jpbe.dsl.position.Matcher.totalNumberOfMatches;
import static li.selman.jpbe.dsl.token.Token.ALPHA;
import static li.selman.jpbe.dsl.token.Token.ALPHA_NUM;
import static li.selman.jpbe.dsl.token.Token.END;
import static li.selman.jpbe.dsl.token.Token.SPACE;
import static li.selman.jpbe.dsl.token.Token.START;
import static li.selman.jpbe.dsl.token.Token.UNDERSCORE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import li.selman.jpbe.dsl.token.TokenSequence;
import org.junit.jupiter.api.Test;

class MatcherTest {

    @Test
    public void positionOfRegexWithEndOrStartTokenTest() {
        assertThat(positionOfRegex(TokenSequence.of(SPACE, END), "_ ", 1, 2)).isEqualTo(1);
        assertThat(positionOfRegex(TokenSequence.of(START, ALPHA_NUM, SPACE, END), "A ", 0, 2)).isEqualTo(1);
        assertThat(positionOfRegex(TokenSequence.of(SPACE, UNDERSCORE, END), "AB  _B _", 6, 8)).isEqualTo(1);
    }

    @Test
    public void positionOfRegexTest() {
        assertThat(positionOfRegex(TokenSequence.of(SPACE, UNDERSCORE), "AB  _B _", 2, 5)).isEqualTo(1);
        assertThat(positionOfRegex(TokenSequence.of(SPACE, UNDERSCORE), "AB  _B _", 3, 5)).isEqualTo(1);
        assertThat(positionOfRegex(TokenSequence.of(SPACE, UNDERSCORE), "AB  _B _", 6, 8)).isEqualTo(2);
    }

    @Test
    public void positionOfRegexWithNoMatchTest() {
        assertThatThrownBy(() -> positionOfRegex(TokenSequence.of(SPACE), "ABCDED", 1, 3))
            .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> positionOfRegex(TokenSequence.of(UNDERSCORE), "_ABCDED", 0, 2))
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void totalNumberOfMatchesWithSingleTokenTest() {
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE), "A B C")).isEqualTo(2);
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE), "A  B C")).isEqualTo(2);
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE), "ABC")).isEqualTo(0);
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE), "ABC   A")).isEqualTo(1);
    }

    @Test
    public void totalNumberOfMatchesStartAndOrEndTokensTest() {
        // If you use START or END token, you can only match 0 or 1 times
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE, UNDERSCORE), "A B_C")).isEqualTo(0);
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE, UNDERSCORE, END), "A B_C")).isEqualTo(0);
        assertThat(totalNumberOfMatches(TokenSequence.of(START, SPACE, UNDERSCORE), "A B_C")).isEqualTo(0);
        assertThat(totalNumberOfMatches(TokenSequence.of(START, SPACE, UNDERSCORE, END), "A B_C")).isEqualTo(0);

        assertThat(totalNumberOfMatches(TokenSequence.of(START), "ABC    ___")).isEqualTo(1);
        assertThat(totalNumberOfMatches(TokenSequence.of(END), "ABC    ___")).isEqualTo(1);
        assertThat(totalNumberOfMatches(TokenSequence.of(START, END), "")).isEqualTo(1);

        assertThat(totalNumberOfMatches(TokenSequence.of(UNDERSCORE, END), "ABC    ___")).isEqualTo(1);
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE, UNDERSCORE, END), "ABC    ___")).isEqualTo(1);

        assertThat(totalNumberOfMatches(TokenSequence.of(START, ALPHA, SPACE), "ABC    ___")).isEqualTo(1);
        assertThat(totalNumberOfMatches(TokenSequence.of(START, ALPHA, SPACE, UNDERSCORE), "ABC    ___")).isEqualTo(1);
        assertThat(totalNumberOfMatches(TokenSequence.of(START, ALPHA, SPACE, UNDERSCORE, END), "ABC    ___"))
            .isEqualTo(1);
    }

    @Test
    public void totalNumberOfMatchesWithTwoTokenTest() {
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE, UNDERSCORE), "A B_C")).isEqualTo(0);
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE, UNDERSCORE), "A  B C")).isEqualTo(0);
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE, UNDERSCORE), "ABC")).isEqualTo(0);
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE, UNDERSCORE), "ABC_")).isEqualTo(0);
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE, UNDERSCORE), "ABC_")).isEqualTo(0);
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE, UNDERSCORE), "ABC    ___")).isEqualTo(1);
        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE, UNDERSCORE), "ABC    ___B _")).isEqualTo(2);

        assertThat(totalNumberOfMatches(TokenSequence.of(SPACE, UNDERSCORE, END), "ABC    ___B _")).isEqualTo(1);
        assertThat(totalNumberOfMatches(TokenSequence.of(START, SPACE, UNDERSCORE), "    ___B _ABC")).isEqualTo(1);
    }

    @Test
    public void outOfRangeGetPositionOfRegex() {
        assertThatThrownBy(() -> positionOfRegex(TokenSequence.of(), "ABCDED", 2, 1))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> positionOfRegex(TokenSequence.of(), "ABCDED", 1, 300))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> positionOfRegex(TokenSequence.of(), "ABCDED", 200, 300))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
