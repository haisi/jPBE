package li.selman.jpbe.dsl.expression;

import li.selman.jpbe.dsl.DslElement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Hasan Selman Kara
 */
public class TraceExpression implements Expression {

    private final List<Expression> expressions;

    public TraceExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public Optional<String> apply(final String s) {
        final String output = expressions.stream()
            .map(expression -> expression.apply(s))
            // TODO here we hide that some expression could not be applied on the input
            // TODO furthermore, we replace it with an empty string
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.joining());

        return Optional.ofNullable(output);
    }

    @Override
    public int getSize() {
        return expressions.stream().mapToInt(DslElement::getSize).sum();
    }
}
