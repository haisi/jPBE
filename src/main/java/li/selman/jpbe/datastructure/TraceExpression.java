package li.selman.jpbe.datastructure;

import li.selman.jpbe.dsl.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Hasan Selman Kara
 */
public class TraceExpression implements Expression {

    private final List<Expression> expressions;

    public TraceExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    /**
     * Applies all expressions on {@code s} and joins the results in order.
     *
     * @param s input string
     * @return the whole output string
     */
    @Override
    public Optional<String> apply(final String s) {
        List<String> substrings = new ArrayList<>();
        for (var expression : expressions) {
            Optional<String> outputSubstring = expression.apply(s);
            if (outputSubstring.isEmpty()) {
                // Applying failed
                return Optional.empty();
            }

            substrings.add(outputSubstring.get());
        }

        String output = String.join("", substrings);
        return Optional.of(output);
    }

    // TODO implement with streams for easy parallelization
//    public Optional<String> apply(final String s) {
//        final Stream<Optional<String>> appliedExpressions = applyAllExpressions(s);
//
//        if (hasAnyFailedApplications(appliedExpressions)) {
//            // One of the expressions could not be applied on the input
//            return Optional.empty();
//        }
//
//        // TODO java.lang.IllegalStateException: stream has already been operated upon or closed
//        String output = concatAllSubstrings(appliedExpressions);
//
//        return Optional.of(output);
//    }
//
//    private String concatAllSubstrings(Stream<Optional<String>> appliedExpressions) {
//        return appliedExpressions
////            .filter(Optional::isPresent) not necessary as `hasAnyFailedApplications` returned false
//            .map(Optional::get)
//            .collect(Collectors.joining());
//    }
//
//    private boolean hasAnyFailedApplications(Stream<Optional<String>> appliedExpressions) {
//        return appliedExpressions
//            .anyMatch(Optional::isEmpty);
//    }
//
//    private Stream<Optional<String>> applyAllExpressions(String s) {
//        return expressions.stream()
////            .parallel() // TODO applying should be parallelizable
//            .map(expression -> expression.apply(s));
//    }

    @Override
    public int getSize() {
        return expressions.size();
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TraceExpression that = (TraceExpression) o;

        // TODO order of lists should be ignored when comparing
        return expressions.equals(that.expressions);
    }

    @Override
    public int hashCode() {
        return expressions.hashCode();
    }

    @Override
    public String toString() {
        return String.format("TraceExpression(%s)", expressions);
    }
}
