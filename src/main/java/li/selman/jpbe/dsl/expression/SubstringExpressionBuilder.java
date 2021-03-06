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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import li.selman.jpbe.dsl.Expression;
import li.selman.jpbe.dsl.ExpressionBuilder;
import li.selman.jpbe.dsl.position.Position;
import li.selman.jpbe.dsl.position.PositionsBuilder;

/**
 * @author Hasan Selman Kara
 */
public class SubstringExpressionBuilder implements ExpressionBuilder {

    private final PositionsBuilder positionBuilder;

    public SubstringExpressionBuilder(PositionsBuilder positionBuilder) {
        this.positionBuilder = positionBuilder;
    }

    @Override
    public List<Expression> computeExpressions(final String input, final String substr) {
        List<Expression> result = new ArrayList<>();

        for (Integer k : substringsStartIndices(input, substr)) {
            Set<Position> y1 = positionBuilder.computePositions(input, k);
            // in reference it's: k + input.length()   WARUM?!?!
            Set<Position> y2 = positionBuilder.computePositions(input, input.length() - k);

            for (Position p1 : y1) {
                for (Position p2 : y2) {
                    result.add(new SubstringExpression(p1, p2));
                }
            }
        }

        return result;
    }

    // TODO(#optimization): replace with Intlist to avoid boxing
    static List<Integer> substringsStartIndices(String s, String substring) {
        // TODO(#optimization): replace with KMP
        List<Integer> positions = new ArrayList<>();
        if (s.length() < substring.length()) {
            return positions;
        }

        for (int i = 0; i < s.length() - substring.length() + 1; i++) {
            if (s.substring(i, i + substring.length()).equals(substring)) {
                positions.add(i);
            }
        }

        return positions;
    }

}
