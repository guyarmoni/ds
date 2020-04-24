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
	IAVLNode root;
	int size;
	IAVLNode pseudoNode;
	int LeftRotations;
	int RightRotations;
	
	/* AVLTree constructor */
	public AVLTree() { 
		this.size = 0;
		this.root = null;
		this.pseudoNode = new AVLNode();	/* generate unique pseudo node for this tree */
	}

  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   * complexity: O(1)
   *
   */
  public boolean empty() {
	if (this.size() == 0) {				/* determine if empty according to tree size */
		return true;
	}									/* tree implementation guarantees non-negative size for tree */
	return false;
  }

  /**
   * ADDED METHOD
   * private IAVLNode doSearch(int k)
   *
   * returns the node with key k if it exists in the tree
   * returns null if tree is empty 
   * otherwise, returns last existing node in search path
   * 
   * complexity: O(log(n))
   */
  private IAVLNode doSearch(int k) {
	  
	  /* checks if tree is empty */
	  if (this.empty()) {return null;} 	
	  
	  IAVLNode prevNode = null;
	  IAVLNode currNode = this.root; 			/* set root to be first node to start the search */
	  int currKey = currNode.getKey();
	  while (currNode != this.pseudoNode) {		/* continue search by BST rules while key not found & relevant son exist */
		  prevNode = currNode;					/* restore currNode in prevNode then set relevant son to currNode */
		  if (k == currKey) {					/* return node with key k if exist */
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
	  /* return last node of search if key not found */
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
	  
	  /* apply doSearch, if node is actual node return it's value, if node is pseudo return null
	   * condition resNode.getKey() != k relies on fact that all keys are distinct 
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
   * parent of node becomes parent of pivot,
   * set pivot as root if node was former root
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
	  pivot.getRight().setParent(node); 			/* node's set to be pivot's right subtree's new parent */
	  
	  /* node's parent becomes new pivot's parent
	   * set new tree root if needed
	   * if node was left son, replace with pivot as left son, otherwise replace with pivot as right son  */
	  this.swapSonOrRoot(node, pivot);
	  
	  /* set node as pivot's new right subtree, set pivot as node's parent */
	  pivot.setRight(node);							
	  node.setParent(pivot);						
	  
	  /* correct size and height for pivot and node */
	  ((AVLNode) pivot).fixSizeHeight();
	  ((AVLNode) node).fixSizeHeight();
  }
  
  
  /**
   * ADDED METHOD
   * private void rotateL(AVLNode node)
   *
   * perform left rotation
   * complexity: O(1)
   */
  private void rotateL(IAVLNode node) {
	  IAVLNode pivot = node.getRight(); 	/* set pivot to be right son of rotated node */
	  node.setRight(pivot.getLeft());		/* pivot's left subtree becomes rotated node's right subtree */
	  pivot.getLeft().setParent(node); 		/* node's set to be pivot's left subtree's new parent */
	  
	  /* node's parent becomes new pivot's parent
	   * set new tree root if needed
	   * if node was left son, replace with pivot as left son, otherwise replace with pivot as right son  */
	  this.swapSonOrRoot(node, pivot);
	  
	  /* set node as pivot's new left subtree, set pivot as node's parent */
	  pivot.setLeft(node);					
	  node.setParent(pivot);				
	    
	  /* correct size and height for pivot and node */
	  ((AVLNode) pivot).fixSizeHeight();
	  ((AVLNode) node).fixSizeHeight();
  }
  
  
  /**
   * ADDED METHOD
   * private void rotateRL(AVLNode node)
   *
   * perform right then left rotation
   * complexity: O(1)
   */
  private void rotateRL(IAVLNode node) {
	  IAVLNode pivot = node.getRight().getLeft(); 	/* set pivot */
	  node.setRight(pivot.getLeft());				/* pivot's left subtree becomes rotated node's right subtree */
	  pivot.getParent().setLeft(pivot.getRight());	/* pivot's right subtree becomes pivot's parent left subtree */
	  pivot.getParent().setParent(pivot); 			/* pivot becomes his previous parent's parent */
	  pivot.setRight(pivot.getParent()); 			/* pivot's previous parent becomes pivot's right son */
	 
	  /* node's parent becomes new pivot's parent
	   * set new tree root if needed
	   * if node was left son, replace with pivot as left son, otherwise replace with pivot as right son  */
	  this.swapSonOrRoot(node, pivot);
	  
	  /* set node as pivot's new left subtree, set pivot as node's parent */
	  pivot.setLeft(node);								
	  node.setParent(pivot);							
	  
	  /* correct size and height for pivot, node and extra node between them which is pivot's new right son */
	  ((AVLNode) pivot).fixSizeHeight();
	  ((AVLNode) node).fixSizeHeight();
	  ((AVLNode) pivot.getRight()).fixSizeHeight();
  }
  
  
  /**
   * ADDED METHOD
   * private void rotateLR(AVLNode node)
   *
   * perform left then right rotation
   * complexity: O(1)
   */
  private void rotateLR(IAVLNode node) {
	  IAVLNode pivot = node.getLeft().getRight(); 	/* set pivot */
	  node.setLeft(pivot.getRight());				/* pivot's right subtree becomes rotated node's left subtree */
	  pivot.getParent().setRight(pivot.getLeft());	/* pivot's left subtree becomes pivot's parent right subtree */
	  pivot.getParent().setParent(pivot); 			/* pivot becomes his previous parent's parent */
	  pivot.setLeft(pivot.getParent()); 			/* pivot's previous parent becomes pivot's left son */
	  
	  /* node's parent becomes new pivot's parent
	   * set new tree root if needed
	   * if node was left son, replace with pivot as left son, otherwise replace with pivot as right son  */
	  this.swapSonOrRoot(node, pivot);
	  
	  /* set node as pivot's new right subtree, set pivot as node's parent */
	  pivot.setRight(node);								
	  node.setParent(pivot);							
	  
	  /* correct size and height for pivot, node and extra node between them which is pivot's new left son */
	  ((AVLNode) pivot).fixSizeHeight();
	  ((AVLNode) node).fixSizeHeight();
	  ((AVLNode) pivot.getLeft()).fixSizeHeight();
  }
  
  /**
   * ADDED METHOD
   * private void rotate(AVLNode node)
   *
   * perform correct rotation after calculating BF for node and sons.
   * return number of rotations performed
   * complexity: O(1)
   */
  private int rotate(AVLNode node, boolean isDelete){
	  int BFRight = ((AVLNode) node.getRight()).BF();
	  int BFLeft = ((AVLNode) node.getLeft()).BF();
	 
	  if (node.BF() == -2) {
		  if (BFRight == -1 || isDelete && BFRight == 0) {
			  rotateL(node);
			  this.LeftRotations += 1;
			  return 1;
		  }
		  else if (BFRight == 1) {
			  rotateRL(node);
			  this.LeftRotations += 1;
			  this.RightRotations += 1;
			  return 2;
		  }
	  }
	
	  else if (node.BF() == 2) {
		  if (BFLeft == -1) {
			  rotateLR(node);
			  this.LeftRotations += 1;
			  this.RightRotations += 1;
			  return 2;
		  }
		  else if (BFLeft == 1 || isDelete && BFLeft == 0) {
			  rotateR(node);
			  this.RightRotations += 1;
			  return 1;
		  }
	  }
	  return 0;
  }
  
  /**
   * ADDED METHOD
   * private void initialInsertion(IAVLNode parent, IAVLNode son)
   *
   * inserts new item to the AVL tree, and increasing tree size, before performing rotations.
   * complexity: O(1)
   */
  private void initialInsertion(IAVLNode parent, IAVLNode son) {
	  this.size += 1;							/* increase tree size */
	  son.setParent(parent);					/* set pseudo nodes as sons */
	  son.setLeft(this.pseudoNode);
	  son.setRight(this.pseudoNode);
	  if (parent == null) {						/* tree is empty, set node as root */
		  this.root = son;
	  }
	  else {									/* insert new node in place (after last node in doSearch) */
		  if (parent.getKey() > son.getKey()) {		
			  parent.setLeft(son);
		  }
		  else {
			  parent.setRight(son);
		  }
	  }
  }
  
  /**
   * ADDED METHOD
   * public int fixTree(IAVLnode node)
   *
   * go in path from given node to root
   * set new height and size for each node in path
   * perform rotations if needed
   * return number of rotations made
   * complexity: O(log(n))
   */
  public int fixTree(IAVLNode node, boolean isDelete) {	  
	  /* tree contains only root which was  just inserted, no fix needed 
	  if (this.size() == 1) {
		  return 0;
	  } */ /* <-- THIS BLOCK NOT GOOD DONT CONSIDER DELETION */
	  
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
	  
	  /* create new node with given key and value and cast to AVLNode to perform added methods */
	  Item insertedItem = new Item(k, i);
	  IAVLNode insertedNode = new AVLNode(insertedItem);		
		  
	  int rotations = 0;
	  							
	  this.initialInsertion(resNode, insertedNode);			/* insert new node in place before balancing, increase tree size by 1 */
	  rotations = this.fixTree(resNode, false);				/* correct all sizes and heights in path up to root, perform balancing */ 

	  return rotations;
  }
  
  /**
   * ADDED METHOD
   * public IAVLNode getPredecessor(IAVLNode node)
   *
   * returns the given node's predecessor if exits
   * otherwise return pseudo node
   * complexity: O(log(n))
   */ 
  public IAVLNode getPredecessor(IAVLNode node) {
	  
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
		  /* if root reached with no left turns, predecessor doesn't exist -- return pseudo node, else predecessor is first node after left turn */
		  if(currNode == null) {
			  return this.pseudoNode;
		  }
		  return currNode.getParent();	
	  	}
  	}

  /**
   * ADDED METHOD
   * public IAVLNode getSuccessor(IAVLNode node)
   *
   * returns the given node's successor if exits
   * otherwise return pseudo node
   * complexity: O(log(n))
   */ 
  public IAVLNode getSuccessor(IAVLNode node) {
	  
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
   * private void fixAfterDeletion(IAVLNode node)
   * 
   * decrease all ancestors' size and height by 1
   * complexity: O(log(n)) 
   */ 
  private void fixAfterDeletion(IAVLNode node) {
	  AVLNode currNode = (AVLNode) node.getParent();
	  while(currNode != null) {
		  currNode.fixSizeHeight();
		  currNode = (AVLNode)currNode.getParent();
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
   * return physically deleted node's parent (null if node was root)
   * complexity: O(log(n)) 
   */ 
  private IAVLNode initialDeletion(IAVLNode node) {
	  IAVLNode outputNode;
	  
	  /* node is a leaf - fix all ancestors, disconnect from tree, turn node to pseudo node (for complete isolation from tree) */
	  if (node.getLeft() == this.pseudoNode && node.getRight() == this.pseudoNode) {
		  if (((AVLNode) node).isLeftSon()) {
			  node.getParent().setLeft(this.pseudoNode);
		  }
		  else {
			  node.getParent().setRight(this.pseudoNode);
		  }
		  /*fixAfterDeletion(node);*/
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
	  
	  /* node has 1 son -- fix all ancestors, assign node's parent to be this son parent
	   * initiate fields as new node, disconnect from tree */
	  else {
		  /* fixAfterDeletion(node); */
		  nodeByPass(node);
		  outputNode = node.getParent();
		  node = this.pseudoNode;
	  }
	  return outputNode;
  }
  
  /**
   * public int delete(IAVLNode node)
   *
   * decreases tree size by 1
   * deletes a node from the binary tree
   * perform rebalancing as needed
   * 
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * complexity: O(log(n))
   */
   public int delete(IAVLNode node){
	   this.size -= 1;
	   IAVLNode fixingNode = initialDeletion(node);							
	   int rotations = this.fixTree(fixingNode, true);
	   
	   if (node == this.root) {
		   this.root = null;
	   }
	   
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
 	   /* perform search to find a node with key k if in tree */
 	   IAVLNode resNode = this.doSearch(k);
 	   
 	   /* given key not in tree, return -1 */
 	   if(resNode == null || resNode.getKey() != k || this.empty()) {							
 		   return -1;
 	   }
 	   
 	   /* use method delete(IAVLNode node) to delete node before balancing, correct all sizes and heights, and perform balancing */
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
	   
	   /* check if tree is empty, if so return null */
	   if (this.empty()) { 						
		   return null;
	   }
	   
	   /* get to the left-most node in the tree, then get its value */
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
	   
	   /* check if tree is empty, if so return null */
	   if (this.empty()) {return null;} 
	   
	   /* get to the right-most node in the tree, then get its value */
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
	   
	   /* stop when getting to pseudo node */
	   if (node == this.pseudoNode) {								
		   return index;
	   }
   		index = keysInOrder(node.getLeft(), outputArr, index);			/* start recursive calls on left son */
   		outputArr[index] = node.getKey();								/* add key in matching index of output array */
   		index++;
   		index = keysInOrder(node.getRight(), outputArr, index);			/* continue with recursive calls on right son */
   		return index;
   }
   
   /**
    * added method
    * private void ValsInOrder(IAVLNode node, String[] outputArr)
    *
    * performs in-order walk and stores all values, by order, in given array.
    * complexity: O(n)
    */
   private int infoInOrder(IAVLNode node, String[] outputArr, int index) {

	   /* stop when getting to pseudo node */
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
        
        keysInOrder(this.root, keysArr, 0);		/* perform in-order walk through the tree and get keys */
        
        return keysArr;    
  }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   * complexity: O(n)
   */
  public String[] infoToArray() {
	  String[] infoArr = new String[this.size];
	  String[] emptyArr = {};
	  
      if(this.empty()) {return emptyArr;}
	  
      infoInOrder(this.root, infoArr, 0);		/* perform in-order walk through the tree and get values */
      
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
    * public int getRoot()
    *
    * Returns the root AVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    * complexity: O(1)
    */
   
   public IAVLNode getRoot() {
	   if (this.empty()) {return null;} 		/* check if tree is empty. if so returns null */
	   
	   return this.root;
   }
   
   /**
    * public int rank(IAVLNode node)
    *
    * Returns the rank of the given node.
    * complexity: O(log(n))
    */
//	public int rank(IAVLNode node){ 
//		int rank = ((AVLNode) node.getLeft()).getSize() + 1;
//		IAVLNode currNode = node; 
//		while (currNode != null) {									/* go from node up to the root */
//			if (currNode == currNode.getParent().getRight()) {		/* if you go up left, add to rank his parent's left son size, +1 for parent itself */
//				rank += 1 + ((AVLNode) currNode.getParent().getLeft()).getSize();
//			}
//			currNode = currNode.getParent();						/* start next iteration with parent */
//		}
//		return rank;
//   }
   
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
		
		IAVLNode currNode = this.root;
		IAVLNode leftSon = currNode.getLeft();
		int currRank = ((AVLNode) leftSon).getSize() + 1;
		
		/* if rank of current node smaller than i, search in current node's right sub-tree for a node whose rank is (currRank - i) */
		if (currRank  < i) {
			select(currNode.getRight(), i - currRank);
		}
		/* if rank of current node bigger than i, search in current node's left sub-tree for a node whose rank is i */
		else if (currRank > i) {
			select(currNode.getLeft(), i);
		}
		/* rank of current node is exactly i, mission accomplished */
		return (AVLNode)currNode;
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
	  public Item item; 						/* Item contains key & value of node */
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
	public void setSize(int size){ 		
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
	
	/* return true if node is left son, false otherwise (right son or root) */
	public boolean isLeftSon() {
		if (this.parent != null) {
			if (this.parent == this.getParent().getLeft()) {
				return true;
			}
		}
		return false;
	}
	
  }
  
}




