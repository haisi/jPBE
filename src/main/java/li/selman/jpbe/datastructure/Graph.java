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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import li.selman.jpbe.dsl.Expression;
import li.selman.jpbe.dsl.expression.Expressions;

/**
 * @author Hasan Selman Kara
 */
@SuppressWarnings("unchecked")
public class Graph {

    private final int maxNode;
    private final List<Edge> edges;

    // TODO(#optimization): remove mutable state!
//    private List<Edge>[] outgoingEdges;

    Graph(int maxNode, List<Edge> edges) {
        this.maxNode = maxNode;
        this.edges = edges;
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
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
                if ((edge1.from == 0 && edge2.from != 0)
                            || (edge1.from != 0 && edge2.from == 0)
                            || (edge1.from == n1 && edge2.from != n2)
                            || (edge1.from != n1 && edge2.from == n2)
                            || (edge1.to == 0 && edge2.to != 0)
                            || (edge1.to != 0 && edge2.to == 0)
                            || (edge1.to == n1 && edge2.to != n2)
                            || (edge1.to != n1 && edge2.to == n2)) {
                    continue;
                }

                final Set<Expression> intersect;
                if (edge1.getExpressionsSize() <= edge2.getExpressionsSize()) {
                    intersect = new HashSet<>(edge1.getExpressions());
                    intersect.retainAll(edge2.getExpressions());
                } else {
                    intersect = new HashSet<>(edge2.getExpressions());
                    intersect.retainAll(edge1.getExpressions());
                }

                if (intersect.isEmpty()) {
                    continue;
                }
                int from = edge1.from + edge2.from * (n1 + 1);
                int to = edge1.to + edge2.to * (n1 + 1);
                Edge edge = new Edge(from, to, intersect);
                newEdges.add(edge);
                addToDictionaries(fromMap, toMap, edge);
            }
        }

        // TODO(#wip): finish intersect
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

    // TODO(#api): extract the searching the path algorithm
    //  - So users can choose if they don't want to have the heuristics of getting the shortest path,
    //  i.e. a global min instead of a local

    // TODO(#refactor): create class `TraceSet` that wrap List<TraceExpression> and has functions to get the best path

    /**
     * Finds the optimal path from S to T.
     *
     * @return direct path of edges from start to end or an empty list
     */
    public List<Edge> computeLocalOptimaPath() {
        // TODO(#check): check if this state can actually happen? (I guess with an empty graph after intersection?)
        if (edges == null || edges.isEmpty()) throw new IllegalStateException("Edges cannot be null or empty");

        Optional<Edge> directEdge = findDirectEdge();
        if (directEdge.isPresent()) {
            return List.of(directEdge.get());
        }

        /*
          Find the cheapest path from S to T
         */
        // init
        int[] distance = new int[maxNode + 1];
        distance[0] = 0;
        for (int i = 1; i < distance.length; i++) {
            distance[i] = Integer.MAX_VALUE;
        }

        // An array of edges (optimization!)
        List<Edge>[] paths = new List[maxNode + 1];
        paths[0] = new ArrayList<>();


        throw new UnsupportedOperationException("Not done yet!");
//        return getAllTraceExpressions(directEdge);
        // TODO(#wip): handle intersect graph as well
    }

    @SuppressWarnings("UnusedMethod")
    private List<Expressions> getAllTraceExpressions(Stream<Edge> edgeStream) {
        return edgeStream
                .map(Edge::getExpressions)
                // TODO(#api): check whether we should use Set or List
                .map(expressions -> new Expressions(new ArrayList<>(expressions)))
                .collect(Collectors.toList());
    }

    private Optional<Edge> findDirectEdge() {
        return edges.stream()
                .filter(edge -> edge.from == 0 && edge.to == maxNode)
                .findFirst();
    }

    int getMaxNode() {
        return maxNode;
    }

    List<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(String.format("Graph[%d]:%n", maxNode));
        for (Edge edge : edges) {
            builder
                    .append("\t")
                    .append(edge.from).append(" -{").append(edge.getExpressions().size()).append("}->").append(edge.to)
                    .append("\n");
        }

        return builder.toString();
    }
}
