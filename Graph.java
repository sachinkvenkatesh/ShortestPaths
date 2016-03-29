
/**
 * Class to represent a graph
 * 
 *
 */

import java.io.*;
import java.util.*;

class Graph implements Iterable<Vertex> {
	public List<Vertex> verts; // array of vertices
	public int numNodes; // number of verices in the graph
	public boolean nonNegative;
	public boolean uniformW;
	public int sumOfNumOfPaths;

	/**
	 * Constructor for Graph
	 * 
	 * @param size
	 *            : int - number of vertices
	 */
	Graph(int size) {
		numNodes = size;
		verts = new ArrayList<>(size + 1);
		verts.add(0, null);
		nonNegative = true;
		uniformW = true;
		sumOfNumOfPaths = 0;
		// create an array of Vertex objects
		for (int i = 1; i <= size; i++)
			verts.add(i, new Vertex(i));
	}

	/**
	 * Method to add an edge to the graph
	 * 
	 * @param a
	 *            : int - one end of edge
	 * @param b
	 *            : int - other end of edge
	 * @param weight
	 *            : int - the weight of the edge
	 */
	void addEdge(int a, int b, int weight) {
		Vertex u = verts.get(a);
		Vertex v = verts.get(b);
		Edge e = new Edge(u, v, weight);
		u.Adj.add(e);
		v.Adj.add(e);
	}

	/**
	 * Method to add an arc (directed edge) to the graph
	 * 
	 * @param a
	 *            : int - the head of the arc
	 * @param b
	 *            : int - the tail of the arc
	 * @param weight
	 *            : int - the weight of the arc
	 */
	void addDirectedEdge(int a, int b, int weight) {
		Vertex head = verts.get(a);
		Vertex tail = verts.get(b);
		Edge e = new Edge(head, tail, weight);
		head.Adj.add(e);
		tail.revAdj.add(e);
	}

	/**
	 * Method to create an instance of VertexIterator
	 */
	public Iterator<Vertex> iterator() {
		return new VertexIterator();
	}

	/**
	 * A Custom Iterator Class for iterating through the vertices in a graph
	 * 
	 *
	 * @param <Vertex>
	 */
	private class VertexIterator implements Iterator<Vertex> {
		private Iterator<Vertex> it;

		/**
		 * Constructor for VertexIterator
		 * 
		 * @param v
		 *            : Array of vertices
		 * @param n
		 *            : int - Size of the graph
		 */
		private VertexIterator() {
			it = verts.iterator();
			it.next(); // Index 0 is not used. Skip it.
		}

		/**
		 * Method to check if there is any vertex left in the iteration
		 * Overrides the default hasNext() method of Iterator Class
		 */
		public boolean hasNext() {
			return it.hasNext();
		}

		/**
		 * Method to return the next Vertex object in the iteration Overrides
		 * the default next() method of Iterator Class
		 */
		public Vertex next() {
			return it.next();
		}

		/**
		 * Throws an error if a vertex is attempted to be removed
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public static Graph readGraph(Scanner in, boolean directed) {
		// read the graph related parameters
		int n = in.nextInt(); // number of vertices in the graph
		int m = in.nextInt(); // number of edges in the graph
		boolean flag = true;
		// create a graph instance
		Graph g = new Graph(n);
		int prevW = 0;
		for (int i = 0; i < m; i++) {
			int u = in.nextInt();
			int v = in.nextInt();
			int w = in.nextInt();
			if (flag) {
				prevW = w;
				flag = false;
			}
			if (w < 0)
				g.nonNegative = false;
			if (prevW != w)
				g.uniformW = false;
			if (directed) {
				g.addDirectedEdge(u, v, w);
			} else {
				g.addEdge(u, v, w);
			}
			prevW = w;
		}
		in.close();
		return g;
	}

	/**
	 * Method for Topological order using DFS
	 * 
	 * @param g:
	 *            Graph
	 * @return:ArrayDeque - vertices in topological order
	 */
	static ArrayDeque<Vertex> topologicalOrder(Graph g) {
		// true - if graph is undirected or no cycle detection in directed
		// graphs
		// false - if cycle has to be detected in directed graphs
		return DFSTop(g, false, true);
	}

	/**
	 * Method for Topological order using DFS
	 * 
	 * @param g:
	 *            Graph
	 * @param isCycleAllowed:
	 *            Boolean - to enable cycle detection
	 * @return: ArrayDeque - vertices in topological order
	 */
	static ArrayDeque<Vertex> DFSTop(Graph g, boolean isCycleAllowed, boolean isDirected) {
		ArrayDeque<Vertex> stack = new ArrayDeque<>();
		LinkedList<Edge> cycle = new LinkedList<>();
		boolean cycleExist = false;

		for (Vertex v : g)
			v.color = Vertex.Color.WHITE;

		for (Vertex v : g) {
			if (v.color == Vertex.Color.WHITE) {
				cycleExist = DFSVisit(v, stack, isCycleAllowed, isDirected, cycle);
				if (!cycleExist)
					break;
			}
		}
		// if (stack.size() == g.numNodes)
		if (cycleExist && stack.size() == g.numNodes)
			return stack;
		else
			return null;
	}

	/**
	 * Private method for DFS
	 * 
	 * @param u:
	 *            Vertex - DFS on this vertex
	 * @param stack:
	 *            ArrayDeque<Vertex> - to store the topological order of
	 *            vertices
	 * @param isCycleAllowed:
	 *            boolean - true if cycle can exist in the given graph, else
	 *            false
	 * @param isDirected:
	 *            boolean - true if the graph is directed, else false
	 * @param cycle:
	 *            LinkedList<Edge> - to store the cycle, if there is no DAG
	 * 
	 * @throws CyclicGraphException
	 *             exception to be thrown when cycle is encountered in the graph
	 */
	public static boolean DFSVisit(Vertex u, ArrayDeque<Vertex> stack, boolean isCycleAllowed, boolean isDirected,
			LinkedList<Edge> cycle) {
		u.color = Vertex.Color.GRAY; // vertex being processed
		for (Edge e : u.Adj) {
			if (!e.edgeValid)
				continue;
			Vertex v = e.otherEnd(u);
			if (v.color == Vertex.Color.WHITE) {
				v.parent = u;

				if (!DFSVisit(v, stack, isCycleAllowed, isDirected, cycle)) {
					cycle.add(e);
					return false;
				}
			} else if (!isCycleAllowed
					&& ((isDirected && v.color == Vertex.Color.GRAY) || (!isDirected && v != u.parent))) {
				// To detect cycle and add those edges into the list
				cycle.add(e);
				return false;
			}
		}

		u.color = Vertex.Color.BLACK;// vertex processed
		stack.push(u);
		return true;
	}

	/**
	 * To find the cycle in the current Graph
	 * 
	 * @param cycle
	 */
	public static void findCycle(LinkedList<Edge> cycle) {
		boolean remove = false;
		Vertex first;
		Edge e;
		Iterator<Edge> itr = cycle.iterator();
		// get the first edge, the edge.To vertex is where the cycle starts
		if (itr.hasNext())
			first = itr.next().To;
		else
			return;
		// to get the cycle - go till the edge having e.To == the vertex
		// obtained above i.e start and end are same vertex and hence a cycle.
		// Remove all other edges thereafter(Including the edge with e.To == the
		// start vertex)
		while (itr.hasNext()) {
			e = itr.next();
			if (e.To.name == first.name)
				remove = true;
			if (remove)
				itr.remove();
		}
	}
}
