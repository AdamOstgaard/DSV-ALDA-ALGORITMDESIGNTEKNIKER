
/**
 * A node in a {@link SkipList}
 */
public class SkipListNode<T extends Comparable<T>> {
    /**
     * References to possible jumps to higher values in the list.
     * The higher index in the array the longer the jump.
     */
    private SkipListNode<T>[] skipLinksForwards;

    private SkipListNode<T>[] skipLinksBackwards;

    /**
     * The owning {@link SkipList}
     */
    private SkipList<T> owner;

    private int height;

    private T data;

    /**
     * Creates a new {@link SkipListNode}.
     * @param item the data contained in the node.
     * @param height the number of possible skiplinks.
     */
    protected SkipListNode(T item, int height) {
        this.skipLinksForwards = new SkipListNode[height];
        this.skipLinksBackwards = new SkipListNode[height];
        this.data = item;
        this.height = height;
    }
    
    /**
     * @return the owning {@link SkipList}
     */
    public SkipList<T> getOwner() {
        return owner;
    }

    /**
     * @return the data of the node.
     */ 
    public T getData() {
        return data;
    }

    /**
     * @return the zero-based height of the node 
     */
    public int getHeight() {
        return height - 1;
    }
    
    /**
     * Adds the node to the provided {@link SkipList} and builds its skipreferences
    * @param previousNode the node of which the new node should be placed after. Should be null if the node is the first node in the list.
    * @param nextNode the node after the new node. Should be null if the node is the last one in the list.
    * @param owner the owning {link SkipList}
    */
    protected void addToList(SkipListNode<T> previousNode, SkipListNode<T> nextNode, SkipList<T> owner) {
        this.owner = owner;
        buildNodes(previousNode, nextNode);
    }
    
    /**
     * Follows the skiplink at the proivided level.
     * @param skiplevel the level at which the jump will occur
     */
    private SkipListNode<T> getNextNode(int skipLevel) {
        return skipLinksForwards[skipLevel];
    }

    
    /**
     * Follows the backwards skiplink at the proivided level.
     * @param skiplevel the level at which the jump will occur
     */
    private SkipListNode<T> getPreviousNode(int skipLevel) {
        return skipLinksBackwards[skipLevel];
    }

    /**
     * gets the next element at base level in the list.
     */
    public SkipListNode<T> getNextNode() {
        return getNextNode(0);
    }

    
    /**
     * gets the previous element at base level in the list.
     */
    public SkipListNode<T> getPreviousNode() {
        return getPreviousNode(0);
    }

    /**
     * Jumps as far as possible without passing the data provided.
     * @param data the data to be used to compare against. 
     * @return the {@link SkipListNode} furthest away or null if no movement is viable.
     */
    protected SkipListNode<T> greedyJump(T data) {
        for (int i = getHeight(); i >= 0; i--) {
            SkipListNode<T> tempNode = getNextNode(i);
            if (tempNode != null && tempNode.getData().compareTo(data) <= 0) {
                return tempNode;
            }
        }
        return null;
    }


    /**
     * Jumps as far backwards from this node as possible without passing the data in the list.
     * @param data the data to be used to compare against. 
     * @return the element furthest away or null if no movement is viable.
     */
    protected SkipListNode<T> greedyBackwardsJump(T data) {
        for (int i = getHeight(); i >= 0; i--) {
            SkipListNode<T> tempNode = getPreviousNode(i);
            if (tempNode != null && tempNode.getData().compareTo(data) <= 0) {
                return tempNode;
            }
        }
        return null;
    }

    /**
     * Makes the longest possible jump.
     * @return the element furthest away or null if no movement is viable.
     */
    protected SkipListNode<T> greedyBackwardsJump() {
        for (int i = getHeight(); i >= 0; i--) {
            SkipListNode<T> tempNode = getPreviousNode(i);
            if (tempNode != null) {
                return tempNode;
            }
        }
        return null;
    }

     /**
     * Makes the longest possible jump.
     * @return the highest furthest away node from this this node.
     */
    protected SkipListNode<T> greedyJump() {
        for (int i = getHeight(); i >= 0; i--) {
            SkipListNode<T> tempNode = getNextNode(i);
            if (tempNode != null) {
                return tempNode;
            }
        }
        return null;
    }

    /**
     * Builds the jumplist for the node.
     * @param referenceNode the {@link SkipListNode} which the new element will be placed after. 
     * Can be null if there are no other elements in the list.
     */
    private void buildNodes(SkipListNode<T> previousNode, SkipListNode<T> nextNode) {
        rebuildPreviousNodeLinks(previousNode);
        rebuildFollowingNodeList(nextNode);
    }

    /**
     * Rebuilds the references for all backwards skiplinks for this node.
     * @param referenceNode the node of which the new node should be placed before. Should be null if the node is the first node in the list.
     */
    protected void rebuildPreviousNodeLinks(SkipListNode<T> referenceNode) {
        // this node is the first node in the list
        if(referenceNode == null){
            skipLinksBackwards = new SkipListNode[height];
            return;
        }
        int previousMaxHight = 0;

        for (int i = 0; i <= Math.min(referenceNode.getHeight(), getHeight()); i++) {
            referenceNode.skipLinksForwards[i] = this;
            skipLinksBackwards[i] = referenceNode;
            previousMaxHight++;
        }

        SkipListNode<T> tempNode = referenceNode.greedyBackwardsJump();

        for (int i = previousMaxHight; i <= getHeight();) {
            if (tempNode == null) {
                // There are no more links to be made. 
                break;
            }

            if (tempNode.getHeight() >= i) {
                tempNode.skipLinksForwards[i] = this;
                skipLinksBackwards[i] = tempNode;
                i++;
            } else {
                tempNode = tempNode.greedyBackwardsJump();
            }
        }
    }
    
    /**
     * Rebuilds the references for all following skiplinks for this node.
     * @param referenceNode reference to the node which this node should be placed before or null if this is the last node in the list.  
     */
    protected void rebuildFollowingNodeList(SkipListNode<T> referenceNode) {  
        // this node is the last node in the list, remove all links!
        if(referenceNode == null){
            skipLinksForwards = new SkipListNode[height];
            return;
        }

        int previousMaxHight = 0;

        //add references to the reference node
        for (int i = 0; i <= Math.min(referenceNode.getHeight(), getHeight()); i++) {
            referenceNode.skipLinksBackwards[i] = this;
            skipLinksForwards[i] = referenceNode;
            previousMaxHight++;
        }
 
        SkipListNode<T> tempNode = referenceNode.greedyJump();

        for (int i = previousMaxHight; i <= getHeight();) {
            if (tempNode == null) {
                // There are no more links to be made. 
                break;
            }

            if (tempNode.getHeight() >= i) {
                tempNode.skipLinksBackwards[i] = this;
                skipLinksForwards[i] = tempNode;
                i++;
            } else {
                tempNode = tempNode.greedyJump();
            }
        }
    }

    //#region Debugging 
    @Override
    public String toString() {
        return "-----------\nData: " + this.data.toString() + "\n" + "height: " + height + "\n" + 
                 getSkipLinksString() + (getNextNode() != null ? getNextNode().toString() : "") ;
    }

    /**
     * Returns a debugging string
     */
    private String getSkipLinksString() {
        String s = "Can jump forward to: \n ";
        for (SkipListNode<T> var : skipLinksForwards) {
            if (var == null) {
                continue;
            }
            s += "\tData: " + var.getData().toString() + " height: " + (var.getHeight() + 1) + " \n";
        }

        s += "Can jump backwards to: \n";
        for (SkipListNode<T> var : skipLinksBackwards) {
            if (var == null) {
                continue;
            }
            s += "\tData: " + var.getData().toString() + " height: " + (var.getHeight() + 1) + " \n";
        }
        return s;
    }
    //#endregion Debugging
}