
// Ver 1.0:  Wed, Feb 3.  Initial description.
// Ver 1.1:  Thu, Feb 11.  Simplified Index interface

import java.util.Comparator;

public class IndexedHeap<T extends Index> extends BinaryHeap<T> {
	/** Build a priority queue with a given array q */
	IndexedHeap(T[] q, Comparator<T> comp) {
		super(q, comp);
		setIndex();
	}

	/** Create an empty priority queue of given maximum size */
	IndexedHeap(int n, Comparator<T> comp) {
		super(n, comp);
		setIndex();
	}

	/** restore heap order property after the priority of x has decreased */
	void decreaseKey(T x) {
		percolateUp(x.getIndex());
	}
	
	@Override
	public void assign(int index, T value)
	{
		super.assign(index, value);
		value.putIndex(index);
	}

	public void setIndex() {
		for (int i = 1; i <= size; i++)
			pq[i].putIndex(i);
	}
}
