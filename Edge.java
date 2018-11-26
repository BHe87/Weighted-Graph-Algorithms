public class Edge {

	private static final double FLOATING_POINT_EPSILON = 1E-10;
	private int from;
	private int to;
	private String edgeType;
	private int capacity;
	private int weight;
	private int flow = 0;

	public Edge(int from, int to, String edgeType, int capacity, int weight, int flow) {
		this.from = from;
		this.to = to;
		this.edgeType = edgeType;
		this.capacity = capacity;
		this.weight = weight;
		this.flow = flow;
	}

	public int from() {
		return from;
	}

	public int to() {
		return to;
	}

	public int weight() {
		return weight;
	}

	public int capacity() {
		return capacity;
	}

	public int flow() {
		return flow;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public void setFrom(int from) {
		this.from = from;
	}
	
	public int otherEndPoint(int vertex) {
		if (vertex == from) {
			return to;
		}
		else if (vertex == to) {
			return from;
		} else throw new IllegalArgumentException("Invalid Endpoint");
	}

	public String edgeType() {
		return edgeType;
	}
	
	public double residualCapacityTo(int vertex) {
		if (vertex == from) {//backward edge
			return flow;
		} else if (vertex == to) {//forward edge
			return capacity - flow;
		} else throw new IllegalArgumentException("Invalid Endpoint");
	}

	public void addResidualFlowTo(int vertex, double delta) {
		if (!(delta >= 0.0)) {
			throw new IllegalArgumentException("Delta must be nonnegative");
		}
		if (vertex == from) {//backward edge
			flow -= delta;
		} else if (vertex == to) {//forward edge
			flow += delta;
		} else throw new IllegalArgumentException("Invalid Endpoint");

		if (Math.abs(flow) <= FLOATING_POINT_EPSILON) {
			flow = capacity;
		}
		if (!(flow >= 0)) {
			throw new IllegalArgumentException("Flow is Negative");
		}
		if (!(flow <= capacity)) {
			throw new IllegalArgumentException("Flow Exceeds capacity");
		}
	}

	public String toString() {
		return from + "->" + to + " " + String.format("%5.2f", weight);
	}

	public String toStringFlow() {
		return from + "->" + to + " " + flow + "/" + capacity;
	}
}