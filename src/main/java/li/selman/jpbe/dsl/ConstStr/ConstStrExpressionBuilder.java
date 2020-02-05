package li.selman.jpbe.dsl.ConstStr;

import li.selman.jpbe.dsl.ExpressionBuilder;
import li.selman.jpbe.dsl.Expression;

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
