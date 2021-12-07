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
import static li.selman.jpbe.dsl.token.Token.ALPHA_NUM;
import static li.selman.jpbe.dsl.token.Token.ALPHA_NUM_NO_LEADING_ZEROS;
import static li.selman.jpbe.dsl.token.Token.DOT;
import static li.selman.jpbe.dsl.token.Token.LEADING_ZERO;
import static li.selman.jpbe.dsl.token.Token.SPACE;
import static li.selman.jpbe.dsl.token.Token.START;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import li.selman.jpbe.dsl.token.TokenSequence;
import org.junit.jupiter.api.Test;

/**
 * @author Hasan Selman Kara
 */
class DynamicPositionTest {

    @Test
    void startTokenTest() {
        Position p;
        String s;
        int expectedIdx;

        p = new DynamicPosition(TokenSequence.of(START), TokenSequence.of(ALPHA), 1);
        s = "anystring";
        expectedIdx = 0;

        assertThat(p.evalToPosition(s)).isEqualTo(expectedIdx);
    }

    @Test
    void leadingZeroPositionTest() {
        Position start = new DynamicPosition(
                TokenSequence.of(START, LEADING_ZERO), TokenSequence.of(ALPHA_NUM_NO_LEADING_ZEROS), 1);
        Position end = new DynamicPosition(
                TokenSequence.of(ALPHA_NUM_NO_LEADING_ZEROS), TokenSequence.of(DOT, ALPHA_NUM), 1);

        assertThat(start.evalToPosition("0087145.B.04")).isEqualTo(2);
        assertThat(start.evalToPosition("0087335.C.02")).isEqualTo(2);
        assertThat(start.evalToPosition("0301375.0.00")).isEqualTo(1);
        assertThat(start.evalToPosition("0305265.0.00")).isEqualTo(1);

        assertThatThrownBy(() -> start.evalToPosition("Somejunk 0305265.0.00"))
                .isInstanceOf(NoPositionException.class);

        assertThat(end.evalToPosition("0087145.B.04")).isEqualTo(7);
        assertThat(end.evalToPosition("0087335.C.02")).isEqualTo(7);
        assertThat(end.evalToPosition("0301375.0.00")).isEqualTo(7);
        assertThat(end.evalToPosition("0305265.0.00")).isEqualTo(7);
    }

    @Test
    void secondOccurrenceFromLeftTest() {
        Position p;
        String s;
        int expectedIdx;

        p = new DynamicPosition(TokenSequence.of(ALPHA), TokenSequence.of(SPACE), -2);
        s = "AAA BBB  BBB";
        expectedIdx = 3;

        assertThat(p.evalToPosition(s)).isEqualTo(expectedIdx);
    }
}
