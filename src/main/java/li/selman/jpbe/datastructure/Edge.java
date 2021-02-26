/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.datastructure;

import java.util.Set;
import li.selman.jpbe.dsl.Expression;

/**
 * @author Hasan Selman Kara
 */
public class Edge {

    public final int from;
    public final int to;
    private final Set<Expression> expressions;

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
