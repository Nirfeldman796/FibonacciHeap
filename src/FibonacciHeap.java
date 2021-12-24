/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{

    private HeapNode first, min;
    private int size, numMark, numTrees;
    static int numLinks, numCuts;

   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *   
    */
    public boolean isEmpty() {
    	return this.first == null;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    */
    public HeapNode insert(int key)
    {    
    	return new HeapNode(key); // should be replaced by student code
    }

    /**
     * private void connectBrothers(HeapNode a, HeapNode b)
     *
     * Gets two nodes (a and b) and connects between them.
     *
     */

    private void connectBrothers (HeapNode a, HeapNode b) { //O(1)
        a.setNext(b);
        b.setPrev(a);
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    */
    public void deleteMin() {
        return; // should be replaced by student code
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    */
    public HeapNode findMin() { return this.min; }
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	  return; // should be replaced by student code   		
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    *   
    */
    public int size()
    {
    	return this.size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    * 
    */
    public int[] countersRep()
    {
    	int[] arr = new int[100];
        return arr; //	 to be replaced by student code
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    */
    public void delete(HeapNode x) 
    {    
    	return; // should be replaced by student code
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	return; // should be replaced by student code
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return -234; // should be replaced by student code
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    */
    public static int totalLinks()
    {    
    	return -345; // should be replaced by student code
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return -456; // should be replaced by student code
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        int[] arr = new int[100];
        return arr; // should be replaced by student code
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	public int key;
        public HeapNode info;
        private int rank;
        private boolean mark;
        private HeapNode child, next, prev, parent;

    	public HeapNode(int key) {
    	    this.key = key;
            this.rank = 0;
            this.mark = false;
            this.next = this;
            this.prev = this;
    	}

    	public int getKey() {
    		return this.key;
    	}

        public void setKey(int key) {
           this.key = key;
       }

        public HeapNode getInfo() {
           return this.info;
       }

        public void setInfo(HeapNode info) {
           this.info = info;
       }

        public int getRank() {
           return this.rank;
       }

        public void setRank(int rank) {
           this.rank = rank;
       }

        public boolean getMark() {
           return this.mark;
       }

        public void setMark(boolean mark) {
           this.mark = mark;
       }

        public HeapNode getChild() { return this.child; }

        public void setChild(HeapNode child) {
           this.child = child;
       }

        public HeapNode getNext() {
           return this.next;
       }

        public void setNext(HeapNode next) {
           this.next = next;
       }

        public HeapNode getPrev() {
           return this.prev;
       }

        public void setPrev(HeapNode prev) {
           this.prev = prev;
       }

        public HeapNode getParent() {
           return this.parent;
       }

        public void setParent(HeapNode parent) {
           this.parent = parent;
       }
    }
}
