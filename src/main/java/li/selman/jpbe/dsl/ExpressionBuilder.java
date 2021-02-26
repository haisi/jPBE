/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.dsl;

import java.util.List;

/**
 * Interface for expression builders, so that the graph builder can be generically extended
 * with more expression builders.
 *
 * @author Hasan Selman Kara
 */
public interface ExpressionBuilder {

    /**
     * @param input of the initially provided input example
     * @param substr of the initially provided output example
     * @return all expressions of the builder, so that each expression f produces: f(input) = substr
     */
    List<Expression> computeExpressions(final String input, final String substr);

}
