/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.position;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import li.selman.jpbe.dsl.token.TokenSequenceBuilder;

/**
 * Wraps and applies multiple PositionBuilders.
 * @author Hasan Selman Kara
 */
public class PositionsBuilder {

    private final ConstantPositionBuilder constantPositionBuilder;
    private final DynamicPositionBuilder dynamicPositionBuilder;

    private String lastString;
    private final Map<Integer, Set<Position>> cache = new HashMap<>();

    public PositionsBuilder(TokenSequenceBuilder tokenSequenceBuilder) {
        this.constantPositionBuilder = new ConstantPositionBuilder();
        this.dynamicPositionBuilder = new DynamicPositionBuilder(tokenSequenceBuilder);
    }

    /**
     * Generates all possible position which match a given index on a given string.
     *
     * @param input Input string
     * @param k     index of position to generate to
     * @return set of positions
     */
    public Set<Position> computePositions(String input, int k) {
        if (input.equals(lastString)) {
            cache.clear();
            lastString = input;
        } else if (cache.containsKey(k)) {
            return cache.get(k);
        }

        Set<Position> constantPositions = constantPositionBuilder.computePositions(input, k);
        Set<Position> dynamicPositions = dynamicPositionBuilder.computePositions(input, k);

        Set<Position> union = union(constantPositions, dynamicPositions);
        cache.put(k, union);
        return union;
    }

    private Set<Position> union(Set<Position> a, Set<Position> b) {
        var union = new HashSet<Position>(a.size() + b.size());
        union.addAll(a);
        union.addAll(b);
        return union;
    }

}
