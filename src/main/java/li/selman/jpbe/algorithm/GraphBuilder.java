package li.selman.jpbe.algorithm;

import li.selman.jpbe.algorithm.expressionbuilders.ExpressionBuilder;
import li.selman.jpbe.datastructure.Edge;
import li.selman.jpbe.datastructure.Graph;
import li.selman.jpbe.dsl.expression.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Hasan Selman Kara
 */
public class GraphBuilder {

    private final List<ExpressionBuilder> expressionBuilders;

    /**
     * @throws IllegalArgumentException if {@code expressionBuilders} is empty
     */
    public GraphBuilder(List<ExpressionBuilder> expressionBuilders) throws IllegalArgumentException {
        if (expressionBuilders.size() == 0) throw new IllegalArgumentException("");

        this.expressionBuilders = new ArrayList<>(expressionBuilders);
    }

    /**
     * Creates a directed acyclic graph where each node represents the index between two characters of the output string.
     * The edges represent a operation that returns the given substring between the two nodes given the input string.
     * Each edge contains a set of possible operations.
     *
     * @param input  user provided input
     * @param output user provided output
     * @return directed acyclic graph with all expressions that satisfy the I/O example
     */
    public Graph createAllPrograms(final String input, final String output) {

        int size = output.length() * output.length() / 2;
        final List<Edge> edges = new ArrayList<>(size);

        // Loop over all possible substrings of 'output' (n * n / 2)
        for (int from = 0; from < output.length(); from++) {
            for (int to = from + 1; to <= output.length(); to++) {

                // For each substring find all expressions f, so that f(input) = substring
                var substring = output.substring(from, to);

                // All expression for which `f(input) = substr` applies
                Set<Expression> expressions = expressionBuilders.stream()
                    .map(expressionBuilder -> expressionBuilder.computeExpressions(input, substring))
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());

                edges.add(new Edge(from, to, expressions));
            }
        }

        return new Graph(output.length(), edges);
    }

}
