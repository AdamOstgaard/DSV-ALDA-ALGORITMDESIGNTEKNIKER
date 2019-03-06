import java.util.Iterator;

public class SkipListIterator<T extends Comparable<T>> implements Iterator<T> { 
    private SkipList<T> skiplist;
    private SkipListNode<T> skiplistNode = null; 

    public SkipListIterator(SkipList<T> skiplist) {
        this.skiplist = skiplist;
    }

    @Override
    public boolean hasNext() {
        if (skiplistNode == null) {
            return skiplist.getHead() != null;
        }
        return skiplistNode.getNextNode() != null;
    }

    @Override
    public T next() {
        if (this.skiplistNode == null) {
            skiplistNode = skiplist.getHead();
        } else {
            this.skiplistNode = skiplistNode.getNextNode();
        }
        return skiplistNode.getData();
    }
}