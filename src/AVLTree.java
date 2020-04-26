import java.lang.Math;

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */
public class AVLTree {
	private IAVLNode root;
	private int size;
	private IAVLNode pseudoNode;
	private int leftRotations;
	private int rightRotations;
	
	
	/**
	 *  AVLTree constructor 
	 */
	public AVLTree() { 
		this.size = 0;
		this.root = null;
		this.pseudoNode = new AVLNode();	/* generate unique pseudo node for this tree */
		this.leftRotations = 0;
		this.rightRotations = 0;
	}

	
	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 * complexity: O(1)
	 *
	 */
	public boolean empty() {
		/* determine if empty according to tree size, implementation guarantees non-negative size for tree */
		if (this.size() == 0) {				
			return true;
		}
		
		return false;
	}

	
	/**
	 * ADDED METHOD
	 * 
	 * private IAVLNode doSearch(int k)
	 *
	 * returns the node with key k if exists in the tree
	 * returns null if tree is empty 
	 * otherwise, returns last existing node in search path
	 * 
	 * complexity: O(log(n))
	 */
	private IAVLNode doSearch(int k) {
		/* return null for empty tree */
		if (this.empty()) {
			return null;
		} 	
		
		/* set root as the node to start the search with */
		IAVLNode prevNode = null;
		IAVLNode currNode = this.root; 			
		int currKey = currNode.getKey();
		
		/* perform search by BST rules while key not found & relevant son exist 
		 * in each iteration store currNode in prevNode then set relevant son to currNode 
		 */
		while (currNode != this.pseudoNode) {		
			prevNode = currNode;					
			if (k == currKey) {						
				return currNode;
			}
			
			else if (k > currKey) {
				currNode = currNode.getRight();
			}
			
			else {
				currNode = currNode.getLeft();
			}
			
			currKey = currNode.getKey();
		}
		
		/* return last node in search path if key was not found */
		return prevNode;
		}


	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 * complexity: O(log(n))
	 */
	public String search(int k) {
		  
		/* apply doSearch with key k. if node exists return it's value, otherwise return null.
		 * precondition: all keys are distinct.
		 */ 
		IAVLNode resNode = this.doSearch(k);  
		
		if (resNode == null || resNode.getKey() != k){
			return null;
		}
		
		else {
			return resNode.getValue();
		}
	}

	
	/**
	 * ADDED METHOD
	 * private void swapSonOrRoot(AVLNode node AVLNode pivot)
	 *
	 * aid method for rotations
	 * sets parent of node as parent of pivot.
	 * sets pivot as root if node was former root,
	 * otherwise set pivot as son for node's parent in same direction instead of node
	 * 
	 * complexity: O(1)
	 */
	private void swapSonOrRoot(IAVLNode node, IAVLNode pivot) {
		pivot.setParent(node.getParent());
		
		if (node.getParent() == null) {
			this.root = pivot;
		}
		
		else {
			if(((AVLNode) node).isLeftSon()) {		
				node.getParent().setLeft(pivot);
			}
			
			else {
				node.getParent().setRight(pivot);
			}
		}
	}
  
	
	/**
	 * ADDED METHOD
	 * private void rotateR(AVLNode node)
	 *
	 * perform right rotation
	 * complexity: O(1)
	 */
	private void rotateR(IAVLNode node) {
		IAVLNode pivot = node.getLeft();				/* set pivot to be left son of rotated node */
		node.setLeft(pivot.getRight()); 				/* pivot's right subtree becomes rotated node's left subtree */
		pivot.getRight().setParent(node); 				/* node's set to be pivot's right subtree's new parent */
		
		/* node's parent becomes new pivot's parent
		 * set new tree root if needed
		 * if node was left son, replace with pivot as left son, otherwise replace with pivot as right son  
		 */
		this.swapSonOrRoot(node, pivot);
	  
		/* set node as pivot's new right subtree, set pivot as node's parent */
		pivot.setRight(node);							
		node.setParent(pivot);						
	  
		/* correct size and height for pivot and node */
		((AVLNode) node).fixSizeHeight();
		((AVLNode) pivot).fixSizeHeight();
	}
  
  
	/**
	 * ADDED METHOD
	 * private void rotateL(AVLNode node)
	 *
	 * perform left rotation
	 * complexity: O(1)
	 */
	private void rotateL(IAVLNode node) {
		IAVLNode pivot = node.getRight(); 		/* set pivot to be right son of rotated node */
		node.setRight(pivot.getLeft());			/* pivot's left subtree becomes rotated node's right subtree */
		pivot.getLeft().setParent(node); 		/* node's set to be pivot's left subtree's new parent */
	
		/* node's parent becomes new pivot's parent
		 * set new tree root if needed
		 * if node was left son, replace with pivot as left son, otherwise replace with pivot as right son
		 */
		this.swapSonOrRoot(node, pivot);
		  
		/* set node as pivot's new left subtree, set pivot as node's parent */
		pivot.setLeft(node);					
		node.setParent(pivot);				
		    
		/* correct size and height for pivot and node */
		((AVLNode) node).fixSizeHeight();
		((AVLNode) pivot).fixSizeHeight();
	}
  
  
	/**
	 * ADDED METHOD
	 * private void rotateRL(AVLNode node)
	 *
	 * perform right then left rotation
	 * complexity: O(1)
	 */
	private void rotateRL(IAVLNode node) {
		rotateR(node.getRight());
		rotateL(node);
	}

	
	/**
	 * ADDED METHOD
	 * private void rotateLR(AVLNode node)
	 *
	 * perform left then right rotation
	 * complexity: O(1)
	 */
	private void rotateLR(IAVLNode node) {
		rotateL(node.getLeft());
		rotateR(node);
	}

	
	/**
	 * ADDED METHOD
	 * private void rotate(AVLNode node, boolean isDelete)
	 *
	 * perform correct rotation according to cases learned in class
	 * cases are calculated by BF for node and node's sons
	 * isDelete parameter indicates if rotation performed as part of insertion or deletion (for correct rotations)
	 * 
	 * return number of rotations performed
	 * complexity: O(1)
	 */
	private int rotate(AVLNode node, boolean isDelete){
		int BFRight = ((AVLNode) node.getRight()).BF();
		int BFLeft = ((AVLNode) node.getLeft()).BF();
		 
		if (node.BF() == -2) {
			if (BFRight == -1 || isDelete && BFRight == 0) {
				rotateL(node);
				this.leftRotations++;
				return 1;
			}
			
			else if (BFRight == 1) {
				rotateRL(node);
				this.leftRotations++;
				this.rightRotations++;
				return 2;
			}
		}
		
		else if (node.BF() == 2) {
			if (BFLeft == -1) {
				rotateLR(node);
				this.leftRotations++;
				this.rightRotations++;
				return 2;
			}
			
			else if (BFLeft == 1 || isDelete && BFLeft == 0) {
				rotateR(node);
				this.rightRotations++;
				return 1;
			}
		}
		
		return 0;
	}
  
  
	/**
	 * ADDED METHOD
	 * private void newNodeInTree(int k, String s) {
	 *
	 * creates new node in the tree and sets all parameters
	 * increasing tree size by 1
	 * returns new created node
	 * complexity: O(1)
	 */
	private IAVLNode newNodeInTree(int k, String s) {
		  
		/* create new node with given key and value */
		Item newItem = new Item(k, s);
		IAVLNode node = new AVLNode(newItem);	
		  
		node.setLeft(this.pseudoNode);
		node.setRight(this.pseudoNode);
		
		/* tree is empty, set new node as root */
		if (this.empty()) {						
			this.root = node;
		}

		return node;
	}
  
  
	/**
	 * ADDED METHOD
	 * private void initialInsertion(IAVLNode parent, int k, String s)
	 *
	 * inserts new node to the AVL tree in the correct place, before performing rotations.
	 * complexity: O(1)
	 */
	private void initialInsertion(IAVLNode parent, int k, String s) {
		  
		/* create new node with given key and value */
		IAVLNode son = this.newNodeInTree(k, s);
		son.setParent(parent);
		  
		/* insert new node in correct place under parent */
		if (parent != null) {							
			if (parent.getKey() > son.getKey()) {		
				parent.setLeft(son);
			}
			
			else {
				parent.setRight(son);
			}
		}
		/* increase tree size */
		this.size++;
	}
  
	
	/**
	 * ADDED METHOD
	 * public int fixTree(IAVLnode node, boolean isDelete)
	 *
	 * go in path from given node to root
	 * set new height and size for each node in path
	 * perform rotations if needed
	 * isDelete parameter indicates if fixTree performed as part of insertion or deletion (for correct rotations)
	 * 
	 * return number of rotations made
	 * complexity: O(log(n))
	 */
	public int fixTree(IAVLNode node, boolean isDelete) {	  
		/* for each node in path to root
		 * set new height and size
		 * perform rotation as needed 
		 * assign # of rotations to output 
		 */
		int rotations = 0;
		IAVLNode currNode = node; 
		  
		while(currNode != null) {
			((AVLNode) currNode).fixSizeHeight();
			int tmp_rotataions = rotations;
			rotations += rotate((AVLNode)currNode, isDelete);
			
			/* after rotation currNode's parent sits in node's previous position in tree, 
			 * go up 2 levels to continue in path to root correctly 
			 */  
			if (rotations != tmp_rotataions && currNode != this.root) {
				currNode = currNode.getParent().getParent();
			}
			
			else {
				currNode = currNode.getParent();
			}
		}
		return rotations;
	}
  
  
	/**
	 * public int insert(int k, String i)
	 *
	 * inserts an item with key k and info i to the AVL tree.
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	 * returns -1 if an item with key k already exists in the tree.
	 * complexity: O(log(n))
	 */
	public int insert(int k, String i) {
		  
		/* perform search to find a node with key k if in tree, or its potential parent otherwise */
		IAVLNode resNode = this.doSearch(k); 	
	   
		/* given key already exists in tree, return -1 */ 
		if (resNode != null) {
			if(resNode.getKey() == k) {								
				return -1;
			}
		}
		
		int rotations = 0;
		this.initialInsertion(resNode, k, i);			/* insert new node in place before balancing, increase tree size by 1 */
		rotations = this.fixTree(resNode, false);		/* correct all sizes and heights in path up to root, perform balancing */ 
		
		return rotations;
	}
  
	
	/**
	 * ADDED METHOD
	 * private IAVLNode getPredecessor(IAVLNode node)
	 *
	 * returns the given node's predecessor if exits
	 * otherwise return pseudo node
	 * complexity: O(log(n))
	 */ 
	private IAVLNode getPredecessor(IAVLNode node) {
		  
		/* if left son exist, return his subtree's right-most node */ 
		if (node.getLeft() != this.pseudoNode) {
			IAVLNode currNode = node.getLeft();
			
			while (currNode.getRight() != this.pseudoNode) {
				currNode = currNode.getRight();
			}
			return currNode;
		}
	
		/* if left son doesn't exist, go up in path to root and find first left turn */
		else {
			IAVLNode currNode = node;
			
			while(((AVLNode) currNode).isLeftSon() && currNode != null) {
				currNode = currNode.getParent();
			}
			
			/* if root reached with no left turns, predecessor doesn't exist -- return pseudo node, 
			 * otherwise predecessor is first node after left turn */
			if(currNode == null) {
				return this.pseudoNode;
			}
			
			return currNode.getParent();	
		}
	}

	
	/**
	 * ADDED METHOD
	 * private IAVLNode getSuccessor(IAVLNode node)
	 *
	 * returns the given node's successor if exits
	 * otherwise return pseudo node
	 * complexity: O(log(n))
	 */ 
	private IAVLNode getSuccessor(IAVLNode node) {
	
		/* if right son exist, return his subtree's left-most node */ 
		if (node.getRight() != this.pseudoNode) {
			IAVLNode currNode = node.getRight();
			
			while (currNode.getLeft() != this.pseudoNode) {
				currNode = currNode.getLeft();
			}
			return currNode;
		}
	
		/* if right son doesn't exist, go up in path to root and find first right turn */
		else {
			IAVLNode currNode = node;
			
			while(!((AVLNode) currNode).isLeftSon() && currNode != null) {
				currNode = currNode.getParent();
			}
			
			/* if root reached with no right turns, successor doesn't exist -- return pseudo node, else successor is first node after right turn */
			if(currNode == null) {
				return this.pseudoNode;
			}
			
			return currNode.getParent();
		}
	}
  
  
	/**
	 * ADDED METHOD
	 * private void nodeByPass(IAVLNode node)
	 * 
	 * if deleted node has exactly 1 son, perform bypass (connect node's son to node's parent)
	 * complexity: O(1) 
	 */ 
	private void nodeByPass(IAVLNode node) {
		IAVLNode leftSon = node.getLeft();
		IAVLNode rightSon = node.getRight();
		IAVLNode chosenSon = null;
		  
		/* check direction of node's only son */
		if (leftSon != this.pseudoNode) {
			leftSon.setParent(node.getParent());
			chosenSon = leftSon;
		}
	
		else {
			rightSon.setParent(node.getParent());
			chosenSon = rightSon;
		}
		
		if (node == this.root) {
			this.root = chosenSon;
		}
		
		/* set chosenSon as node's parent new son in the correct direction */
		else {
			if (((AVLNode)node).isLeftSon()) {
				node.getParent().setLeft(chosenSon);
			}
			
			else {
				node.getParent().setRight(chosenSon);
			}
		}  
	}
 
	
	/**
	 * ADDED METHOD
	 * private void initialDeletion(IAVLNode node)
	 *
	 * deletes item from the AVL tree
	 * decrease all ancestors' size and height by 1
	 * do not perform balancing yet
	 * return physically deleted node's parent (return null if node was root)
	 * complexity: O(log(n)) 
	 */ 
	private IAVLNode initialDeletion(IAVLNode node) {
		IAVLNode outputNode;
		  
		/* node is a leaf -- disconnect from tree, 
		 * turn node to pseudo node (for complete isolation from tree) */
		if (node.getLeft() == this.pseudoNode && node.getRight() == this.pseudoNode) {
			if (((AVLNode) node).isLeftSon()) {
				node.getParent().setLeft(this.pseudoNode);
			}
			
			else {
				node.getParent().setRight(this.pseudoNode);
			}
	
			outputNode = node.getParent();
			node = this.pseudoNode;
		}
		  
		/* node has 2 sons, find his successor (which doesn't have left son), delete it and replace item with node's item */
		else if (node.getLeft() != this.pseudoNode && node.getRight() != this.pseudoNode) {
			IAVLNode successor = this.getSuccessor(node);
			outputNode = successor.getParent();
			((AVLNode) node).setItem(new Item(successor.getKey(),successor.getValue()));
			initialDeletion(successor);  
		}
		  
		/* node has 1 son -- assign node's parent to be this son parent,
		 * initiate fields as new node, disconnect from tree */
		else {
			nodeByPass(node);
			outputNode = node.getParent();
			node = this.pseudoNode;
		}
		
		return outputNode;
	}
  	
	/**
	 * ADDED METHOD
	 * public int delete(IAVLNode node)
	 * 
	 * this method purpose is to delete a given node (instead of deleting a node by a given key)
	 * necessary for performing deletion as part of tree list 
	 *
	 * decreases tree size by 1
	 * deletes a node from the AVL tree
	 * perform rebalancing as needed
	 * 
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	 * complexity: O(log(n))
	 */
	public int delete(IAVLNode node){
		IAVLNode fixingNode = initialDeletion(node);							
		int rotations = this.fixTree(fixingNode, true);	   
		
		if (node == this.root) {
			this.root = null;
		}	 
		this.size--;
		return rotations;
	}


	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there;
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	 * returns -1 if an item with key k was not found in the tree.
	 * complexity: O(log(n))
	 */
	public int delete(int k){
		/* perform doSearch to find a node with key k, if exists in tree */
		IAVLNode resNode = this.doSearch(k);
	
		/* given key not in tree, return -1 */
		if(resNode == null || resNode.getKey() != k || this.empty()) {							
			return -1;
		}
		
		/* use method delete(IAVLNode node) to delete node return number of rotations performed */
		else {
			int rotations = delete(resNode);
			return rotations;
		}
	}
   

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty
	 * complexity: O(log(n))
	 */
	public String min() {
		
		/* return null if tree is empty */
		if (this.empty()) { 						
			return null;
		}
		
		/* get to the left-most node in the tree, and get it's value */
		IAVLNode currNode = this.root;
		while (currNode.getLeft() != this.pseudoNode){
			currNode = currNode.getLeft();	
		}
		return currNode.getValue(); 
	}


	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree,
	 * or null if the tree is empty
	 * complexity: O(log(n))
	 */
	public String max() {
		   
		/* return null if tree is empty */
		if (this.empty()) {return null;} 
		   
		/* get to the right-most node in the tree, and get it's value */
		IAVLNode currNode = this.root;
		while (currNode.getRight() != this.pseudoNode) {			
			currNode = currNode.getRight();		
		}
		return currNode.getValue(); 
	}   
   
   
	/**
	 * ADDED METHOD
	 * private void KeysInOrder(IAVLNode node, int[] outputArr)
	 *
	 * performs in-order walk and stores all keys, by order, in given array. 
	 * complexity: O(n)
	 */
	private int keysInOrder(IAVLNode node, int[] outputArr, int index) {	
		
		/* stop recursion when getting to pseudo node */
		if (node == this.pseudoNode) {								
			return index;
		}
		
		index = keysInOrder(node.getLeft(), outputArr, index);			/* start recursive calls with left son */
		outputArr[index] = node.getKey();								/* add key in matching index of output array */
		index++;
		index = keysInOrder(node.getRight(), outputArr, index);			/* continue recursive calls with right son */
		return index;
	}
   
	
	/**
	 * ADDED METHOD
	 * private void ValsInOrder(IAVLNode node, String[] outputArr)
	 *
	 * performs in-order walk and stores all values, by order, in given array.
	 * complexity: O(n)
	 */
	private int infoInOrder(IAVLNode node, String[] outputArr, int index) {
	
		/* stop recursion when getting to pseudo node */
		if (node == this.pseudoNode) {
			return index;
		}
		
		index = infoInOrder(node.getLeft(), outputArr, index);		/* start recursive calls on left son */		
		outputArr[index] = node.getValue();							/* add value in matching index of output array */
		index++;
		index = infoInOrder(node.getRight(), outputArr, index);		/* continue with recursive calls on right son */
		return index;
	}

	
	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree,
	 * or an empty array if the tree is empty.
	 * complexity: O(n)
	 */
	public int[] keysToArray() {					
		int[] keysArr = new int[this.size];
		int[] emptyArr = {};
	
		if(this.empty()) {return emptyArr;}
		
		/* perform in-order walk through the tree and get keys */
		keysInOrder(this.root, keysArr, 0);		
		
		return keysArr;    
	}

	
	/**
	 * public String[] infoToArray()
	 *
	 * Returns a sorted array which contains all values in the tree,
	 * or an empty array if the tree is empty.
	 * complexity: O(n)
	 */
	public String[] infoToArray() {					
		String[] infoArr = new String[this.size];
		String[] emptyArr = {};
	
		if(this.empty()) {return emptyArr;}
		
		/* perform in-order walk through the tree and get keys */
		infoInOrder(this.root, infoArr, 0);		
		
		return infoArr;    
	}

	
	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 *
	 * precondition: none
	 * postcondition: none
	 * complexity: O(1)
	 */
	public int size() {
		return this.size;
	}
	
	/**
	 * public IAVLNode getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 *
	 * precondition: none
	 * postcondition: none
	 * complexity: O(1)
	 */
	public IAVLNode getRoot() {
		if (this.empty()) {
			return null;
		}
		return this.root;
	}
   
   
	/**
	 * ADDED METHOD
	 * public int getRightRotations()
	 *
	 * Returns the number of right rotations performed
	 * complexity: O(1)
	 */
	public int getRightRotations() {
		return this.rightRotations;
	}
	
	
	/**
	 * ADDED METHOD
	 * public int getLeftRotations()
	 *
	 * Returns the number of left rotations performed
	 * complexity: O(1)
	 */
	public int getLeftRotations() {
		return this.leftRotations;
	}
	
	/**
	 * public IAVLNode select(IAVLNode node, int i)
	 *
	 * Returns the node whose rank is i
	 * if i > this.size or i<1 return null.
	 * complexity: O(log(n))
	 */
	public AVLNode select(IAVLNode node, int i) {
		
		if (i < 1 || i > this.size) {
			return null;
		}
		
		IAVLNode currNode = node;
		IAVLNode leftSon = currNode.getLeft();
		int currRank = ((AVLNode) leftSon).getSize() + 1;
		
		/* rank of current node is exactly i, mission accomplished */
		if (currRank == i) {
			return (AVLNode)currNode;
		}
		/* if rank of current node smaller than i, search in current node's right sub-tree for a node whose rank is (currRank - i) */
		else if (currRank  < i) {
			return select(currNode.getRight(), i - currRank);
		}
		
		/* if rank of current node bigger than i, search in current node's left sub-tree for a node whose rank is i */
		else {
			return select(currNode.getLeft(), i);
		}
	}
	
	
	/**
	 * ADDED METHOD
	 * public int treeListInsert(int i, int k, String s) 
	 *
	 * aid method for inserting new items to the tree according to tree list logic
	 * inserts an item to the ith position in tree-list with key k and info s.
	 * returns -1 if i<0 or i>tree.size otherwise return 0.
	 * complexity: O(log(n))
	 */
	public int treeListInsert(int i, int k, String s) {
		if (i < 0 || i > this.size) {
			return -1;
		}
	
		/* create new node */
		IAVLNode insertedNode = this.newNodeInTree(k, s);
	
		/* if i == tree.size, insert node at the end of tree */
		if (i == this.size) {
			AVLTree.AVLNode maxNode = this.select(this.root, this.size);
			insertedNode.setParent(maxNode);
			if (!this.empty()) {
				maxNode.setRight(insertedNode);
			}
		}
		
		/* find the ith node in tree, if it has no left son, put new node as left son
		 * otherwise, put new node as ith node's predecessor right son */
		else {
			AVLNode potentialParent = this.select(this.root, i+1);
			
			if (potentialParent.getLeft() == this.pseudoNode) {
				insertedNode.setParent(potentialParent);
				potentialParent.setLeft(insertedNode);
			}
			
			else {
				IAVLNode parent = this.getPredecessor(potentialParent);
				insertedNode.setParent(parent);
				parent.setRight(insertedNode);
			}	  
		}
		
		/* perform balancing, correct heights and sizes */
		this.fixTree(insertedNode.getParent(), false);

		/* increase tree size */
		this.size++;

		return 0;
	}
	
	
	/**
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode {	
		public int getKey(); //returns node's key 
		public String getValue(); //returns node's value [info]
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
    	public void setHeight(int height); // sets the height of the node
    	public int getHeight(); // Returns the height of the node 
	}

	
	/**
	 * public class AVLNode
	 *
	 * If you wish to implement classes other than AVLTree
	 * (for example AVLNode), do it in this file, not in 
	 * another file.
	 * This class can and must be modified.
	 * (It must implement IAVLNode)
	 */
	public class AVLNode implements IAVLNode {
		private Item item; 		/* Item contains key & value of node */
		private int height;
		private int size;
		private IAVLNode left;
		private IAVLNode right;
		private IAVLNode parent;
		  
		/* create AVLNode by inserting item and setting other parameters to zero/NULL, update with insertion to tree */  
	  	public AVLNode(Item item){	
	  		this.item = item;
	  		this.size = 1;
	  		this.height = 0;
	  		this.left = null;				
	  		this.right = null;		
	  		this.parent = null;
	  	}
	 	 	
		/* constructor of pseudo node, generate only one for each tree. would replace missing son */
		public AVLNode() {		
			this.item = new Item(777, null);
			this.height = -1;
			this.size = 0;
			this.left = null;
			this.right = null;
			this.parent = null;
		}
	  	
		/* return item's key */
		public int getKey() {					  
			return this.item.getKey(); 
		}
		
		/* return item's value */
		public String getValue() {				 
			return this.item.getInfo(); 
		}
		
		/* set left son to be node given as argument */
		public void setLeft(IAVLNode node) { 	
			this.left = node;
		}
		
		/* return left son */
		public IAVLNode getLeft() {			   
			return this.left;
		}
		
		/* set right son to be node given as argument */
		public void setRight(IAVLNode node) {  
			this.right = node;
		}
		
		/* return right son */
		public IAVLNode getRight() { 			
			return this.right; 
		}
		
		/* set parent to be node given as argument */
		public void setParent(IAVLNode node) {  
			this.parent = node;
		}
	
		/* return parent */
		public IAVLNode getParent() {			
			return this.parent;
		}
		
		/* height is calculated as max height of sons + 1 */
		public int calcHeight() {				
			int leftHeight = this.getLeft().getHeight();
			int rightHeight = this.getRight().getHeight();
			
			return Math.max(leftHeight, rightHeight) + 1;
		}
	
		/* set height to be height given as argument */
		public void setHeight(int height) { 	
			this.height = height;
		}
		
		/* return node's height */
		public int getHeight() { 				
			return this.height;
	    }
		
		/* size computed by aggregating sizes of both sons and adding 1 for the node itself */
		public int calcSize() {
			return ((AVLNode)this.getLeft()).getSize() + ((AVLNode) this.getRight()).getSize() + 1;
		}
		
		/* set size to be size given as argument */
		public void setSize(int size) { 		
			this.size = size;
		}
		
		/* return node's size */
		public int getSize() { 				
			return this.size;
	    }
		
		/* set new height and size */
		public void fixSizeHeight() {
			if (this.height == -1) {
				this.height = -1;
				this.size = 0;
			}
			
			else {
				this.setHeight(this.calcHeight());		
				this.setSize(this.calcSize()); 
			}
		}
		
		/* calculate node's balance factor */
		public int BF() {
			
			/* check if pseudoNode, if so, BF is 0 */
			if (this.height == -1) { 
				return 0;
			}
			
			else {
				return this.getLeft().getHeight() - this.getRight().getHeight();
			}
		}
		
		/* set node's item */
		private void setItem(Item item) {
			this.item = item;
		}
		
		/* return node's item */
		public Item getItem() {
			return this.item;
		}
		
		/* return true if node is left son, false otherwise (right son or root) */
		private boolean isLeftSon() {
			if (this.parent != null) {
				if (this == this.getParent().getLeft()) {
					return true;
				}
			}
			return false;
		}
	}
}




