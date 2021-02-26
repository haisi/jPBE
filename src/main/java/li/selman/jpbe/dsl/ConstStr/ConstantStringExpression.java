/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl.ConstStr;

import java.util.Optional;
import li.selman.jpbe.dsl.Expression;

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
        if ((constant == null || constant.isEmpty())) {
            throw new IllegalArgumentException("The constant must not be null or empty!");
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstantStringExpression that = (ConstantStringExpression) o;

        return constant.equals(that.constant);
    }

    @Override
    public int hashCode() {
        return constant.hashCode();
    }

    @Override
    public String toString() {
        return String.format("ConStr('%s')", constant);
    }
}
