import java.util.Comparator;

public class BinaryHeap<T> implements PQ<T> {
	T[] pq;
	Comparator<T> c;
	int size;

	/** Build a priority queue with a given array q */
	BinaryHeap(T[] q, Comparator<T> comp) {
		pq = q;
		c = comp;
		size = q.length - 1;
		buildHeap();
	}

	/** Create an empty priority queue of given maximum size */
	BinaryHeap(int n, Comparator<T> comp) {
		pq = (T[]) new Object[n + 1];
		c = comp;
		size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void insert(T x) {
		add(x);
	}

	public T deleteMin() {
		return remove();
	}

	public T min() {
		return peek();
	}

	public void add(T x) {
		if (pq.length - 1 == size)
			resize();

		size++;
		assign(size, x); // pq[size] = x;
		percolateUp(size);
	}

	public void resize() {
		T[] newPQ = pq;
		pq = (T[]) new Object[2 * size];
		for (int i = 0; i <= size; i++)
			pq[i] = newPQ[i];
	}

	public T remove() { /* to be implemented */
		T min;
		min = pq[1];
		assign(1, pq[size--]);// pq[1] = pq[size--];
		percolateDown(1);
		return min;
	}

	public T peek() { /* to be implemented */
		return pq[1];
	}

	/** pq[i] may violate heap order with parent */
	void percolateUp(int i) {
		assign(0, pq[i]); // pq[0] = pq[i];
		while (c.compare(pq[i / 2], pq[0]) > 0) {
			assign(i, pq[i / 2]); // pq[i] = pq[i / 2];
			i = i / 2;
		}
		assign(i, pq[0]); // pq[i] = pq[0];
	}

	/** pq[i] may violate heap order with children */
	void percolateDown(int i) {
		int child;
		T tmp = pq[i];
		for (; 2 * i <= size; i = child) {
			child = 2 * i;
			if (child != size && c.compare(pq[child + 1], pq[child]) < 0)
				child++;
			if (c.compare(pq[child], tmp) < 0)
				assign(i, pq[child]); // pq[i] = pq[child];
			else
				break;
		}
		assign(i, tmp);// pq[i] = tmp;
	}

	/** Create a heap. Precondition: none. */
	void buildHeap() {
		for (int i = size / 2; i > 0; i--)
			percolateDown(i);
	}

	/*
	 * sort array A[1..n]. A[0] is not used. Sorted order depends on comparator
	 * used to buid heap. min heap ==> descending order max heap ==> ascending
	 * order
	 */
	public static <T> void heapSort(T[] A, Comparator<T> comp) {

		BinaryHeap<T> heap = new BinaryHeap<>(A, comp);
		heap.buildHeap();
		T temp;

		for (int i = A.length - 1; i > 0; i--) {
			temp = A[i];
			A[i] = A[1];
			A[1] = temp;

			heap.size--;
			heap.percolateDown(1);
		}
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 1; i <= size; i++)
			s.append(pq[i] + " ");
		return s.toString();
	}

	/**
	 * To assign the value to a particular index. This is implemented so as to
	 * ease the implementation of the indexed heap
	 * 
	 * @param index
	 * @param value
	 */
	public void assign(int index, T value) {
		pq[index] = value;
	}

	public static void main(String args[]) {
		Comparator<Integer> cmp = new IntegerComparator();
		BinaryHeap<Integer> heap = new BinaryHeap<>(10, cmp);
		Integer[] input = { 0, 12, 52, 34, 69, 12, 45, 36, 76, 11, 52 };
		// for (int i = 0; i < input.length; i++)
		// heap.insert(input[i]);

		// System.out.println(heap.size);
		// System.out.println();
		// System.out.println(heap.pq[1]);
		// for (int i = 1; i <= heap.size; i++) {
		// System.out.println(heap.deleteMin());
		// }

		heapSort(input, cmp);
		for (int i : input)
			System.out.print(i + " ");
		// while (!heap.isEmpty())
		// System.out.print(heap.deleteMin() + " ");

		// for(Integer i:heap.pq)
		// System.out.println(i);
	}
}

class IntegerComparator implements Comparator<Integer> {

	@Override
	public int compare(Integer o1, Integer o2) {
		return (o1 - o2);
	}

}
