package li.selman.jpbe.algorithm.expressionbuilders;

import li.selman.jpbe.dsl.expression.Expression;
import li.selman.jpbe.dsl.expression.SubstringExpression;
import li.selman.jpbe.dsl.position.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Hasan Selman Kara
 */
public class SubstringExpressionBuilder implements ExpressionBuilder {

    private final PositionBuilder positionBuilder;

    public SubstringExpressionBuilder(PositionBuilder positionBuilder) {
        this.positionBuilder = positionBuilder;
    }

    @Override
    public List<Expression> computeExpressions(final String input, final String substr) {
        List<Expression> result = new ArrayList<>();

        for (Integer k : substringsStartIndices(input, substr)) {
            Set<Position> y1 = positionBuilder.computePositions(input, k);
            Set<Position> y2 = positionBuilder.computePositions(input, k + input.length());

            for (Position p1 : y1) {
                for (Position p2 : y2) {
                    result.add(new SubstringExpression(p1, p2));
                }
            }
        }

        return result;
    }

    // TODO replace with Intlist to avoid boxing
    static List<Integer> substringsStartIndices(String s, String substring) {
        // TODO replace with KMP
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
