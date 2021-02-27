/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.position;

import static li.selman.jpbe.dsl.token.Token.ALPHA;
import static li.selman.jpbe.dsl.token.Token.SPACE;
import static li.selman.jpbe.dsl.token.Token.START;
import static org.assertj.core.api.Assertions.assertThat;

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
        int actualIdx;

        p = new DynamicPosition(TokenSequence.of(START), TokenSequence.of(ALPHA), 1);
        s = "anystring";
        expectedIdx = 0;

        assertThat(p.evalToPosition(s)).isEqualTo(expectedIdx);
    }

    @Test
    void secondOccurrenceFromLeftTest() {
        Position p;
        String s;
        int expectedIdx;
        int actualIdx;

        p = new DynamicPosition(TokenSequence.of(ALPHA), TokenSequence.of(SPACE), -2);
        s = "AAA BBB  BBB";
        expectedIdx = 3;

        assertThat(p.evalToPosition(s)).isEqualTo(expectedIdx);
    }
}
