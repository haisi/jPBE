/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.conststr;

import java.util.List;
import li.selman.jpbe.dsl.Expression;
import li.selman.jpbe.dsl.ExpressionBuilder;

/**
 * @author Hasan Selman Kara
 */
public class ConstStrExpressionBuilder implements ExpressionBuilder {

    @Override
    public List<Expression> computeExpressions(String input, String substr) {
        return List.of(new ConstantStringExpression(substr));
    }

}
