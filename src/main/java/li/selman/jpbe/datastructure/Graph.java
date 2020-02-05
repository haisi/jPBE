package li.selman.jpbe.datastructure;

import li.selman.jpbe.dsl.Expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Hasan Selman Kara
 */
public class Graph {

    private final int maxNode;
    private final List<Edge> edges;

    public Graph(int maxNode, List<Edge> edges) {
        this.maxNode = maxNode;
        this.edges = edges;
    }

    public Graph intersect(Graph other) {
        int n1 = maxNode;
        int n2 = other.maxNode;
        // number of nodes after Cartesian product
        int max = (n1 + 1) * (n2 + 1) - 1;
        List<Edge> newEdges = new ArrayList<>();
        List<List<Edge>> fromMap = new ArrayList<>(max + 1);
        List<List<Edge>> toMap = new ArrayList<>(max + 1);

        for (Edge edge1 : this.edges) {
            for (Edge edge2 : other.edges) {
                // as there are no loops,
                // thus, the following nodes are impossible to lead from s to t
                if (edge1.from == 0 && edge2.from != 0
                    || edge1.from != 0 && edge2.from == 0
                    || edge1.from == n1 && edge2.from != n2
                    || edge1.from != n1 && edge2.from == n2
                    || edge1.to == 0 && edge2.to != 0
                    || edge1.to != 0 && edge2.to == 0
                    || edge1.to == n1 && edge2.to != n2
                    || edge1.to != n1 && edge2.to == n2) continue;

                final Set<Expression> intersect;
                if (edge1.getExpressionsSize() <= edge2.getExpressionsSize()) {
                    intersect = new HashSet<>(edge1.getExpressions());
                    intersect.retainAll(edge2.getExpressions());
                } else {
                    intersect = new HashSet<>(edge2.getExpressions());
                    intersect.retainAll(edge1.getExpressions());
                }

                if (intersect.isEmpty()) continue;
                int from = edge1.from + edge2.from * (n1 + 1);
                int to = edge1.to + edge2.to * (n1 + 1);
                Edge edge = new Edge(from, to, intersect);
                newEdges.add(edge);
                addToDictionaries(fromMap, toMap, edge);
            }
        }

        return null;
    }

    private static void addToDictionaries(List<List<Edge>> fromDict, List<List<Edge>> toDict, Edge edge) {
        if (fromDict.get(edge.from) == null)
            fromDict.set(edge.from, new ArrayList<>());
        fromDict.get(edge.from).add(edge);

        if (toDict.get(edge.to) == null)
            toDict.set(edge.to, new ArrayList<>());
        toDict.get(edge.to).add(edge);
    }

    int getMaxNode() {
        return maxNode;
    }

    List<Edge> getEdges() {
        return edges;
    }
}
