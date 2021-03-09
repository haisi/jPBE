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

import java.util.Set;

public class ConstantPositionBuilder implements PositionBuilder {

    @Override
    public Set<Position> computePositions(String input, int k) {
        if (k < 0) throw new IllegalArgumentException("k cannot be < 0");
        if (k > input.length()) throw new IllegalArgumentException("k cannot be > input.length()");

        ConstantPosition positionFromTheLeft = new ConstantPosition(k);
        ConstantPosition positionFromTheRight;
        if (input.length() == k) {
            positionFromTheRight = new ConstantPosition(Integer.MIN_VALUE);
        } else {
            positionFromTheRight = new ConstantPosition(-(input.length() - k));
        }

        return Set.of(
            positionFromTheLeft,
            positionFromTheRight
        );
    }

}
