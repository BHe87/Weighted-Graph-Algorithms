import java.util.*;
import java.io.*;
import java.text.*;

public class NetworkAnalysis {

	private static AdjacencyList graph;
	private static FlowNetwork residualGraph;
	private static Scanner fileReader;
	private static int vertexCount;
	private static Dijkstra sp;

	public static void main(String[] args) {
		File file = new File(args[0]);
		createGraph(file);
		createResidualGraph(file);
		System.out.println("Please Select an Option" + "\n" +
						   "(1) Find the Lowest Latency Path Between Two Points" + "\n" +
						   "(2) See if the Graph is Copper-Only Connected" + "\n" + 
						   "(3) Find the Maximum Amount of Data That Can Be Transmitted From One Point to Another" + "\n" + 
						   "(4) See if the Graph Would Remain Connected if Any Two Vertices Were to Fail" + "\n" + 
						   "(5) Quit Program");

		Scanner reader = new Scanner(System.in);
		String response = reader.nextLine();
		if (response.equals("1")) {//Dijkstra's Algorithm
			System.out.println("Point a?");
			int s = reader.nextInt();
			System.out.println("Point b?");
			int t = reader.nextInt();
			sp = new Dijkstra(graph, s);
			sp.findShortestPath(graph, s, t);
		}

		if (response.equals("2")) {//make new graph using only copper cables and check if this new graph is connected
			createCopperGraph(file);
			CC cc = new CC(graph);
			System.out.println(cc.isConnected());
		}

		if (response.equals("3")) {//Ford Fulkerson
			System.out.println("Point a?");
			int s = reader.nextInt();
			System.out.println("Point b?");
			int t = reader.nextInt();
			FordFulkerson ff = new FordFulkerson(residualGraph, s, t);
			ff.findMaxFlow(residualGraph, s, t);
		}
		if (response.equals("4")) {//remove any 2 vertices and see if graph is still connected
			for (int i = 0; i < graph.V(); i++) {
				for (int j = i+1; j < graph.V(); j++) {
					graph.remove(i);
					graph.remove(j);
					CC cc = new CC(graph, i, j);
					if (!cc.isConnected()) {
						System.out.println("False");
						return;
					}
					createGraph(file);					
				}
			}
			System.out.println("True");
		}
		if (response.equals("5")) {
			System.exit(0);
		}
	}

	public static void createGraph(File file) {
		try {
			fileReader = new Scanner(file);
			vertexCount = fileReader.nextInt();
			graph = new AdjacencyList(vertexCount);//representing the graph as an adjacency list
			System.out.println(fileReader.nextLine());
			graph.createTopRow(vertexCount);//adding the "top row" of with all the vertices in the graph

			while (fileReader.hasNext()) {
				String l = fileReader.nextLine();
				String[] line = l.split(" ");
				//go to the first endpoint in the adjacency list and add the second endpoint to that index's list
				graph.add(Integer.parseInt(line[0]), Integer.parseInt(line[1]), line[2], Integer.parseInt(line[3]), Integer.parseInt(line[4]));
				//adding the reverse of the above edge since our graph is undirected
				graph.add(Integer.parseInt(line[1]), Integer.parseInt(line[0]), line[2], Integer.parseInt(line[3]), Integer.parseInt(line[4]));
			}
			//graph.printGraph();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("error");
		}		
	}

	public static void createResidualGraph(File file) {
		try {
			fileReader = new Scanner(file);
			vertexCount = fileReader.nextInt();
			residualGraph = new FlowNetwork(vertexCount);
			System.out.println(fileReader.nextLine());

			while (fileReader.hasNext()) {
				String l = fileReader.nextLine();
				String[] line = l.split(" ");
				FlowEdge flowEdge = new FlowEdge(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[3]), 0);
				FlowEdge backwardsEdge = new FlowEdge(Integer.parseInt(line[1]), Integer.parseInt(line[0]), Integer.parseInt(line[3]), 0);//backwards edges have a capacity of the forwards edge's flow
				residualGraph.addEdge(flowEdge);
				residualGraph.addEdge(backwardsEdge);
			}
			System.out.println(residualGraph.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("error");
		}

	}

	public static void createCopperGraph(File file) {
		try {
			graph = new AdjacencyList();
			fileReader = new Scanner(file);
			vertexCount = fileReader.nextInt();
			System.out.println(fileReader.nextLine());
			graph.createTopRow(vertexCount);

			while (fileReader.hasNext()) {
				String nextLine = fileReader.nextLine();
				String[] lineArray = nextLine.split(" ");
				if (lineArray[2].equals("copper")) {
					graph.add(Integer.parseInt(lineArray[0]), Integer.parseInt(lineArray[1]), lineArray[2], Integer.parseInt(lineArray[3]), Integer.parseInt(lineArray[4]));
					graph.add(Integer.parseInt(lineArray[1]), Integer.parseInt(lineArray[0]), lineArray[2], Integer.parseInt(lineArray[3]), Integer.parseInt(lineArray[4]));
				}
			}
			//graph.printGraph();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("error");
		}
	}
}