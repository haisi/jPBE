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

/**
 * Denotes a constant index of a string.
 * If {@link ConstantPosition#constant} is positive, the index is counted from the left —
 * just like {@link String#charAt(int)}.
 * <p>
 * If the constant is negative, the index is counted from the right.
 * In that case the last index of a string is denoted with the value in {@link ConstantPosition#LAST_POSITION_INDEX} as
 * 0 is already reserved for the "positive" index way of counting.
 * @author Hasan Selman Kara
 */
public class ConstantPosition implements Position {

    private static final int LAST_POSITION_INDEX = Integer.MIN_VALUE;

    private final int constant;

    /**
     * @param constant signed integer denoting the index on any string
     */
    public ConstantPosition(int constant) {
        this.constant = constant;
    }

    public static ConstantPosition lastPosition() {
        return new ConstantPosition(LAST_POSITION_INDEX);
    }

    @Override
    public int evalToPosition(String s) {
        if (constant == LAST_POSITION_INDEX) {
            return s.length();
        }

        return constant >= 0 ? constant : s.length() + constant;
    }

    @Override
    public int getDslWeight() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstantPosition that = (ConstantPosition) o;

        return constant == that.constant;
    }

    @Override
    public int hashCode() {
        return constant;
    }

    @Override
    public String toString() {
        return String.format("CPos(%s)", constant);
    }
}
