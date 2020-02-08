package li.selman.jpbe.datastructure;

import li.selman.jpbe.dsl.Expression;

import java.util.Set;

/**
 * @author Hasan Selman Kara
 */
public class Edge {

    public final int from;
    public final int to;
    private final Set<Expression> expressions;

    public Edge(int from, int to, Set<Expression> expressions) {
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
}
