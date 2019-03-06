import java.util.Collection;

/**
 * A sorted generic skiplist.
 */
public class SkipList<T extends Comparable<T>> {
    private SkipListNode<T> head;
    private int maxHeight;
    private int count;
    private HeightGenerator heightGenerator;

    /**
     * creates a new skiplist empty with the the default maxHeight (16).
     */
    public SkipList() {
        this(16);
    }

    /**
     * creates a new skiplist empty with the maxheight provided.
     * @param maxHeight the maxheight used when generating the linkarrays for the nodes. The higher the height the longer the possible jumps. 
     */
    public SkipList(int maxHeight) {
        this.maxHeight = maxHeight;
        heightGenerator = new HeightGenerator(maxHeight);
    }

    /**
     * creates a new skiplist with the default maxHeight (16) and adds the provided items to the list.
     * @param collection the items to be added in the initial list.
     */
    public SkipList(Collection<T> collection) {
        this();
        addRange(collection);
    }
    
    /**
     * creates a new skiplist with the maxheight provided and adds the provided items to the list.
     * @param maxHeight the maxheight used when generating the linkarrays for the nodes. The higher the height the longer the possible jumps. 
     * @param collection the items to be added in the initial list.
     */
    public SkipList(Collection<T> collection, int maxHeight) {
        this(maxHeight);
        addRange(collection);
    }

    /**
     * The numbers of element in the list.
     */
    public int getCount() {
        return count;
    }
    
    /**
     * Adds the collection of items to the list.
     * @param collection the items to be added.
     */
    public void addRange(Collection<T> collection) {
        for (T item : collection) {
            this.add(item);
        }
    }

    /**
     * Add a {@link SkipListNode} to the list at the specified index.
     * @param index The index at which the element will be placed.
     */
    public void add(T data) {
        count++;

        SkipListNode<T> nodeBefore = findLargestSmallerElement(data);
        SkipListNode<T> nodeAfter = nodeBefore == null ? head : nodeBefore.getNextNode();
        SkipListNode<T> newNode = new SkipListNode<T>(data, heightGenerator.getHeight());
        newNode.addToList(nodeBefore, nodeAfter, this);
        if (head == null || newNode.getData().compareTo(head.getData()) < 0) {
            head = newNode;
        }
    }

    /**
     * Removes the first occurance of the data in the list if there is one.
     * @param element the element to be removed
     */
    public void remove(T element) {
        SkipListNode<T> node = findNode(element);
        if (node != null) {
            count--;
        }
        removeNode(node);
    }

    /**
     * Removes the node from the list and rebuilds the links for the surrounding nodes.
     * @param node the node to be removed.
     * @throws NodeDoesNotExistException thrown if the node is not part of the list.
     */
    private void removeNode(SkipListNode<T> node) {
        SkipListNode<T> previousNode = node.getPreviousNode();
        SkipListNode<T> followingNode = node.getNextNode();

        if (previousNode == null) {
            head = followingNode;
        }
        
        previousNode.rebuildFollowingNodeList(followingNode);
        followingNode.rebuildPreviousNodeLinks(previousNode);
    }

    /**
     * Searches the skiplist for the data and returns the {@link SkipListNode} containing it, if one exists.
     * @return the element or null if the element does not exist in the list.
     */
    private SkipListNode<T> findNode(T data) {
        SkipListNode<T> tempNode = head;

        while (tempNode != null) {
            if (tempNode.getData() == data) {
                return tempNode;
            }
            tempNode = tempNode.greedyJump(data);
        }
        return null;
    }

    /**
     * Searches the skiplist for the largest element that is still smaller than the provided data. 
     * @return the {@link SkipListNode} that would be right before the data if it were a part of the list
     * @param data data to be used as comparaer when searching the list.
     */
    private SkipListNode<T> findLargestSmallerElement(T data) {
        SkipListNode<T> currentNode = head;

        if (currentNode != null && currentNode.getData().compareTo(data) > 0) {
            return null;
        }

        SkipListNode<T> prevNode = null;

        while (currentNode != null) {
            prevNode = currentNode;
            if (currentNode.getData().compareTo(data) <= 0) {
                currentNode = currentNode.greedyJump(data);
            }
        }
        return prevNode;
    }
    
    //#region Debugging
    @Override
    public String toString() {
        return "maxHeight: " + maxHeight + " Count: " + count + "\n" + head.toString();
    }
    //#endregion
}