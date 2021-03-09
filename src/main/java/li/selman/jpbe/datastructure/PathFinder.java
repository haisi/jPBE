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

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Hasan Selman Kara
 * @see <a href="https://www.geeksforgeeks.org/shortest-path-for-directed-acyclic-graphs/">Algorithm explained</a>
 */
public class PathFinder {

    private final Graph graph;
    private final int v;

    public PathFinder(Graph graph) {
        this.graph = graph;
        this.v = graph.getMaxNode() + 1;
    }

    void shortestPath() {
        // u = from
        // t = to

        Deque<Integer> stack = new ArrayDeque<>();
        int[] dist = new int[v];

        // Mark all the edges as not visited
        boolean[] visited = new boolean[v];
        for (int i = 0; i < v; i++) {
            visited[i] = false;
        }

        // Store topological
        for (int i = 0; i < v; i++) {
            if (!visited[i]) {
                topologicalSort(i, visited, stack);
            }
        }

    }

    private void topologicalSort(int i, boolean[] visited, Deque<Integer> stack) {
        // Mark the current node as visited
        visited[i] = true;

//        graph.getEdges()


    }

}
