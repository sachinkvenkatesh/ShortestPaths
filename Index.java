// Ver 1.0:  Wed, Feb 3.  Initial description.
// Ver 1.1:  Thu, Feb 11.  Simplified description by removing T.
//                         Class implementing Index use member functions
//                         instead of static functions in Ver 1.0

public interface Index {
    public void putIndex(int index);
    public int getIndex();
}

/* Ver 1.0:
public interface Index<T> {
    public void putIndex(T e, int index);
    public int getIndex(T e);
}
*/
