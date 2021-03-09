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

public interface PositionBuilder {

    /**
     * Generates all possible position which match a given index on a given string.
     * Note that if the passed parameters are valid, then
     * <b>all computed positions must evaluate to {@code k} on {@code input}.</b>
     *
     * @param input Input string
     * @param k     index of position to generate to
     * @return set of positions
     * @throws IllegalArgumentException if {@code k} &lt; 0
     * @throws IllegalArgumentException if {@code k} &gt; {@code input.length()}
     */
    Set<Position> computePositions(String input, int k);

}
