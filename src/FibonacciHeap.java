/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap{

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
    	HeapNode nNode = new HeapNode(key);
        if (isEmpty()) { // the heap is empty: first and min are null
            this.min = nNode;
            this.first = nNode;
        }
        else { // the heap is not empty
            addNewRoot(nNode);
            if (min.key>nNode.key) // check if the new node has the smallest key
                min = nNode;
        }
        numTrees++;
        size++;
        return nNode;
    } // complexity - O(1) we are changing a constant number of pointers

    /**
     * public void addNewNode(HeapNode node)
     * adds a node as a new tree - new root
     * complexity - O(1) changes a constant number of pointers and checks if the min changed
     * @param node - the node to be inserted as a new node to a current heap(not empty)
     */
    public void addNewRoot(HeapNode node){
        HeapNode second = first;
        node.prev = second.prev;
        node.next = second;
        second.prev = node;
        this.first = node;
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
        if (!this.isEmpty()) {
            if (this.size == 1) { //the deleted node is the only node
                this.first = null;
                this.min = null;
                this.size = 0;
                this.numTrees = 0;
                this.numMark = 0;
            }
            else {
                int minRank = this.min.rank;

                if (this.min.rank == 0) { //the deleted node has no children
                    connectBrothers(this.min.prev,this.min.next);
                }
                else {
                    HeapNode lastBrother = this.min.child.prev; //last child
                    int changeMark = updateParent(lastBrother);
                    this.numMark = this.numMark - changeMark;
                    connectBrothers(this.min.prev,this.min.child);
                    connectBrothers(lastBrother,this.min.next);
                }

                //updating the first node pointer if the minimum node was also the first one
                if (this.min == this.first && this.numTrees > 1) {
                    this.first = this.min.next; // ??? his child ot the next?
                }
                if (this.min == this.first && this.numTrees == 1) {
                    this.first = this.first.child;
                }

                this.size --;
                this.numTrees = this.numTrees + (minRank - 1); //adding the min children to numTree
                this.min = null;
                this.consolidate(); //W.C: O(n) ; amortized: O(log n)
            }
        }
    }

    /**
     * public int updateParent(HeapNode node)
     *
     * Changes the parent of the node and its sibling to be null
     * and changes their marked status if necessary.
     *
     * Returns the number of marks that have changed.
     */
    public int updateParent(HeapNode node) { // O(log n)
        int cnt = 0;
        HeapNode curr = node;
        do {
            curr.setParent(null);
            if (curr.mark) {
                curr.setMark(false);
                cnt++;
            }
            curr = curr.next;
        } while (curr != node);
        return cnt;
    }

    /**
     * public void consolidate()
     *
     * Creates an array which we will use in order to have
     * only one tree for each rank in our fibonacci heap.
     *
     */
    public void consolidate() { //W.C: O(n) ; amortized: O(log n)
        HeapNode[] buckets = new HeapNode[(int)((Math.log(this.size)) / (Math.log(2))) + 2];
        HeapNode curr = this.first;

        do{
            HeapNode currNext = curr.next;
            curr.setPrev(null); //disconnecting curr from the list
            curr.setNext(null);

            if (buckets[curr.rank] == null) { //the relevant bucket is empty
                buckets[curr.rank] = curr;
            }
            else { //the relevant bucket isn't empty
                int currRank = curr.rank;
                HeapNode temp = link(curr, buckets[currRank]); //connect the 2 nodes with the same rank
                buckets[currRank] = null; //clear current node from the array

                while (buckets[temp.rank] != null) {
                    HeapNode newTemp = buckets[temp.rank];
                    buckets[temp.rank] = null; //clear current node from the array
                    temp = link(temp, newTemp); //connect the 2 nodes with the same rank
                }
                buckets[temp.rank] = temp;
            }
            curr = currNext;
        } while (curr != this.first);
        this.connectArray(buckets);// O(log n)
    }

    /**
     * public HeapNode link (HeapNode a, HeapNode b)
     *
     * Connect two heaps, one heap becomes the child of the root of the other heap.
     *
     * Returns the root of the new linked heap.
     *
     */
    public HeapNode link (HeapNode a, HeapNode b) {
        if (a.key > b.key) {
            HeapNode temp = a;
            a = b;
            b = temp;
        }
        if (a.getChild() != null) { //if a has a child b will be his new child
            HeapNode oldPrev = a.child.prev;
            b.setNext(a.child);
            a.child.setPrev(b);

            b.setPrev(oldPrev);
            oldPrev.setNext(b);
        }
        else { //connecting b's next and prev pointers to himself
            b.setNext(b);
            b.setPrev(b);
        }

        b.setParent(a);
        a.setChild(b);
        numLinks++;
        a.setRank(a.rank + 1);
        this.numTrees--;
        return a;
    }

    /**
     * public void connectArray(HeapNode[] roots)
     *
     * Builds the heap from the array we got from consolidating() func.
     *
     */
    public void connectArray(HeapNode[] roots) {
        this.numTrees = 0;
        HeapNode prev = null;

        for (HeapNode root : roots) {
            if (root != null) {
                if (prev == null) {
                    this.first = root;
                    this.min = root;
                    prev = this.first;
                } else {
                    connectBrothers(prev, root);
                    if (root.key < this.min.key) {
                        this.min = root;
                    }
                    prev = root;
                }
                this.numTrees++;
            }
        }
        connectBrothers(prev, this.first);
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
    public void meld (FibonacciHeap heap2){
        if (!heap2.isEmpty()) {
            if (this.isEmpty()) {
                this.first = heap2.first;
                this.min = heap2.min;
                this.size = heap2.size;
                this.numMark = heap2.numMark;
                this.numTrees = heap2.numTrees;
            }

            else { //both heaps are not empty
                HeapNode lastHeap = this.first.prev;
                HeapNode lastHeap2 = heap2.first.prev;
                connectBrothers(lastHeap, heap2.first);
                connectBrothers(lastHeap2, this.first);
                if (this.min.key > heap2.min.key) {
                    this.min = heap2.min;
                }
                this.size += heap2.size;
                this.numMark += heap2.numMark;
                this.numTrees += heap2.numTrees;
            }
        }
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
    public int[] countersRep() {
        if (isEmpty()){ // empty heap
            return new int[0];
        }
        int[] arr = new int[maxRank()+1];
        HeapNode node = this.first;
        arr[node.rank]++;
        node = node.next;
        while (node != first){
            arr[node.rank]++;
            node = node.next;
        }
        return arr;
    }

    /**
     *
     *
     */
    public int maxRank(){ // assume the heap is not empty
        HeapNode max = this.first;
        HeapNode node = this.first.next;
        while (node != first){
            if(max.rank < node.rank)
                max = node;
        }
        return max.rank;
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
        decreaseKey(x, x.key + Integer.MAX_VALUE); //O(logn)
        this.deleteMin();
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta)
    {
        x.setKey(x.key - delta);
        if (x.parent == null){ // X is a root then only update min if needed
            if (min.key > x.key)
                min = x;
            return;
        }
        if (x.parent.key > x.key){ // x is not root and there is  a violation of the heap
            cascadingCuts(x, x.parent);
        }
    }

    /**
     *
     */
    public void cascadingCuts(HeapNode x,HeapNode y){
        cut(x,y);
        numCuts++;
        if (y.parent != null) {
            if (!y.getMark()) {
                y.setMark(true);
                numMark++;
            }
            else{
                cascadingCuts(y,y.parent);
            }
        }
    }

    /**
     *
     */
    public void cut(HeapNode x,HeapNode y){
        x.parent = null;
        y.rank = y.rank-1;
        if (x.mark){
            x.mark = false; // x is going to be a root now
            numMark--; //
        }
        if (x.next == x){
            y.child = null;
        }
        else{
            y.child = x.next;
            x.prev.next = x.next;
            x.next.prev = x.prev;
        }
        addNewRoot(x); // adds x as a new root to the heap (left side of the heap)
        if (x.key < min.key)
            min = x;
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
        return this.numTrees + (2 * this.numMark);
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
        return numLinks;
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
        return numCuts;
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
        int[] arr = new int[k];
        if (k==0 || H.isEmpty()) return arr;
        FibonacciHeap heapToArray = new FibonacciHeap();
        HeapNode currMin = H.findMin();
        HeapNode childNode, newNode, tmpMin;
        arr[0] = currMin.getKey();
        for (int i=1;i<k;i++){
            childNode = currMin.getChild();
            for (int j=0; j < currMin.getRank(); j++){ // adds all the next layer of children
                newNode = heapToArray.insert(childNode.getKey());
                newNode.setReferenceNode(childNode); // creates reference to the original fib tree
                childNode = childNode.getNext();
            }
            tmpMin = heapToArray.findMin(); // the min in the new heap
            arr[i] = tmpMin.getKey();
            currMin = tmpMin.getReferenceNode(); // the original node of the curr min
            heapToArray.deleteMin();// creates a legal binomial heap, keeps time complexity as it needed
        }
        return arr;
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
        private HeapNode child, next, prev, parent, referenceNode;

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

        public HeapNode getReferenceNode(){ return this.referenceNode; }

        public void setReferenceNode(HeapNode node){ this.referenceNode = node; }
    }
}
