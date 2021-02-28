/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */

package li.selman.jpbe.dsl.position;

import java.util.Set;

public class ConstantPositionBuilder {

    public Set<Position> computePositions(String input, int k) {
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
