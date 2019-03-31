package li.selman.jpbe.dsl.expression;

import li.selman.jpbe.JPbeUtils;
import li.selman.jpbe.dsl.expression.Expression;

import java.util.Optional;

/**
 * @author Hasan Selman Kara
 */
public class ConstantStringExpression implements Expression {

    private final String constant;

    public ConstantStringExpression(String constant) {
        JPbeUtils.requireIsNotNullOrEmpty(constant, "The constant must not be null or empty!");
        this.constant = constant;
    }

    @Override
    public Optional<String> apply(String s) {
        return Optional.of(constant);
    }

    @Override
    public int getSize() {
        return 1;
    }
}
