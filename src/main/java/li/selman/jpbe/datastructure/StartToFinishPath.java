/*
 * (c) Copyright 2021 Hasan Selman Kara All rights reserved.
 */
package li.selman.jpbe.datastructure;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import li.selman.jpbe.dsl.Expression;

/**
 * Represents all paths on {@link Graph} that lead from start to finish.
 *
 * @author Hasan Selman Kara
 */
// TODO(#optimization): why is this so slow?

// TODO(#api): could be written as a Java 8 interface
//  - Makes it extendable
//  - No need to allocate data on the heap
public class StartToFinishPath {

    private final List<Edge> path;

    /**
     * @param edges must be ordered from S to T.
     * @throws IllegalArgumentException if edges are null or empty
     */
    StartToFinishPath(List<Edge> edges, int maxNode) {
        if (edges == null || edges.isEmpty()) throw new IllegalArgumentException("Paths cannot be null or empty");
        if (!isOrdered(edges, maxNode)) throw new IllegalArgumentException("Edges are not ordered");

        this.path = edges;
    }

    private boolean isOrdered(List<Edge> edges, int maxNode) {
        int previousTo = 0;
        final int lastIdx = edges.size() - 1;
        for (int i = 0; i < edges.size(); i++) {
            var edge = edges.get(i);

            if (firstEdgeDoesNotStartsFromZero(i, edge)) return false;
            if (lastEdgeDoesNotFinishAtMaxNode(lastIdx, i, edge, maxNode)) return false;

            if (edge.from != previousTo) {
                return false;
            } else {
                previousTo = edge.to;
            }
        }

        return true;
    }

    private boolean lastEdgeDoesNotFinishAtMaxNode(int lastIdx, int i, Edge edge, int maxNode) {
        if (i == lastIdx) {
            return edge.to != maxNode;
        }
        return false;
    }

    private boolean firstEdgeDoesNotStartsFromZero(int i, Edge edge) {
        if (i == 0) {
            return edge.from != 0;
        }
        return false;
    }

    private static Expression smallestExpressionOfEachEdge(Edge edge) {
        return edge.getExpressions()
            .stream()
            .min(Comparator.comparingInt(Expression::getDslWeight))
            .orElseThrow(() -> new RuntimeException("Edge cannot have no elements: " + edge));
    }

    public TraceExpression computeOptimalTraceExpression() {
        List<Expression> optimalExpressions = path.stream()
            .map(StartToFinishPath::smallestExpressionOfEachEdge)
            .collect(Collectors.toUnmodifiableList());

        return new TraceExpression(optimalExpressions);
    }

}
