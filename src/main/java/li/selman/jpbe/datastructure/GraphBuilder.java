/*
 * (c) Copyright 2021 Hasan Selman Kara. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package li.selman.jpbe.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import li.selman.jpbe.dsl.Expression;
import li.selman.jpbe.dsl.ExpressionBuilder;

/**
 * @author Hasan Selman Kara
 */
public class GraphBuilder {

    private final List<ExpressionBuilder> expressionBuilders;

    /**
     * @throws IllegalArgumentException if {@code expressionBuilders} is empty
     */
    public GraphBuilder(List<ExpressionBuilder> expressionBuilders) throws IllegalArgumentException {
        if (expressionBuilders.isEmpty()) throw new IllegalArgumentException("ExpressionBuilders cannot be empty");

        this.expressionBuilders = new ArrayList<>(expressionBuilders);
    }

    /**
     * Creates a directed acyclic graph where each node represents the index between two characters of
     * the output string.
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
                    // TODO(#bug): creates too many SubstringExpressions
                    .map(expressionBuilder -> expressionBuilder.computeExpressions(input, substring))
                    .flatMap(List::stream)
                    .collect(Collectors.toSet());

                edges.add(new Edge(from, to, expressions));
            }
        }

        return new Graph(output.length(), edges);
    }

}
