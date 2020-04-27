/**
 * Name: Guy Armoni
 * Username: guya
 * ID: 205988595
 * <p>
 * Name: Omer Militscher
 * Username: militscher
 * ID: 313585085
 */

/**
 *
 * Tree list
 *
 * An implementation of a Tree list with  key and info
 *
 */
public class TreeList {
    private AVLTree tree;

    /**
     *  TreeList constructor
     */
    public TreeList() {
        this.tree = new AVLTree();
    }

    /**
     * public Item retrieve(int i)
     *
     * returns the item in the ith position if it exists in the list.
     * otherwise, returns null
     * complexity: O(log(n))
     */
    public Item retrieve(int i) {
        if (i < 0 || i >= tree.size()) {
            return null;
        }

        AVLTree.AVLNode node = this.tree.select(this.tree.getRoot(), i + 1);

        return node.getItem();
    }


    /**
     * public int insert(int i, int k, String s)
     *
     * inserts an item to the ith position in list  with key k and  info s.
     * returns -1 if i<0 or i>n otherwise return 0.
     * complexity: O(log(n))
     */
    public int insert(int i, int k, String s) {
        return this.tree.treeListInsert(i, k, s);
    }


    /**
     * public int delete(int i)
     *
     * deletes an item in the ith posittion from the list.
     * returns -1 if i<0 or i>n-1 otherwise returns 0.
     * complexity: O(log(n))
     */
    public int delete(int i) {
        if (i < 0 || i >= this.tree.size()) {
            return -1;
        }

        AVLTree.IAVLNode deletedNode = this.tree.select(this.tree.getRoot(), i + 1);
        this.tree.delete(deletedNode);

        return 0;
    }

    /* return the AVLTree contained in TreeList
     * complexity: O(1)
     * */

    public AVLTree getTree() {
        return this.tree;
    }
}
