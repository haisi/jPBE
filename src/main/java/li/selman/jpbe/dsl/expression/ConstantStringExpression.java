package li.selman.jpbe.dsl.expression;

import li.selman.jpbe.JPbeUtils;

import java.util.Optional;

/**
 * @author Hasan Selman Kara
 */
public class ConstantStringExpression implements Expression {

    private final String constant;

    /**
     * @param constant will always be returned regardless the input string
     * @throws IllegalArgumentException if {@code constant} is null or empty
     */
    public ConstantStringExpression(String constant) throws IllegalArgumentException {
        // Because the expression represents one possible way to compute a substring of the initial, given string.
        // Thus, it must represent at least one character.
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
