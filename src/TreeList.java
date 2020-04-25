
/**
 *
 * Tree list
 *
 * An implementation of a Tree list with  key and info
 *
 */
 public class TreeList{
	 AVLTree tree;
	 
	 public TreeList() {
		 this.tree = new AVLTree();
	 }
	 
  /**
   * public Item retrieve(int i)
   *
   * returns the item in the ith position if it exists in the list.
   * otherwise, returns null
   */
  public Item retrieve(int i){
	if (i < 0 || i >= tree.size) {
		return null;
	}
	AVLTree.AVLNode node = this.tree.select(this.tree.root, i + 1);
	return node.item;
  }

  /**
   * public int insert(int i, int k, String s) 
   *
   * inserts an item to the ith position in list  with key k and  info s.
   * returns -1 if i<0 or i>n otherwise return 0.
   */
   public int insert(int i, int k, String s) {
	  if (i < 0 || i > this.tree.size) {
		  return -1;
	  }

	   if (this.tree.empty()){
		   this.tree.insert(k, s);
		   return 0;
	   }

	  Item item  = new Item(k, s);
	  AVLTree.AVLNode insertedNode = (this.tree).new AVLNode(item);



	  if (i == this.tree.size) {
		  AVLTree.AVLNode maxNode = this.tree.select(this.tree.root, this.tree.size);
		  insertedNode.setParent(maxNode);
		  maxNode.setRight(insertedNode);
	  }
	  
	  else {
		  AVLTree.AVLNode potentialParent = this.tree.select(this.tree.root, i+1);
		  if (potentialParent.getLeft() == this.tree.pseudoNode) {
			  insertedNode.setParent(potentialParent);
			  potentialParent.setLeft(insertedNode);
		  }
		  else {
			  AVLTree.IAVLNode parent = this.tree.getPredecessor(potentialParent);
			  insertedNode.setParent(parent);
			  potentialParent.setRight(insertedNode);
		  }
			  
	  }
	  this.tree.fixTree(insertedNode, false);

	  return 0;
   }

  /**
   * public int delete(int i)
   *
   * deletes an item in the ith posittion from the list.
	* returns -1 if i<0 or i>n-1 otherwise returns 0.
   */
   public int delete(int i){
	   if (i < 0 || i >= this.tree.size) {
		   return -1;
	   }
	   
	   AVLTree.IAVLNode deletedNode = this.tree.select(this.tree.root, i + 1);
	   this.tree.delete(deletedNode);
	   
	   return 0;
   }
	 
 }