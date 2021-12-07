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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Wraps and applies multiple PositionBuilders.
 *
 * @author Hasan Selman Kara
 */
public class PositionsBuilder implements PositionBuilder {

    private final List<PositionBuilder> positionBuilders;

    private String lastString;
    private final Map<Integer, Set<Position>> cache = new HashMap<>();

    private PositionsBuilder(List<PositionBuilder> positionBuilders) {
        this.positionBuilders = positionBuilders;
    }

    public static PositionsBuilder of(List<PositionBuilder> positionBuilders) {
        return new PositionsBuilder(positionBuilders);
    }

    public static PositionsBuilder of(PositionBuilder... positionBuilders) {
        return new PositionsBuilder(Arrays.asList(positionBuilders));
    }

    @Override
    public Set<Position> computePositions(String input, int k) {
        if (k < 0) throw new IllegalArgumentException("k cannot be < 0");
        if (k > input.length()) throw new IllegalArgumentException("k cannot be > input.length()");

        if (input.equals(lastString)) {
            cache.clear();
            lastString = input;
        } else if (cache.containsKey(k)) {
            return cache.get(k);
        }

        // Compute positions given all configured PositionBuilders
        Set<Position> unionOfFoundPositions = positionBuilders.stream()
                .map(positionBuilder -> positionBuilder.computePositions(input, k))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        cache.put(k, unionOfFoundPositions);
        return unionOfFoundPositions;
    }

}
