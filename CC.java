import java.util.*;

public class CC {
    private boolean[] visited;  // visited[v] = has vertex v been visited?
    private int[] id;           // id[v] = id of connected component containing v
    private int[] size;         // size[id] = number of vertices in given component
    private int count;          // number of connected components

    public CC(AdjacencyList G) {
        visited = new boolean[G.V()];
        id = new int[G.V()];
        size = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!visited[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    public CC(AdjacencyList G, int i, int j) {//finding the connected components excluding vertices i and j
        visited = new boolean[G.V()];
        visited[i] = true;
        visited[j] = true;
        id = new int[G.V()];
        size = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!visited[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    private void dfs(AdjacencyList G, int v) {
        visited[v] = true;
        id[v] = count;
        size[count]++;
        for (Edge edge : G.adjacentVertices(v)) {
            int w = edge.otherEndPoint(v);
            if (!visited[w]) {
                dfs(G, w);
            }
        }
    }

    public int id(int v) {
        validateVertex(v);
        return id[v];
    }

    public int size(int v) {
        validateVertex(v);
        return size[id[v]];
    }

    public int count() {
        return count;
    }

    public boolean connected(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return id(v) == id(w);
    }

    public boolean areConnected(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return id(v) == id(w);
    }
    
    private void validateVertex(int v) {// throw an IllegalArgumentException unless {@code 0 <= v < V}
        int V = visited.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public boolean isConnected() {
        return (count() == 1);
    }
}