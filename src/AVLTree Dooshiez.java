/**     Name: Dana Aminoch
 UserName: danaaminoch
 ID: 318437126
 ---------------------------------
 Name: Maya Ben Dov
 UserName: mayabendov
 ID: 318731346
 */

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {
	/**
	 * public boolean empty()
	 * <p>
	 * returns true if and only if the tree is empty
	 */

	private IAVLNode root;
	private IAVLNode external_leaf;
	private IAVLNode min;
	private IAVLNode max;

	public AVLTree() { // complexity = O(1)
		this.external_leaf = new AVLNode();
		this.root = external_leaf;
		this.max = null;
		this.min = null;
	}

	public boolean empty() { // complexity = O(1)
		if (this.root.getKey() ==-1) {
			return true;
		}
		return false;
	}


	/**
	 * public String search(int k)
	 * <p>
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 */
	public String search(int k) { // complexity = O(log(n))
		if (this.empty())
			return null;
		IAVLNode curr_node = this.root;
		while (curr_node.isRealNode()) {
			if (curr_node.getKey() == k)
				return curr_node.getValue();
			else {
                if (k < curr_node.getKey())
                    curr_node = curr_node.getLeft();
                else
                    curr_node = curr_node.getRight();
            }
		}
		return null;
	}

	/**
	 * public int insert(int k, String i)
	 * <p>
	 * inserts an item with key k and info i to the AVL tree.
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	 * returns -1 if an item with key k already exists in the tree.
	 */
	public int insert(int k, String i) { // complexity = O(log(n))
		if (this.empty()) {
		    IAVLNode root = new AVLNode(external_leaf, external_leaf, null, k, i);
            insert_update_min_max(root);
			this.root = root;
			return 0;
		}
		if (this.search(k) != null) // if the node is already in the tree we don't need to add it again. zero rotations.
			return -1;
		IAVLNode parent_node = find_parent_for_given_key_and_change_subtreesize(k, 1);

		IAVLNode new_node = new AVLNode(external_leaf, external_leaf, parent_node, k, i); // create the new node we will add to the tree under the parent node
		insert_update_min_max(new_node);
		boolean is_parent_leaf = parent_node.isLeaf(); // we want to remember if the parent of the new node is a leaf or not

		boolean insert_from_left = true; //we want to know if we added the new node on the left of the parent or on the right
		if (k > parent_node.getKey()) { // new node is on left side of parent
			parent_node.setRight(new_node);
			insert_from_left = false;
		} else { // new node is on right side
			parent_node.setLeft(new_node);
		}
		int rotations_number_after_rebalance = 0;
		if (is_parent_leaf)// the parent of the new node is a leaf. take care of ranks and rotations
			rotations_number_after_rebalance = insert_rebalance(parent_node, insert_from_left);
		return rotations_number_after_rebalance;
	}

	public IAVLNode find_parent_for_given_key_and_change_subtreesize(int k, int insert_or_delete) { // complexity = O(log(n))
		if (k == this.root.getKey()) { // when we split 2 trees by the root
			return null;
		}
		IAVLNode curr_node = this.root;
		IAVLNode parent_node = curr_node;
		while (curr_node.isRealNode()) {
			curr_node.setSubtreeSize(curr_node.getSubtreeSize() + insert_or_delete); // every node we pass gets +1 to the subtreesize if we add new node or -1 if we delete a node
			if (curr_node.getKey() == k)
				return parent_node;
			else {
                if (curr_node.getKey() > k){
                    parent_node = curr_node;
                    curr_node = curr_node.getLeft();
                }
                else  {
                    parent_node = curr_node;
                    curr_node = curr_node.getRight();
                }
            }
		}
		return parent_node;
	}

	public int insert_rebalance(IAVLNode parent_node, boolean insert_from_left) { // complexity = O(log(n))
		// is leaf. take care of ranks and rotations
		int number_of_promote = 0;
		while ((parent_node.zero_one() || parent_node.one_zero())) { // we promote until the problem was fixed or we reached on of the extreme cases or we reached the root
		    parent_node.promote();
			number_of_promote+=1;
            if (parent_node.getKey()==this.root.getKey()){break;}
            insert_from_left = !x_is_the_right_child_of_his_parent(parent_node);
            parent_node = parent_node.getParent();
		}
		if (parent_node.isRankLegal())
			return 0;
		else { // we are in the case of 2,0 or 0,2 and the child is 1,2 or 2,1
			if (insert_from_left) { // that new leaf was added on the left size-  the zero is on the left
				if (parent_node.getLeft().getLeftDiff() == 1) { // it also means that parent_node.getLeft().getRightDiff()==2
					r_rotation(parent_node);
					parent_node.demote();
					return number_of_promote+2;
				} else { // parent_node.getLeft().getLeftDiff()==2 -and- parent_node.getLeft().getRightDiff()==1
                    lr_rotation(parent_node);
					parent_node.demote();
					parent_node.getParent().promote();
					parent_node.getParent().getLeft().demote();
					return number_of_promote+5;
				}
			}
			else {// that new leaf was added on the right size-  the zero is on the right
				if (parent_node.getRight().getRightDiff() == 1) { // it also means that parent_node.getRight().getLeftDiff()==2
				    l_rotation(parent_node);
					parent_node.demote();
					return number_of_promote+2;
				} else { // parent_node.getRight().getRightDiff()==2 -and- parent_node.getRight().getLeftDiff()==1
				    rl_rotation(parent_node);
					parent_node.demote();
					parent_node.getParent().promote();
					parent_node.getParent().getRight().demote();
					return number_of_promote+5;
				}
			}
		}
	}

	public void make_x_left_son_of_y(IAVLNode x, IAVLNode y) { // complexity = O(1)
	    if (y!=null){
            y.setLeft(x);
        }
		if (x.isRealNode()) {
			x.setParent(y);
		}
	}

	public void make_x_right_son_of_y(IAVLNode x, IAVLNode y) {// complexity = O(1)
        if (y!=null) {
            y.setRight(x);
        }
		if (x.isRealNode()) {
			x.setParent(y);
		}
	}

	public void r_rotation(IAVLNode y) { // complexity = O(1)
		IAVLNode x = y.getLeft();// x is the left son of y
		if (y.getKey() == this.root.getKey()) {
			this.root = x;
		}
		IAVLNode x_right = x.getRight();
        IAVLNode y_parent = y.getParent();
        boolean y_right_child = x_is_the_right_child_of_his_parent(y);
		make_x_right_son_of_y(y, x);
		make_x_left_son_of_y(x_right, y);
        if (y_right_child) {make_x_right_son_of_y(x,y_parent);}
        else {make_x_left_son_of_y(x,y_parent);}
        y.setSubtreeSize(y.getLeft().getSubtreeSize()+y.getRight().getSubtreeSize()+1);
		x.setSubtreeSize(x.getLeft().getSubtreeSize()+x.getRight().getSubtreeSize()+1);
	}

	public void l_rotation(IAVLNode x) { // complexity = O(1)
		IAVLNode y = x.getRight();// y is the right son of x
		if (x.getKey() == this.root.getKey()) {
			this.root = y;
		}
		IAVLNode y_left = y.getLeft();
        IAVLNode x_parent = x.getParent();
        boolean x_right_child = x_is_the_right_child_of_his_parent(x);
		make_x_left_son_of_y(x, y);
		make_x_right_son_of_y(y_left, x);
		if (x_right_child) {make_x_right_son_of_y(y,x_parent);}
		else {make_x_left_son_of_y(y,x_parent);}
		x.setSubtreeSize(x.getLeft().getSubtreeSize()+x.getRight().getSubtreeSize()+1);
		y.setSubtreeSize(y.getLeft().getSubtreeSize()+y.getRight().getSubtreeSize()+1);
	}

	public void rl_rotation(IAVLNode z) { // complexity = O(1)
		r_rotation(z.getRight());
		l_rotation(z);
	}

	public void lr_rotation(IAVLNode z) { // complexity = O(1)
		l_rotation(z.getLeft());
		r_rotation(z);
	}

	public void insert_update_min_max(IAVLNode new_node) { // complexity = O(1)
		if (this.min == null && this.max == null){
			if (this.empty()) { // if this is the first node it will be both min and max of the tree
				this.min = new_node;
				this.max = new_node;
			}
		}
		else {
			if (new_node.getKey() < this.min.getKey()) {
				this.min = new_node;
			}
			if (new_node.getKey() > this.max.getKey()) {
				this.max = new_node;
			}
		}
	}

	/**
	 * public int delete(int k)
	 * <p>
	 * deletes an item with key k from the binary tree, if it is there;
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	 * returns -1 if an item with key k was not found in the tree.
	 */
	public int delete(int k) // complexity = O(log(n))
	{
		if (empty())
			return -1;
		if (this.search(k) == null) // there is no such node in the tree
			return -1;
		int rotations_number_after_rebalance = 0;
		IAVLNode parent = find_parent_for_given_key_and_change_subtreesize(k, -1);
		IAVLNode delete_me;
		if (parent == null) { //the node we want to delete is the root
			delete_me = this.root;
			if (!delete_me.isLeaf()) {
				if (delete_me.getRight().isRealNode() && delete_me.getLeft().isRealNode()) { // the root has 2 child
					IAVLNode successor= successor(delete_me,0);
					IAVLNode parent_of_successor = successor.getParent();
					boolean successor_is_right_child= replace_with_successor(delete_me, true);
					if (!successor_is_right_child) {
						rotations_number_after_rebalance += delete_rebalance(parent_of_successor, true);
					}
					else {
						rotations_number_after_rebalance += delete_rebalance(successor, false);
					}
				} else { // the root has 1 child
					if (delete_me.getRight().isRealNode()) {
						this.root = delete_me.getRight();
						this.root.setParent(null);
					} else {
						this.root = delete_me.getLeft();
						this.root.setParent(null);
					}
				}
			} else {// the root is a leaf
				this.root = external_leaf;
			}
		} else { //the node we want to delete is not the root
			boolean node_is_left_to_parent = (k < parent.getKey());// the node we want to delete is the left child of his parent
			if (node_is_left_to_parent) {
				delete_me = parent.getLeft();
			} else {
				delete_me = parent.getRight();
			}
			if (!delete_me.isLeaf()) { // the node we want to delete is not a leaf
				if (delete_me.getRight().isRealNode() && delete_me.getLeft().isRealNode()) { // the node we want to delete has 2 children
                    IAVLNode successor= successor(delete_me,0);
				    IAVLNode parent_of_successor = successor.getParent();
				    boolean successor_is_right_child= replace_with_successor(delete_me, node_is_left_to_parent);
                    if (!successor_is_right_child) {
                        rotations_number_after_rebalance += delete_rebalance(parent_of_successor, true);
                    }
                    else {
                        rotations_number_after_rebalance += delete_rebalance(successor, false);
                    }
				} else {// the node we want to delete has 1 child
					if (delete_me.getLeft().isRealNode()) { //the node we want to delete has 1 child on his left side
						if (node_is_left_to_parent) { //the node we want to delete is the left child of his parent and has 1 child on his left side
							parent.setLeft(delete_me.getLeft());
							parent.getLeft().setParent(parent);
						} else { //the node we want to delete is the right child of his parent and has 1 child on his left side
							parent.setRight(delete_me.getLeft());
							parent.getRight().setParent(parent);
						}
					} else {//the node we want to delete has 1 child on his right side
						if (node_is_left_to_parent) { //the node we want to delete is the left child of his parent and has 1 child on his right side
							parent.setLeft(delete_me.getRight());
							parent.getLeft().setParent(parent);
						} else { //the node we want to delete is the right child of his parent and has 1 child on his right side
							parent.setRight(delete_me.getRight());
							parent.getRight().setParent(parent);
						}
					}
					rotations_number_after_rebalance += delete_rebalance(parent, node_is_left_to_parent);
				}
			} else {// the node we want to delete is a leaf
				if (node_is_left_to_parent) {
					parent.setLeft(external_leaf);
				} else {
					parent.setRight(external_leaf);
				}
				rotations_number_after_rebalance += delete_rebalance(parent, node_is_left_to_parent);
			}
		}
		delete_and_split_update_min_max();
		return rotations_number_after_rebalance;    // to be replaced by student code
	}

	public boolean replace_with_successor(IAVLNode delete_me, boolean node_is_left_to_parent) { // complexity = O(log(n))
		boolean successor_is_right_child;
		IAVLNode successor_node = successor(delete_me,-1);
		successor_node.setSubtreeSize(delete_me.getSubtreeSize()-1);
		successor_node.setHeight(delete_me.getHeight());
		//the successor is not the the right son of our node we need to change the left child of the successor parent to external_leaf;
		if (delete_me.getRight().getKey() != successor_node.getKey()) {
			make_x_left_son_of_y(successor_node.getRight(),successor_node.getParent());
			successor_is_right_child=false;
		}
		else {successor_is_right_child=true;}
		if (delete_me.getParent() != null) { // delete_me is not the root
			if (node_is_left_to_parent) {
				make_x_left_son_of_y(successor_node, delete_me.getParent());
			} else {
				make_x_right_son_of_y(successor_node, delete_me.getParent());
			}
		} else {
			this.root = successor_node;
			successor_node.setParent(null);
		}// delete_me is the root
		//is the successor is not the the right son of our node we need to change hhe left child of the successor parent to external_leaf;
		if (delete_me.getRight().getKey() != successor_node.getKey()) {
			make_x_right_son_of_y(delete_me.getRight(), successor_node);
		}
		make_x_left_son_of_y(delete_me.getLeft(), successor_node);
		successor_node.setSubtreeSize(successor_node.getRight().getSubtreeSize()+successor_node.getLeft().getSubtreeSize()+1);
		return successor_is_right_child;
	}

	public IAVLNode successor(IAVLNode x, int decrease_subtreesize) { // complexity = O(log(n))
		IAVLNode successor;
		if (this.max.getKey() == x.getKey()){
			return null;
		}
		if (x.getRight().isRealNode()) {
			successor = x.getRight();
			successor.setSubtreeSize(successor.getSubtreeSize()+decrease_subtreesize);
			while (successor.getLeft().isRealNode()) {
				successor = successor.getLeft();
				successor.setSubtreeSize(successor.getSubtreeSize()+decrease_subtreesize);
			}
			return successor;
		}
		IAVLNode parent;
		while (x.getParent().isRealNode()) {
			parent = x.getParent();
			if (parent.getLeft().getKey() == x.getKey()) {
				return parent;
			}
			x = x.getParent();
		}
		return null;
	}

	public int delete_rebalance(IAVLNode parent_node, boolean delete_from_left) { // complexity = O(log(n))
		int number_of_rotations = 0;
		outerloop:
		while (parent_node != null && !parent_node.isRankLegal()) {
			while (parent_node.two_two()) { // we demote until the problem was fixed or we reached on of the extreme cases or we reached the root
			    parent_node.demote();
				number_of_rotations += 1;
                if (parent_node.getKey()==this.root.getKey()){break outerloop;}
                delete_from_left = !x_is_the_right_child_of_his_parent(parent_node);
				parent_node = parent_node.getParent();
			}
			if (parent_node.isRankLegal()) {break;}
			// we are in the case of 3,1 or 1,3
			if (delete_from_left) { // that leaf was deleted on the left size-  the 3 is on the left
				if (parent_node.getRight().getLeftDiff() == 1 && parent_node.getRight().getRightDiff() == 1) { // we are at 3,1 with 1,1 below the 1
					l_rotation(parent_node);
					parent_node.demote();
					parent_node.getParent().promote();
					return number_of_rotations + 3;
				}
				else {
                    if (parent_node.getRight().getLeftDiff() == 2 && parent_node.getRight().getRightDiff() == 1) { // we are at 3,1 with 2,1 below the 1
                        l_rotation(parent_node);
                        parent_node.demote();
                        parent_node.demote();

                        parent_node= parent_node.getParent();
						delete_from_left = !x_is_the_right_child_of_his_parent(parent_node);
						parent_node= parent_node.getParent();

                        number_of_rotations += 3;
                    }
                    else {
                        if (parent_node.getRight().getLeftDiff() == 1 && parent_node.getRight().getRightDiff() == 2) { // we are at 3,1 with 1,2 below the 1
                            rl_rotation(parent_node);
                            parent_node.demote();
                            parent_node.demote();
                            parent_node.getParent().promote();
                            parent_node.getParent().getRight().demote();

							parent_node= parent_node.getParent();
							delete_from_left = !x_is_the_right_child_of_his_parent(parent_node);
							parent_node= parent_node.getParent();

                            number_of_rotations += 6;
                        }
                    }
                }
			} else {// that leaf was deleted on the right size-  the 3 is on the right
				if (parent_node.getLeft().getLeftDiff() == 1 && parent_node.getLeft().getRightDiff() == 1) { // we are at 1,3 with 1,1 below the 1
				    r_rotation(parent_node);
					parent_node.demote();
					parent_node.getParent().promote();
					return number_of_rotations + 3;
				}
				else {
                    if (parent_node.getLeft().getLeftDiff() == 1 && parent_node.getLeft().getRightDiff() == 2) { // we are at 1,3 with 1,2 below the 1
                        r_rotation(parent_node);
                        parent_node.demote();
                        parent_node.demote();

						parent_node= parent_node.getParent();
						delete_from_left = !x_is_the_right_child_of_his_parent(parent_node);
						parent_node= parent_node.getParent();

                        number_of_rotations += 3;
                    }
                    else {
                        if (parent_node.getLeft().getLeftDiff() == 2 && parent_node.getLeft().getRightDiff() == 1) { // we are at 1,3 with 2,1 below the 1
                            lr_rotation(parent_node);
                            parent_node.demote();
                            parent_node.demote();
                            parent_node.getParent().promote();
                            parent_node.getParent().getLeft().demote();

							parent_node= parent_node.getParent();
							delete_from_left = !x_is_the_right_child_of_his_parent(parent_node);
							parent_node= parent_node.getParent();

                            number_of_rotations += 6;
                        }
                    }
                }
			}
		}
		return number_of_rotations;
	}

	public void delete_and_split_update_min_max() { // complexity = O(log(n))
		if (this.empty()) { // if the tree after the deletion is empty
			this.min = null;
			this.max = null;
		} else {
			IAVLNode current_node = this.root;
			IAVLNode parent_node = this.root;
			while (current_node.isRealNode()) { // find the min in log(n)
				parent_node = current_node;
				current_node = current_node.getLeft();
			}
			this.min = parent_node;
			current_node = this.root;
			parent_node = this.root;
			while (current_node.isRealNode()) { // find the max in log(n)
				parent_node = current_node;
				current_node = current_node.getRight();
			}
			this.max = parent_node;
		}
	}

	/**
	 * public String min()
	 * <p>
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty
	 */
	public String min() { // complexity = O(1)
		if (empty()){
			return null;
		}
		return this.min.getValue(); // to be replaced by student code
	}

	/**
	 * public String max()
	 * <p>
	 * Returns the info of the item with the largest key in the tree,
	 * or null if the tree is empty
	 */
	public String max() {// complexity = O(1)
		if (empty()){
			return null;
		}
		return this.max.getValue(); // to be replaced by student code
	}

	/**
	 * public int[] keysToArray()
	 * <p>
	 * Returns a sorted array which contains all keys in the tree,
	 * or an empty array if the tree is empty.
	 */
	public int[] keysToArray() { // complexity = O(n)
		if (empty()){
			int[] keysArray = new int[0];
			return keysArray;
		}
		IAVLNode[] NodeArr = this.nodesToArray();
		int[] keysArray = new int[this.size()];
		for (int i = 0; i < NodeArr.length; i++) {
			keysArray[i] = NodeArr[i].getKey();
		}
		//int i=0;
		//while ( NodeArr[i] !=null){
		//	keysArray[i] = NodeArr[i].getKey();
		//	i++;
		//}
		return keysArray;
	}

	/**
	 * public String[] infoToArray()
	 * <p>
	 * Returns an array which contains all info in the tree,
	 * sorted by their respective keys,
	 * or an empty array if the tree is empty.
	 */
	public String[] infoToArray() { // complexity = O(n)
		if (empty()){
			String[] infoArray = new String[0];
			return infoArray;
		}
		IAVLNode[] NodeArr = this.nodesToArray();
		String[] infoArray = new String[this.size()];
		for (int i = 0; i < NodeArr.length; i++) {
			infoArray[i] = NodeArr[i].getValue();
		}
		return infoArray;
	}

	public IAVLNode[] nodesToArray(){ // complexity = O(n)
		IAVLNode[] nodeArray = new IAVLNode[this.size()];
		nodeArray[0] = this.min;
		for (int i = 1; i <this.size() ; i++) {
			if (nodeArray[i-1]==null || successor(nodeArray[i-1],0)==null) {break;}// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			nodeArray[i] = successor(nodeArray[i-1],0);
		}
		//int i=1;
		//while ( successor(nodeArray[i-1],0)!=null){
		//	nodeArray[i] = successor(nodeArray[i-1],0);
		//	i++;
		//}
		return nodeArray;
	}

	/**
	 * public int size()
	 * <p>
	 * Returns the number of nodes in the tree.
	 * <p>
	 * precondition: none
	 * postcondition: none
	 */
	public int size() {  // complexity = O(1)
		return this.root.getSubtreeSize();
	}

	/**
	 * public int getRoot()
	 * <p>
	 * Returns the root AVL node, or null if the tree is empty
	 * <p>
	 * precondition: none
	 * postcondition: none
	 */
	public IAVLNode getRoot() { // complexity = O(1)
		if (this.empty())
			return null;
		return this.root;
	}

	/**
	 * public string split(int x)
	 * <p>
	 * splits the tree into 2 trees according to the key x.
	 * splits the tree into 2 trees according to the key x.
	 * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	 * precondition: search(x) != null
	 * postcondition: none
	 */

	public AVLTree[] split(int x) { // complexity = O(log(n))
		IAVLNode x_parent= find_parent_for_given_key_and_change_subtreesize(x,0);
		AVLTree bigger = new AVLTree(); // will contain all the nodes that are bigger the x
		AVLTree smaller = new AVLTree();// will contain all the nodes that are smaller the x
		IAVLNode x_node;
		AVLTree[] trees_array = new AVLTree[2];
		if (x_parent==null){ // when the x is the root
			bigger.root= this.root.getRight();
			smaller.root= this.root.getLeft();
			smaller.delete_and_split_update_min_max();
			bigger.delete_and_split_update_min_max();
			trees_array[0]= bigger;
			trees_array[1]= smaller;
			return trees_array;
		}
		if (x_parent.getKey()>x) {x_node= x_parent.getLeft();} //we create x according to the side that he is from his parent
		else {x_node=x_parent.getRight();}
		bigger.root=x_node.getRight();
		bigger.root.setParent(null);
		x_node.setRight(external_leaf);
		smaller.root=x_node.getLeft();
		smaller.root.setParent(null);
		x_node.setLeft(external_leaf);
		AVLTree tmp_tree = new AVLTree();
		boolean up_and_left = x_is_the_right_child_of_his_parent(x_node); // check if x is the right son of his parent
		x_node = x_node.getParent(); // we start the loop with the parent of x
		while (x_parent!=null){
			x_parent = x_node.getParent();
			if (up_and_left) {
				x_node.setRight(null);
				tmp_tree.root = x_node.getLeft();
				tmp_tree.root.setParent(null);
				x_node.setLeft(null);
				up_and_left = x_is_the_right_child_of_his_parent(x_node); // if x_is_the_right_child_of_his_parent then when we go up to his parent we know that we went up and left
				x_node.setParent(null);
				if (x_parent!= null){
					if (up_and_left) {x_parent.setRight(external_leaf);}
					else {x_parent.setLeft(external_leaf);}
				}
				smaller.join(x_node, tmp_tree);
			}
			else {
				x_node.setLeft(null);
				tmp_tree.root = x_node.getRight();
				tmp_tree.root.setParent(null);
				x_node.setRight(null);
				up_and_left = x_is_the_right_child_of_his_parent(x_node); // if x_is_the_right_child_of_his_parent then when we go up to his parent we know that we went up and left
				x_node.setParent(null);
				if (x_parent!= null){
					if (up_and_left) {x_parent.setRight(external_leaf);}
					else {x_parent.setLeft(external_leaf);}
				}
				bigger.join(x_node, tmp_tree);
			}
			x_node=x_parent;
		}
		smaller.delete_and_split_update_min_max();
		bigger.delete_and_split_update_min_max();
		trees_array[0]= smaller;
		trees_array[1]= bigger;
		return trees_array;
	}

	public boolean x_is_the_right_child_of_his_parent (IAVLNode x){ // complexity = O(1)
		// we want to check if x_is_the_right_child_of_his_parent
	    if (x.getParent()==null) {return true;}
		if (x.getParent().getKey()>x.getKey()) {return false;}
		else {return true;}
	}

	/**
	 * public join(IAVLNode x, AVLTree t)
	 * <p>
	 * joins t and x with the tree.
	 * Returns the complexity of the operation (rank difference between the tree and t)
	 * precondition: keys(x,t) < keys() or keys(x,t) > keys()
	 * postcondition: none
	 */


	public int join(IAVLNode x, AVLTree t){ // complexity = O(log(n))
        int rank_difference = Math.abs(this.root.getHeight() - t.root.getHeight()) + 1;
	    if (!t.root.isRealNode()) { // the case thar t is empty- both if "this" is empty ot "this" is not empty
	    	this.insert(x.getKey(), x.getValue());
        }
	    else {
	        if (!this.root.isRealNode()){// the case that "this" is empty- ant t is not empty
	            t.insert(x.getKey(), x.getValue());
	            this.root = t.root;
            }
		    else{ // both trees are not empty
                //if we are not in the case that we send two subtrees from split to join and they don't have min and max, so we update the min and the max
                boolean my_trees_height_is_smaller = this.root.getHeight() < t.root.getHeight();
                boolean the_smaller_tree_is_on_the_left =
                        (my_trees_height_is_smaller && this.root.getKey() < t.root.getKey()) ||
                                (!my_trees_height_is_smaller && this.root.getKey() > t.root.getKey());
                IAVLNode a;
                IAVLNode b;
                IAVLNode c;
                int subtreesize_of_smaller_tree;
                if (the_smaller_tree_is_on_the_left) {
                    if (my_trees_height_is_smaller) { // case 1
                        a = this.root;
                        b = t.root;
                        c = b;
						subtreesize_of_smaller_tree = this.size();
                    } else { // case 2
                        a = t.root;
                        b = this.root;
                        c = b;
						subtreesize_of_smaller_tree=t.size();
                    }
                    while (b.getHeight() > a.getHeight()) {
                        c = b;
                        c.setSubtreeSize(c.getSubtreeSize()+subtreesize_of_smaller_tree+1);
                        b = b.getLeft();
                    }
                    make_x_left_son_of_y(a,x);
                    make_x_right_son_of_y(b,x);
					if (c.getKey() != b.getKey()){make_x_left_son_of_y(x,c);}
					else {this.root=x;}
                    if (my_trees_height_is_smaller) { this.root = t.root;}// case 1
                    x.setHeight(a.getHeight()+1);
					x.setSubtreeSize(x.getLeft().getSubtreeSize()+x.getRight().getSubtreeSize()+1);
                    if (extreme_case_join(c)){
                    	r_rotation(c);
						c.demote();
					}
                    insert_rebalance(c, true);
                } else {
                    if (my_trees_height_is_smaller) { // case 3
                        a = t.root;
                        b = this.root;
                        c = a;
                        subtreesize_of_smaller_tree= this.size();
                    } else { // case 4
                        a = this.root;
                        b = t.root;
                        c = a;
                        subtreesize_of_smaller_tree=t.size();
                    }
                    while (a.getHeight() > b.getHeight()) {
                        c = a;
                        c.setSubtreeSize(c.getSubtreeSize()+subtreesize_of_smaller_tree+1);
                        a = a.getRight();
                    }
                    make_x_right_son_of_y(b,x);
                    make_x_left_son_of_y(a,x);
                    if (c.getKey() != a.getKey()){make_x_right_son_of_y(x,c);}
                    else {this.root=x;}
                    if (my_trees_height_is_smaller) { this.root = t.root;}// case 3
                    x.setHeight(b.getHeight()+1);
					x.setSubtreeSize(x.getLeft().getSubtreeSize()+x.getRight().getSubtreeSize()+1);
					if (extreme_case_join(c)){
						l_rotation(c);
						c.demote();
					}
                    insert_rebalance(c, false);
                }
            }
        }
        join_update_min_max(x,t);
		return rank_difference;
	}

	public boolean extreme_case_join (IAVLNode c){ // complexity = O(1)
		if (c.getLeftDiff() == 0 && c.getRightDiff() == 2 && c.getLeft().getLeftDiff()==1 && c.getLeft().getRightDiff()==1)
			return true;
		if (c.getLeftDiff() == 2 && c.getRightDiff() == 0 && c.getRight().getLeftDiff()==1 && c.getRight().getRightDiff()==1)
			return true;
		return false;
	}

	public void join_update_min_max(IAVLNode x, AVLTree t)
	{
        // we want to make sure we are not in the case that the split sent 2 trees with no min and max
        if (this.min != null || this.max != null || t.max != null || t.min != null){
            if (this.min == null && this.max == null){ // "this" was empty before
                if (x.getKey()<t.min.getKey()){ this.min = x;}
                else {this.min = t.min;}
                if (x.getKey()>t.max.getKey()){ this.max = x;}
                else {this.max = t.max;}
            }
            else {
                if (t.min == null && t.max == null){ // t was empty before
                    if (x.getKey()<this.min.getKey()){ this.min = x;}
                    else {this.min = t.min;}
                    if (x.getKey()>this.max.getKey()){ this.max = x;}
                    else {this.max = t.max;}
                }
                else { // both trees were not empty
                    if (this.min.getKey()<t.min.getKey()){this.max = t.max;}
                    else {this.min = t.min;}
                }
            }

        }
    }

	/**
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode {
		public int getKey(); //returns node's key (for virtuval node return -1)

		public String getValue(); //returns node's value [info] (for virtuval node return null)

		public void setLeft(IAVLNode node); //sets left child

		public IAVLNode getLeft(); //returns left child (if there is no left child return null)

		public void setRight(IAVLNode node); //sets right child

		public IAVLNode getRight(); //returns right child (if there is no right child return null)

		public void setParent(IAVLNode node); //sets parent

		public IAVLNode getParent(); //returns the parent (if there is no parent return null)

		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node

		public void setHeight(int height); // sets the height of the node

		public int getHeight(); // Returns the height of the node (-1 for virtual nodes)

		public void setSubtreeSize(int i);
		public int getSubtreeSize();
		public boolean isLeaf();
		public boolean isRankLegal();
		public int getRightDiff();
		public int getLeftDiff();
		public void demote();
		public void promote();
        public boolean zero_one();
        public boolean one_zero();
        public boolean two_two();
	}

	/**
	 * public class AVLNode
	 * <p>
	 * If you wish to implement classes other than AVLTree
	 * (for example AVLNode), do it in this file, not in
	 * another file.
	 * This class can and must be modified.
	 * (It must implement IAVLNode)
	 */
	public class AVLNode implements IAVLNode {
		private IAVLNode leftChild;
		private IAVLNode rightChild;
		private IAVLNode parent;
		private int key;
		private String info;
		private int subtreeSize;
		private int height;
		private boolean realnode;

		public AVLNode() { // complexity = O(1)
			setLeft(null);
			setRight(null);
			setParent(null);
			setHeight(-1);
			this.key = -1;
			this.info = null;
			this.realnode = false;
			this.subtreeSize = 0;
		}

		public AVLNode(IAVLNode left, IAVLNode right, IAVLNode parent, int key, String info) { // complexity = O(1)
			setLeft(left);
			setRight(right);
			setParent(parent);
			setHeight(0);
			this.key = key;
			this.info = info;
			this.realnode = true;
			this.subtreeSize = 1;
		}

		public int getKey() {
			if (this.realnode)
				return this.key; // to be replaced by student code
			return -1;
		}

		public String getValue() {
			if (this.realnode)
				return this.info; // to be replaced by student code
			return null;
		}

		public void setLeft(IAVLNode node) {
			this.leftChild = node;
		}

		public IAVLNode getLeft() {
			if (this.realnode)
				return this.leftChild;
			return null;
		}

		public void setRight(IAVLNode node) {
			this.rightChild = node;
		}

		public IAVLNode getRight() {
			if (this.realnode)
				return this.rightChild; // to be replaced by student code
			return null;
		}

		public void setParent(IAVLNode node) {
			this.parent = node; // to be replaced by student code
		}

		public IAVLNode getParent() {
			if (this.realnode)
				return this.parent; // to be replaced by student code
			return null;
		}

		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode() {
			return this.realnode; // to be replaced by student code
		}

		public void setSubtreeSize(int size)
		{
			this.subtreeSize= size;
		}
		public int getSubtreeSize()
		{
			if (this.realnode)
				return this.subtreeSize; // to be replaced by student code
			return 0;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getHeight() {
			if (this.realnode)
				return this.height; // to be replaced by student code
			return -1;
		}

		public boolean isLeaf() {
			return (!this.leftChild.isRealNode()) && (!this.rightChild.isRealNode());
		}

		public boolean isRankLegal() {
			if (!this.isRealNode())
				return true;
			if ((getLeftDiff() == 1 && this.getRightDiff() == 1) || (getLeftDiff() == 1 && getRightDiff() == 2) || (getLeftDiff() == 2 && getRightDiff() == 1))
				return true;
			return false;
		}

		public int getRightDiff() {
			return this.height - this.getRight().getHeight();
		}

		public int getLeftDiff() {
			return this.height - this.getLeft().getHeight();
		}

		public void demote() {
			this.setHeight(this.getHeight() - 1);
		}

		public void promote() {
			this.setHeight(this.getHeight() + 1);
		}

        public boolean zero_one(){
            return this.getLeftDiff() == 0 && this.getRightDiff() == 1;
        }

        public boolean one_zero(){
            return this.getLeftDiff() == 1 && this.getRightDiff() == 0;
        }

        public boolean two_two(){
            return this.getLeftDiff() == 2 && this.getRightDiff() == 2;
        }
	}
}


