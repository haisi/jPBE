/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */

package li.selman.jpbe.dsl.position;

import java.util.Set;

public interface PositionBuilder {

    /**
     * Generates all possible position which match a given index on a given string.
     *
     * @param input Input string
     * @param k     index of position to generate to
     * @return set of positions
     * @throws IllegalArgumentException if {@code k} < 0
     * @throws IllegalArgumentException if {@code k} > {@code input.length()}
     */
    Set<Position> computePositions(String input, int k);

}
