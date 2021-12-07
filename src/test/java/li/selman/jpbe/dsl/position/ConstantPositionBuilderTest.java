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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import org.junit.jupiter.api.Test;

class ConstantPositionBuilderTest {

    @Test
    void negativeTest() {
        // given
        ConstantPositionBuilder builder = new ConstantPositionBuilder();
        int negativeK = -3;

        assertThatThrownBy(
                // when
                () -> {
                    builder.computePositions("AB C", negativeK);
                })
                // then
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void outOfBoundsKTest() {
        // given
        ConstantPositionBuilder builder = new ConstantPositionBuilder();

        assertThatThrownBy(
                // when
                () -> {
                    builder.computePositions("123", 4);
                })
                // then
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void positiveTest() {
        // given
        ConstantPositionBuilder builder = new ConstantPositionBuilder();

        // when
        Set<Position> actual = builder.computePositions("ABC E", 3);

        // then
        assertThat(actual).containsExactlyInAnyOrder(
                new ConstantPosition(3),
                new ConstantPosition(-2)
        );
    }

    @Test
    void lastPositionTest() {
        // given
        ConstantPositionBuilder builder = new ConstantPositionBuilder();

        // when
        Set<Position> actual = builder.computePositions("1234567", 7);

        // then
        assertThat(actual).containsExactlyInAnyOrder(
                ConstantPosition.lastPosition(),
                new ConstantPosition(7)
        );
    }

    @Test
    void firstPositionTest() {
        // given
        ConstantPositionBuilder builder = new ConstantPositionBuilder();

        // when
        Set<Position> actual = builder.computePositions("1234567", 0);

        // then
        assertThat(actual).containsExactlyInAnyOrder(
                new ConstantPosition(-7),
                new ConstantPosition(0)
        );
    }

}
