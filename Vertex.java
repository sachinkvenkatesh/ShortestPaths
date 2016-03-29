
/**
 * Class to represent a vertex of a graph
 * 
 *
 */

import java.util.*;

public class Vertex implements Index, Comparator<Vertex> {
	public enum Color {
		WHITE, GRAY, BLACK
	}

	public int name; // name of the vertex
	public boolean seen; // flag to check if the vertex has already been visited
	public Vertex parent; // parent of the vertex
	public int distance; // distance to the vertex from the source vertex
	public List<Edge> Adj, revAdj; // adjacency list; use LinkedList or
									// ArrayList
	public int index;
	public Color color;
	public int count;

	/**
	 * Constructor for the vertex
	 * 
	 * @param n
	 *            : int - name of the vertex
	 */
	Vertex(int n) {
		name = n;
		seen = false;
		parent = null;
		Adj = new ArrayList<Edge>();
		revAdj = new ArrayList<Edge>(); /* only for directed graphs */
		index = 0;
		color = Color.WHITE;
		count = 0;
	}

	Vertex() {

	}

	/**
	 * Method to represent a vertex by its name
	 */
	public String toString() {
		return Integer.toString(name);
	}

	@Override
	public void putIndex(int index) {
		this.index = index;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public int compare(Vertex o1, Vertex o2) {
		return o1.distance - o2.distance;
	}
}
