package li.selman.jpbe.algorithm.expressionbuilders;

import li.selman.jpbe.dsl.expression.ConstantStringExpression;
import li.selman.jpbe.dsl.expression.Expression;

import java.util.List;

/**
 * @author Hasan Selman Kara
 */
public class ConstStrExpressionBuilder implements ExpressionBuilder {

    @Override
    public List<Expression> computeExpressions(String input, String substr) {
        return List.of(new ConstantStringExpression(substr));
    }

}
