import java.util.*;

public class FlowNetwork {
    private static final String NEWLINE = System.getProperty("line.separator");
    private final int V;
    private int E;
    private List<List<FlowEdge>> graph;
    private List<FlowEdge> currentList;

    public FlowNetwork(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Number of vertices in a Graph must be nonnegative");
        }
        this.V = V;
        this.E = 0;
        graph = new ArrayList<List<FlowEdge>>();
        for (int v = 0; v < V; v++) {
            currentList = new ArrayList<FlowEdge>();
            graph.add(currentList);
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    public void addEdge(FlowEdge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        graph.get(v).add(e);
        graph.get(w).add(e);
        E++;
    }

    public Iterable<FlowEdge> adjacentVertices(int v) {
        validateVertex(v);
        return graph.get(v);
    }

    // return list of all edges - excludes self loops
    public List<FlowEdge> edges() {
        List<FlowEdge> listOfEdges = new ArrayList<FlowEdge>();
        for (int v = 0; v < V; v++) {
            for (FlowEdge e : graph.get(v)) {
                if (e.to() != v) {
                    listOfEdges.add(e);
                }
            }
        }    
        return listOfEdges;
    }

    public String toString() {
        System.out.println("Printing Residual Graph");
        StringBuilder s = new StringBuilder();
        s.append("Number of Vertices: " + V + NEWLINE + "Number of Edges: " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ":  ");
            for (FlowEdge e : graph.get(v)) {
                if (e.to() != v) {
                    s.append(e + "  ");
                }
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}