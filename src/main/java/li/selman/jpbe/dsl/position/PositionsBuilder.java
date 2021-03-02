/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
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
