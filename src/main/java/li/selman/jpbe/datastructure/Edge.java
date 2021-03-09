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

import java.util.Set;
import li.selman.jpbe.dsl.Expression;

/**
 * An edge represents a set of programs which evaluate to a substring of the intended output string.
 * An edge is part of a directed acyclic graph (DAG).
 * <p>
 * For example: Given input {@code i_1} and output {@code o_1}, we generate a graph with
 * {@code n * (n - 1) / 2} edges where {@code n = o_1.length},
 * because we generate an edge for each possible substring made out of {@code o_1}.
 * {@link Edge#from} represents the start index of such substring and {@link Edge#to} the end index.
 * While {@link Edge#expressions} contains all valid expressions in our DSL for which following constraint holds true.
 * <p>
 * For each expression {@code exp} in Expression | {@code exp.apply(i_1) == o_1.substring(from, to)}
 * <p>
 * For example: Given i_1 = "Joe Bob 1990.12.3" and output o_1 = "Bob 1990", we might have the following edge:
 * {@code Edge(0, 3, { CStr("Bob"), SubStr(CPos(4), CPos(7), ... })} where all expression on i_1 would eval to "Bob".
 * @author Hasan Selman Kara
 */
// TODO(#api): turn into record
public class Edge {

    public final int from;
    public final int to;
    private final Set<Expression> expressions;

    /**
     * @param from start index on a substring of the user provided output string
     * @param to end index on a substring of the user provided output string
     * @param expressions all expressions which on input_1 eval to provided_output_1.substring(from, to)
     */
    public Edge(int from, int to, Set<Expression> expressions) {
        if (from >= to) throw new IllegalArgumentException(String.format("Edge cannot go from '%d' to '%d'", from, to));
        if (expressions.isEmpty()) throw new IllegalArgumentException("Expressions cannot be empty");

        this.from = from;
        this.to = to;
        this.expressions = expressions;
    }

    public Set<Expression> getExpressions() {
        return expressions;
    }

    public int getExpressionsSize() {
        return this.expressions.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (from != edge.from) return false;
        if (to != edge.to) return false;
        return expressions.equals(edge.expressions);
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + to;
        result = 31 * result + expressions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("Edge{From=%d, To=%d, Expressions=%s", from, to, expressions);
    }
}
