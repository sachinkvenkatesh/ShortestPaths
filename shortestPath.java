import java.util.LinkedList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class shortestPath {

	public static void initialize(Graph g, Vertex src) {
		for (Vertex u : g) {
			u.distance = Integer.MAX_VALUE;
			u.parent = null;
			u.seen = false;
			u.count = 0;
		}
		src.distance = 0;
	}

	public static boolean relax(Vertex u, Vertex v, Edge e) {
		if (v.distance > u.distance + e.Weight) {
			v.distance = u.distance + e.Weight;
			v.parent = u;
			return true;
		}
		return false;
	}

	/**
	 * Uniform weights, directed/undirected, cyclic/acyclic, only non-negative
	 * edges
	 * 
	 * @param g
	 * @param src
	 */
	public static void BFS(Graph g, Vertex src) {
		Queue<Vertex> queue = new LinkedList<>();
		initialize(g, src);
		queue.add(src);
		src.seen = true;
		Vertex u, v;
		while (!queue.isEmpty()) {
			u = queue.remove();
			for (Edge e : u.Adj) {
				v = e.otherEnd(u);
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
	 * non-uniform weights, cyclic/acyclic graphs, directed graphs, only
	 * non-negative edges
	 * 
	 * @param g
	 * @param src
	 */
	public static void dijkstraShortestPath(Graph g, Vertex src) {
		initialize(g, src);
		IndexedHeap<Vertex> heap = new IndexedHeap<>(g.verts.toArray(new Vertex[g.verts.size()]), new Vertex());
		Vertex u, v;
		while (!heap.isEmpty()) {
			u = heap.remove();
			u.seen = true;
			for (Edge e : u.Adj) {
				v = e.otherEnd(u);
				if (!v.seen)
					if (relax(u, v, e))
						heap.decreaseKey(v);
			}
		}
	}

	/**
	 * Can have negative edges but not negative cycles. It must be acyclic
	 * 
	 * @param g
	 * @param src
	 */
	public static void DAG(Graph g, Vertex src, ArrayDeque<Vertex> topOrder) {
		// ArrayDeque<Vertex> topOrder = new ArrayDeque<>();
		// Graph.DFSVisit(src, topOrder, false, true);
		// if (topOrder == null)
		//// return;
		// System.out.println(topOrder);
		// while (topOrder.peek() != src)
		// topOrder.pop();
		Vertex u, v;
		initialize(g, src);
		while (!topOrder.isEmpty()) {
			u = topOrder.pop();
			for (Edge e : u.Adj) {
				v = e.otherEnd(u);
				relax(u, v, e);
			}
		}
	}

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

	public static void sumOfShortestPaths(Graph g) {
		int sumWShortestPaths = 0;
		for (Vertex u : g) {
			if (u.parent != null)
				sumWShortestPaths += u.distance;
		}

		System.out.print(sumWShortestPaths);
		System.out.println();
	}

	public static void shortestPath(Graph g, Vertex src) {
		ArrayDeque<Vertex> topOrder = new ArrayDeque<>();

		if (g.uniformW && g.nonNegative) {
			BFS(g, src);
			System.out.print("BFS ");
		} else if (Graph.DFSVisit(src, topOrder, false, true)) {
			// System.out.println(topOrder);
			DAG(g, src, topOrder);
			System.out.print("DAG ");
		} else if (g.nonNegative) {
			dijkstraShortestPath(g, src);
			System.out.print("Dij ");
		} else {
			if (bellmanFord(g, src))
				System.out.print("B-F ");
			else
				System.out.println("Unable to solve problem. Graph has a negative cycle");
		}
	}

	public static void findShortestPaths(Graph g, Vertex src) {
		Vertex v;
		for (Vertex u : g) {
			u.color = Vertex.Color.WHITE;
			for (Edge e : u.Adj) {
				v = e.otherEnd(u);
				if (v.distance != u.distance + e.Weight)
					e.edgeValid = false;
			}
		}

		ArrayDeque<Vertex> stack = new ArrayDeque<>();
		if (!Graph.DFSVisit(src, stack, false, true)) {
			System.out.println("Non-positive cycle in graph. DAC is not applicable");
			return;
		}

		int sumOfNumOfPaths = 0;
		src.numOfShortestPaths = 1;
		for (Vertex u : stack) {
			for (Edge e : u.revAdj) {
				if (!e.edgeValid)
					continue;
				v = e.otherEnd(u);
				u.numOfShortestPaths += v.numOfShortestPaths;
			}
			sumOfNumOfPaths += u.numOfShortestPaths;
		}

		System.out.println("Level2");
		System.out.println(sumOfNumOfPaths);
		for (Vertex u : g) {
			if (u.distance != Integer.MAX_VALUE)
				if (u.parent != null)
					System.out.println(u.name + " " + u.distance + " " + u.numOfShortestPaths);
				else
					System.out.println(u.name + " " + u.distance + " -");
			else
				System.out.println(u.name + " INF 0");
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

		File input = new File("lp3_level2.txt");
		in = new Scanner(input);

		Graph g = Graph.readGraph(in, true);
		shortestPath(g, g.verts.get(1));
		sumOfShortestPaths(g);
		printVertices(g);
		findShortestPaths(g, g.verts.get(1));
	}
}
