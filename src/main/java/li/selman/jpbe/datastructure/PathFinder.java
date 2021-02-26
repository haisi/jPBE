package li.selman.jpbe.datastructure;

import java.util.Stack;

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

        Stack<Integer> stack = new Stack<>();
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

    private void topologicalSort(int i, boolean[] visited, Stack<Integer> stack) {
        // Mark the current node as visited
        visited[i] = true;

//        graph.getEdges()


    }

}
