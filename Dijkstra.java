import java.util.*;

public class Dijkstra {
	AdjacencyList G = new AdjacencyList();
	private final double copper = 230000000;
	private final double optical = 200000000;
	private double[] distanceTo; //distanceTo[v] = distance of shortest s->v path
	private Edge[] edgeTo; //edgeTo[v] = last edge on shortest s->v path
	private IndexMinPQ<Double> pq; //priority queue of vertices

	public Dijkstra(AdjacencyList G, int s) {//G is the graph, s is the starting vertex, t is the destination vertex
		distanceTo = new double[G.V()];
		edgeTo = new Edge[G.V()];
		validateVertex(s);

		for (int v = 0; v < G.V(); v++) {
			distanceTo[v] = Integer.MAX_VALUE;
		}
		distanceTo[s] = 0;

		pq = new IndexMinPQ<Double>(G.V());
		pq.insert(s, distanceTo[s]);
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			for (Edge edge : G.adjacentVertices(v)) {//for every vertex connected to v
				relax(edge);
			}
		}
	}

	public Dijkstra() {
	}

	private void relax(Edge e) {//filling the arrays with the shortest path between points
		int v = e.from();
		int w = e.to();
		double weight = 0;

		if (e.edgeType().equals("copper")) {
			weight = e.weight()/ copper;
		} else {
			weight = e.weight() / optical;
		}

		if (distanceTo[w] > distanceTo[v] + weight) {
			distanceTo[w] = distanceTo[v] + weight;
			edgeTo[w] = e;
			if (pq.contains(w)) {
				pq.decreaseKey(w, distanceTo[w]);
			} else {
				pq.insert(w, distanceTo[w]);
			}			
		}
	}

	public double distanceTo(int v) {
		validateVertex(v);
		return distanceTo[v];
	}

	public boolean hasPathTo(int v) {
		validateVertex(v);
		return distanceTo[v] < Integer.MAX_VALUE;
	}

	public Stack<Edge> pathTo(int v) {
		validateVertex(v);
		if (!hasPathTo(v)) {
			return null;
		}
		Stack<Edge> path = new Stack<Edge>();
		for (Edge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
			path.push(e);
		}
		return path;
	}

	private void validateVertex(int v) {//check if v is in the graph
		int V = distanceTo.length;
		if (v < 0 || v >= V) {
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
		}
	}

	public double findShortestPath(AdjacencyList G, int s, int t) {
		Dijkstra sp = new Dijkstra(G, s);
		System.out.println("Shortest Path: " + distanceTo[t] + " m/s");

		if (sp.hasPathTo(t)) { 
			Stack<Edge> stack = sp.pathTo(t);
			while (!stack.isEmpty()) {
				System.out.print(stack.pop().from() + " -> ");
			}
			System.out.print(t);
		} else {
			System.out.printf("No path found from " + s + " to " + t);
		}
		return sp.distanceTo(t);
	}
}