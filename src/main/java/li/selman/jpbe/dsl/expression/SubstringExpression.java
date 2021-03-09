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
package li.selman.jpbe.dsl.expression;

import java.util.Objects;
import java.util.Optional;
import li.selman.jpbe.dsl.Expression;
import li.selman.jpbe.dsl.position.NoPositionException;
import li.selman.jpbe.dsl.position.Position;

/**
 * @author Hasan Selman Kara
 */
public class SubstringExpression implements Expression {

    private final Position startPosition;
    private final Position endPosition;

    public SubstringExpression(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    /**
     * @param s input string
     * @return substring of input or empty if start or end index of substring evals to out of bounds
     */
    @Override
    public Optional<String> apply(final String s) {
        Objects.requireNonNull(s, "Cannot apply substring expression on null!");

        int startIdx;
        int endIndex;
        try {
            startIdx = startPosition.evalToPosition(s);
            endIndex = endPosition.evalToPosition(s);
        } catch (NoPositionException ex) {
            return Optional.empty();
        }

        if (isAnyIndexOutOfBounds(s.length(), startIdx, endIndex)) {
            return Optional.empty();
        }

        return Optional.of(s.substring(startIdx, endIndex));
    }

    private boolean isAnyIndexOutOfBounds(int stringLength, int startIdx, int endIndex) {
        return startIdx > endIndex || endIndex > stringLength;
    }

    @Override
    public int getDslWeight() {
        return startPosition.getDslWeight() * endPosition.getDslWeight();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubstringExpression that = (SubstringExpression) o;

        if (!startPosition.equals(that.startPosition)) return false;
        return endPosition.equals(that.endPosition);
    }

    @Override
    public int hashCode() {
        int result = startPosition.hashCode();
        result = 31 * result + endPosition.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("SubStr(%s, %s)", startPosition, endPosition);
    }
}
