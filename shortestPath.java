import java.util.LinkedList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class shortestPath {

	/**
	 * Graph initialization. Set the distance, seen, parent and count variables
	 * of all the vertices to appropriate values
	 * 
	 * @param g
	 * @param src
	 */
	public static void initialize(Graph g, Vertex src) {
		for (Vertex u : g) {
			u.distance = Integer.MAX_VALUE;
			u.parent = null;
			u.seen = false;
			u.count = 0;
		}
		src.distance = 0;
	}

	/**
	 * To update the distance and parent of a vertex with the shortest distance.
	 * 
	 * @param u:
	 *            Vertex
	 * @param v:
	 *            Vertex
	 * @param e:
	 *            Edge
	 * @return: true - if any changes has been done, else false
	 */
	public static boolean relax(Vertex u, Vertex v, Edge e) {
		if (v.distance > u.distance + e.Weight) {
			v.distance = u.distance + e.Weight;
			v.parent = u;
			return true;
		}
		return false;
	}

	/**
	 * To find the shortest path for a graph with Uniform weights,
	 * directed/undirected, cyclic/acyclic, only non-negative edges
	 * 
	 * @param g:
	 *            Graph
	 * @param src:
	 *            Vertex - source
	 */
	public static void BFS(Graph g, Vertex src) {
		Queue<Vertex> queue = new LinkedList<>();// queue to store the vertices
													// to be visited in order
		initialize(g, src);
		queue.add(src);
		src.seen = true;
		Vertex u, v;
		while (!queue.isEmpty()) {
			u = queue.remove();
			for (Edge e : u.Adj) {
				v = e.otherEnd(u);
				// if the Vertex is already seen, ignore that vertex
				if (!v.seen) {
					v.distance = u.distance + 1;
					v.parent = u;
					v.seen = true;
					queue.add(v);
				}
			}
		}
	}

	/**
	 * To find shortest path for a graph with non-uniform weights,
	 * cyclic/acyclic graphs, directed graphs, only non-negative edges
	 * 
	 * @param g:
	 *            Graph
	 * @param src:
	 *            Vertex - Source
	 */
	public static void dijkstraShortestPath(Graph g, Vertex src) {
		initialize(g, src);
		// Create a indexed heap with vertex distance as the priority
		IndexedHeap<Vertex> heap = new IndexedHeap<>(g.verts.toArray(new Vertex[g.verts.size()]), new Vertex());
		Vertex u, v;

		while (!heap.isEmpty()) {
			u = heap.remove();
			u.seen = true;
			for (Edge e : u.Adj) {
				v = e.otherEnd(u);
				// change the priority of the vertex as its distance is changed
				if (!v.seen)
					if (relax(u, v, e))
						heap.decreaseKey(v);
			}
		}
	}

	/**
	 * To find shortest path for a graph having negative edges but not negative
	 * cycles. It must be acyclic
	 * 
	 * @param g:
	 *            Graph
	 * @param src:
	 *            Vertex - source
	 */
	public static void DAG(Graph g, Vertex src, ArrayDeque<Vertex> topOrder) {
		Vertex u, v;
		initialize(g, src);
		// process the vertices in the topological order
		while (!topOrder.isEmpty()) {
			u = topOrder.pop();
			for (Edge e : u.Adj) {
				v = e.otherEnd(u);
				relax(u, v, e);
			}
		}
	}

	/**
	 * To find shortest path from the source vertex if the graph does not have a
	 * negative cycle
	 * 
	 * @param g:
	 *            Graph
	 * @param src:
	 *            Vertex - Source
	 * @return: true if there is no negative cycle, else false
	 */
	public static boolean bellmanFord(Graph g, Vertex src) {
		Queue<Vertex> queue = new LinkedList<>();
		Vertex u, v;
		initialize(g, src);
		src.seen = true;
		queue.add(src);
		while (!queue.isEmpty()) {
			u = queue.remove();
			u.seen = false;
			u.count = u.count + 1;
			// if a vertex is visited more than the number of graphNode times
			// then there is a cycle
			if (u.count >= g.numNodes)
				return false;
			for (Edge e : u.Adj) {
				v = e.otherEnd(u);
				if (relax(u, v, e)) {
					if (!v.seen) {
						queue.add(v);
						v.seen = true;
					}
				}
			}
		}
		return true;
	}

	/**
	 * To print the vertex, its distance and its parent vertex
	 * 
	 * @param g:
	 *            Graph
	 */
	public static void printVertices(Graph g) {
		for (Vertex u : g) {
			if (u.distance != Integer.MAX_VALUE)
				if (u.parent != null)
					System.out.println(u.name + " " + u.distance + " " + u.parent.name);
				else
					System.out.println(u.name + " " + u.distance + " -");
			else
				System.out.println(u.name + " INF -");
		}
	}

	/**
	 * To find the sum of the shortest paths distances to all the vertices
	 * 
	 * @param g:
	 *            Graph
	 */
	public static void sumOfShortestPaths(Graph g) {
		int sumWShortestPaths = 0;
		// add all the vertice's distanceF
		for (Vertex u : g) {
			if (u.parent != null)
				sumWShortestPaths += u.distance;
		}
		System.out.print(sumWShortestPaths);
		System.out.println();
	}

	/**
	 * To find the type of the input graph and apply appropriate efficient
	 * algorithm to find the shortest path
	 * 
	 * @param g:
	 *            Graph
	 * @param src:
	 *            Vertex - Source
	 */
	public static void shortestPath(Graph g, Vertex src) {
		ArrayDeque<Vertex> topOrder = new ArrayDeque<>();
		LinkedList<Edge> cycle = new LinkedList<>();
		// if Graph has equal positive edge weights
		if (g.uniformW && g.nonNegative) {
			BFS(g, src);
			System.out.print("BFS ");
		}
		// if the Graph is DAG
		else if (Graph.DFSVisit(src, topOrder, false, true, cycle)) {
			DAG(g, src, topOrder);
			System.out.print("DAG ");
		}
		// if the Graph has no negative edges
		else if (g.nonNegative) {
			dijkstraShortestPath(g, src);
			System.out.print("Dij ");
		}
		// if the Graph has a cycle and negative edges but no negative cycles
		else {
			if (bellmanFord(g, src))
				System.out.print("B-F ");
			else {
				System.out.println("Unable to solve problem. Graph has a negative cycle");
				return;
			}
		}
		sumOfShortestPaths(g);
		if (g.numNodes <= 100)
			printVertices(g);
		findShortestPaths(g, src);
	}

	/**
	 * To find all the shortest path from a given vertex
	 * 
	 * @param g:
	 *            Graph
	 * @param src:
	 *            Vertex - Source
	 */
	public static void findShortestPaths(Graph g, Vertex src) {
		Vertex v;

		// finding G' that has edges with v.d = u.d+e.weight
		for (Vertex u : g) {
			u.color = Vertex.Color.WHITE;
			for (Edge e : u.Adj) {
				v = e.otherEnd(u);
				if (v.distance != u.distance + e.Weight)
					e.edgeValid = false;
			}
		}

		ArrayDeque<Vertex> stack = new ArrayDeque<>();
		LinkedList<Edge> cycle = new LinkedList<>();
		// find if G' is DAG or not
		if (!Graph.DFSVisit(src, stack, false, true, cycle)) {
			System.out.println("Non-positive cycle in graph. DAC is not applicable");
			System.out.println(cycle);
			// if G' is not a DAG, find and print the cycle
			Graph.findCycle(cycle);
			printNegCycle(cycle);
			return;
		}

		// if D' is a DAG, find number of possible shortest paths from source to
		// each vertex and print them
		src.numOfShortestPaths = 1;
		for (Vertex u : stack) {
			for (Edge e : u.revAdj) {
				if (!e.edgeValid)
					continue;
				v = e.otherEnd(u);
				// number of shortest paths to a vertex = sum of number of
				// shortest paths of the vertices with the edges coming into it
				u.numOfShortestPaths += v.numOfShortestPaths;
			}
			g.sumOfNumOfPaths += u.numOfShortestPaths;
		}

		System.out.println(g.sumOfNumOfPaths);
		if (g.numNodes <= 100)
			printShortPaths(g);
	}

	/**
	 * To print the number of the shortest path to each vertex from the source vertex
	 * @param g
	 */
	public static void printShortPaths(Graph g) {
		for (Vertex u : g) {
			if (u.distance != Integer.MAX_VALUE)
				System.out.println(u.name + " " + u.distance + " " + u.numOfShortestPaths);
			else
				System.out.println(u.name + " INF 0");
		}
	}

	/**
	 * 
	 * @param cycle
	 */
	public static void printNegCycle(LinkedList<Edge> cycle) {
		for (Edge e : cycle) {
			System.out.println(e.From + " " + e.To + " " + e.Weight);
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		// if (args.length > 0) {
		// File input = new File(args[0]);
		// in = new Scanner(input);
		// } else {
		// in = new Scanner(System.in);
		// }

		File input = new File("no30.txt");
		in = new Scanner(input);

		Timer t = new Timer();
		Graph g = Graph.readGraph(in, true);
		t.start();
		shortestPath(g, g.verts.get(1));
		t.end();
		System.out.println(t);
	}
}
