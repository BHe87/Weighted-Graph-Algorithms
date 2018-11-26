import java.util.*;
import java.lang.Math;

public class FordFulkerson {
	AdjacencyList G = new AdjacencyList();
	private static final double FLOATING_POINT_EPSILON = 1E-11;
	private final int V; //number of vertices
	private boolean[] visited; //visited[v] = true iff s -> v path is in residual graph
	private FlowEdge[] edgeTo; //edgeTo[v] = last edge on shortest residual s -> v path
	private double value; //current value of max flow

	public FordFulkerson(FlowNetwork G, int s, int t) {
		V = G.V();//number of vertices in G
		validate(s);
		validate(t);
		if (s == t) {
			throw new IllegalArgumentException("Source equals sink");
		}
		value = excess(G, t);//while there exists an augmenting path, use it
		while (hasAugmentingPath(G, s, t)) {
			double bottle = Double.POSITIVE_INFINITY;
			//finding the maximum amount we can push at a time in our graph
			for (int v = t; v != s; v = edgeTo[v].otherEndPoint(v)) {
				bottle = Math.min(bottle, edgeTo[v].residualCapacityTo(v));//if the latter is less than bottle then we have a new bottleneck value
			}
			for (int v = t; v != s; v = edgeTo[v].otherEndPoint(v)) {//pushing the flow
				edgeTo[v].addResidualFlowTo(v, bottle);
			}
			System.out.println("bottle: " + bottle);
			value += bottle;//constantly adding the bottleneck to value so we know at the end we have our max flow
		}
		assert check(G, s, t);
	}

	public FordFulkerson() {
		V = 0;
	}

	public double value() {
		return value;
	}

	public boolean inCut(int v) {
		validate(v);
		return visited[v];
	}

	private void validate(int v) {
		if (v < 0 || v >= V) {
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
		}
	}

	private double excess(FlowNetwork G, int v) {//returning excess flow at v
		double excess = 0;
		for (FlowEdge edge : G.adjacentVertices(v)) {
			if (v == edge.from()) {
				excess -= edge.flow();
			} else {
				excess += edge.flow();
			}
		}
		return excess;
	}

	private boolean isFeasible(FlowNetwork G, int s, int t) {
		for (int v = 0; v < G.V(); v++) {
			for (FlowEdge edge : G.adjacentVertices(v)) {
				if (edge.flow() < -FLOATING_POINT_EPSILON || edge.flow() > edge.capacity() + FLOATING_POINT_EPSILON) {
					System.out.println("Edge does not satisfy capacity constratints: " + edge);
					return false;
				}
			}
		}
		if (Math.abs(value + excess(G, s)) > FLOATING_POINT_EPSILON) {
			System.out.println("Excess at source = " + excess(G, s));
			System.out.println("Max flow         = " + value);
			return false;
		}
		if (Math.abs(value - excess(G, t)) > FLOATING_POINT_EPSILON) {
			System.out.println("Excess at sink   = " + excess(G, t));
			System.out.println("Max flow         = " + value);
			return false;
		}
		for (int v = 0; v < G.V(); v++) {
			if (v == s || v == t) {
				continue;
			} else if (Math.abs(excess(G, v)) > FLOATING_POINT_EPSILON) {
				System.out.println("Net flow out of " + v + " doesn't equal zero");
				return false;
			}
		}
		return true;
	}

	private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
		edgeTo = new FlowEdge[G.V()];
		visited = new boolean[G.V()];
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.add(s);//visiting this vertex
		visited[s] = true;//marking this vertex as visited so we don't visit it again later
		while (!queue.isEmpty() && !visited[t]) {//BFS traversal
			int v = queue.remove();
			for (FlowEdge edge : G.adjacentVertices(v)) {
				int w = edge.to();//vertex we're traveling to
				if (edge.residualCapacityTo(w) > 0) {
					if (!visited[w]) {
						edgeTo[w] = edge;
						visited[w] = true;
						queue.add(w);
					}
				}
			}
		}
		return visited[t];
	}

	private boolean check(FlowNetwork G, int s, int t) {
		if (!isFeasible(G, s, t)) {
			System.out.println("Flow is infeasible");
			return false;
		}
		if (!inCut(s)) {
			System.out.println("source" + s + " is not on source side of min cut");
			return false;
		}
		if (inCut(t)) {
			System.out.println("sink" + t + " is on source side of min cut");
			return false;
		}
		double mincutValue = 0.0;
		for (int v = 0; v < G.V(); v++) {
			for (FlowEdge edge : G.adjacentVertices(v)) {
				if (v == edge.from() && inCut(edge.from()) && !inCut(edge.to())) {
					mincutValue += edge.capacity();
				}
			}
		}
		if (Math.abs(mincutValue - value) > FLOATING_POINT_EPSILON) {
			System.out.println("Max flow value = " + value + ", min cut value = " + mincutValue);
			return false;
		}
		return true;
	}
	public void findMaxFlow(FlowNetwork G, int s, int t) {//finding the maximum flow from s to t
		System.out.println("Max Flow From " + s + " to " + t + " : " + value());
	}
}