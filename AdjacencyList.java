import java.util.*;

public class AdjacencyList {
	List<List<Edge>> graph = new ArrayList<List<Edge>>();
	List<Edge> currentList;
	private int V;
	private int E;

	public AdjacencyList(int vertexCount) {
		V = vertexCount;
		graph = new ArrayList<List<Edge>>(vertexCount);
	}

	public AdjacencyList() {
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	public List<Edge> adjacentVertices(int v) {
		return graph.get(v);
	}

	public void createTopRow(int vertexCount) {
		V = vertexCount;
		for (int i = 0; i < vertexCount; i++) {
			graph.add(new ArrayList<Edge>());
		}
	}

	public void add(int from, int to, String edgeType, int capacity, int weight) {
		currentList = graph.get(from);
		currentList.add(new Edge(from, to, edgeType, capacity, weight, 0));
		graph.remove(from);
		graph.add(from, currentList);
	}

	public void remove(int vertex) {
		for (int v = 0; v < V(); v++) {
			currentList = graph.get(v);
			Iterator iterator = currentList.iterator();
			while (iterator.hasNext()) {
				Edge edge = (Edge) iterator.next();
				if (edge.to() == vertex || edge.from() == vertex) {
					iterator.remove();
				}
			}
		}
	}

	public void printGraph() {
		System.out.println("Printing Adjacency List");
		for(int i = 0; i < graph.size(); i++) {
			System.out.print(i + " -> ");
			List<Edge> list = graph.get(i);
			for (Edge edge : list) {
				System.out.print(edge.to() + " -> ");
			}
			System.out.println();
		}
	}	
}