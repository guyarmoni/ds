
import java.util.Arrays;


public class TestStage1 {

    private String error_message="";
    public String getError(){
        return this.error_message;
    }
    public void addError(String msg,int main, int sub){
        this.error_message+=String.format("E%d.%d : %s%n", main,sub,msg);
    }
    /**
     * @return if method AVLTree.empty() works properly
     * @pre AVLTree needs to have empty constructor aka new AVLTree()
     * @pre AVLTree.insert() works with at least 1 element
     */
    public boolean empty() {
        boolean flag =true;
        //TEST 1.1
        AVLTree avlTree = new AVLTree();
        if (!avlTree.empty()) {
            addError("empty tree seems not empty",1,1);
            flag = false;
        }
        //TEST1.2
        avlTree.insert(1, "");
        if (avlTree.empty()){
            addError("not empty tree seems empty",1,2);
            flag = false;
        }
        return flag;
    }

    public boolean search() {
        boolean flag =true;
        //TEST 2.1
        AVLTree avlTree = new AVLTree();
        if (avlTree.search(1) != null) {
            addError("searched 1 in empty tree and result wasn't null",2,1);
            flag = false;
        }
        //TEST 2.2
        avlTree.insert(1, "hello");
        /* if (!avlTree.empty()) {
        System.out.println("empty? = false");} */
        if (!avlTree.search(1).equals("hello")){
            addError("expected string \"hello\" and got something else",2,2);
            flag = false;
        }
        return flag;
    }

    /**
     * @return
     * @pre AVLTree.getRoot() works properly
     */
    public boolean insert() {
        boolean flag =true;
        //TEST 3.1
        AVLTree avlTree = new AVLTree();
        int[] rot = new int[]{0, 0, 1, 0, 2, 0, -1, -1};
        int[] keys = new int[]{1, 2, 3, 5, 4, Integer.MIN_VALUE, 1, 5};
        Item[] items = TestUtils.keysToItems(keys);
        for (int i = 0; i < keys.length; i++) {
        	int res = avlTree.insert(items[i].getKey(), items[i].getInfo());
            if (res != rot[i]) {
                addError(String.format("problem at inserting [%d,\"%s\"], wrong rotation number",
                        items[i].getKey(),items[i].getInfo()),
                        3,1);
                System.out.println("rotations # is " + res + "not" + rot[i]);
                flag = false;
            }
        }
        //TEST 3.2
        if (avlTree.getRoot().getKey() != 2){
            addError(String.format("root is supposed to be [%d,\"%s\"] and is [%d,\"%s\"] instead",
                    2                         ,"2 # 2",
                    avlTree.getRoot().getKey(),avlTree.getRoot().getValue()),
                    3,2);
            flag = false;
        }
        return flag;
    }

    public boolean delete() {
        boolean flag =true;
        //TEST 4.1
        AVLTree avlTree = new AVLTree();
        if (avlTree.delete(1) != -1) {
            addError("does not return -1 on empty tree",4,1);
            flag = false;
        }
        int[] keys = new int[]{1, 2, 3, 6, 5, -1, 4, 7};
        //TEST 4.2
        avlTree = TestUtils.treefromKeys(keys);
        if (avlTree.delete(4) != 0) {
            addError("deleted leaf with 0 rotations, supposed to be 0 rotations, got something else",4,2);
            flag = false;
        }
        //TEST 4.3
        if (avlTree.delete(6) != 0) {
            addError("deleted node with 1 son, supposed to be 0 rotations, got something else",4,3);
            flag = false;
        }
        //TEST 4.4
        avlTree.insert(9, "9 # 9");
        int result = avlTree.delete(3);
        if (result != 1) {
            addError("deleted leaf, supposed to be 1 rotations, got something else",4,4);
            System.out.println("rotations # " + result);
            flag = false;
        }
        //TEST 4.5
        avlTree.insert(8, "8 # 8");
        result = avlTree.delete(5);
        if (result != 2) {
            addError("deleted leaf, supposed to be 2 rotations, got something else",4,5);
            System.out.println("rotations # " + result);
            flag = false;
        }
        //TEST 4.6
        if (avlTree.delete(16) != -1){
            addError("node does not exist, did not return -1",4,6);
            flag = false;
        }
        //TEST 4.7
        avlTree = TestUtils.treefromKeys(new int[]{15,8,22,4,11,20,24,2,9,12,18,13});
        result = avlTree.delete(24);
        if (result != 3){
            addError("example from presentation #3 page 93, deleted leaf expected 3 rotations",4,7);
            System.out.println("rotations # " + result);
            flag = false;
        }

        //sanity check - used in size check
        avlTree = new AVLTree();
        TestUtils.insertKeysToAVLTree(avlTree, TestUtils.createKeysArray(100));
        avlTree.delete(99);
        avlTree.delete(0);
        avlTree.delete(99);
        TestUtils.insertKeysToAVLTree(avlTree, TestUtils.createKeysArray(200, 252));
        for (int i = 220; i <250 ; i++) {
            avlTree.delete(i);
        }

        return flag;
    }

    public boolean size() {
        boolean flag =true;
        //TEST 5.1
        AVLTree avlTree = new AVLTree();
        if (avlTree.size() != 0) {
            addError("size of empty tree was not 0",5,1);
            flag = false;
        }
        TestUtils.insertKeysToAVLTree(avlTree, TestUtils.createKeysArray(100));
        //TEST 5.2
        if (avlTree.size() != 100) {
            addError("added 100 nodes, size was not 100",5,2);
            flag = false;
        }
        //TEST 5.3
        avlTree.delete(99);
        avlTree.delete(0);
        avlTree.delete(99);
        if (avlTree.size() != 98) {
            addError("added 0-99, deleted 99 then 0 then 99 again(shouldn't affect). size expected is 98, got something else",5,3);
            System.out.println("size is " + avlTree.size);
            flag = false;
        }
        //TEST 5.4
        TestUtils.insertKeysToAVLTree(avlTree, TestUtils.createKeysArray(200, 252));
        if(avlTree.size() != 150){
            addError("added 100 nodes, deleted 2 and added 52, size was not 150",5,4);
            System.out.println("size is " + avlTree.size);
            flag = false;
        }
        //TEST 5.5
        for (int i = 220; i <250 ; i++) {
            avlTree.delete(i);
        }
        if (avlTree.size()!=120){
            addError("added 0-99, delted 0 and 99, added 200-251, deleted 220-249. expected size was 120, got something else",5,5);
            System.out.println("size is " + avlTree.size);
            flag = false;
        }
        return flag;
    }

    public boolean minAndMax() {
        boolean flag =true;
        //TEST 6.1
        AVLTree avlTree = new AVLTree();
        if (avlTree.min() != null || avlTree.max() != null) {
            addError("min or max of empty tree was not null",6,1);
            flag = false;
        }
        int[] keys = TestUtils.createKeysArray(100);
        //TEST 6.2
        avlTree = TestUtils.treefromKeys(keys);
        if (!avlTree.min().equals("0 # 0") || !avlTree.max().equals("99 # 99")) {
            addError("inserted 0-99. min was not 0 or max was not 99",6,2);
            flag = false;
        }
        avlTree.insert(-50, "k");
        avlTree.insert(200, "l");
        //TEST 6.3
        if (!avlTree.min().equals("k") || !avlTree.max().equals("l")){
            addError("inserted 0-99, then -50 and 200. min was not -50 or max was not 200",6,3);
            flag = false;
        }
        return flag;
    }

    public boolean keysToArray() {
        boolean flag =true;
        AVLTree avlTree = new AVLTree();
        //TEST 7.1
        if (!Arrays.equals(avlTree.keysToArray(), new int[]{})) {
            addError("keys array of empty tree was not empty array",7,1);
            flag = false;
        }
        int[] keys1 = TestUtils.createKeysArray(100);
        int[] keys2 = TestUtils.createKeysArray(200, 300);
        int[] keys3 = TestUtils.createKeysArray(400, 500);

        TestUtils.insertKeysToAVLTree(avlTree, keys3);
        //TEST 7.2
        if (!Arrays.equals(avlTree.keysToArray(), keys3)) {
            addError("inserted 400-499. keys array wasn't [400,401,...,499]",7,2);
            flag = false;
        }
        int[] keys = TestUtils.combineArrays(keys1, keys3);
        TestUtils.insertKeysToAVLTree(avlTree, keys1);
        //TEST 7.3
        if (!Arrays.equals(avlTree.keysToArray(), keys)) {
            addError("inserted 400-499 then 0-99. keys array wasn't [0,1,2,...,99,400,401,402,...,499]",7,3);
            flag = false;
        }
        keys = TestUtils.combineArrays(keys1, keys2);
        keys = TestUtils.combineArrays(keys, keys3);

        TestUtils.insertKeysToAVLTree(avlTree, keys2);
        //TEST 7.4
        if (!Arrays.equals(avlTree.keysToArray(), keys)){
            addError("inserted 400-499, then 0-99, then 200-299. keys array wasn't [0,1,..,99,200,201,..,299,400,401,..,499]",7,4);
            flag = false;
        }
        return flag;
    }

    public boolean infoToArray() {
        boolean flag =true;
        AVLTree avlTree = new AVLTree();
        //TEST 8.1
        if (!Arrays.equals(avlTree.keysToArray(), new int[]{})) {
            addError("info array of empty tree was not empty array",8,1);
            flag = false;
        }
        int[] keys1 = TestUtils.createKeysArray(100);
        int[] keys2 = TestUtils.createKeysArray(200, 300);
        int[] keys3 = TestUtils.createKeysArray(400, 500);
        String[] infos1 = TestUtils.createInfosArray(100);
        String[] infos2 = TestUtils.createInfosArray(200, 300);
        String[] infos3 = TestUtils.createInfosArray(400, 500);
        TestUtils.insertKeysToAVLTree(avlTree, keys3);
        //TEST 8.2
        if (!Arrays.equals(avlTree.infoToArray(), infos3)) {
            addError("inserted 400-499. info array wasn't [\"400 # 400\",\"401 # 401\",...,\"499 # 499\"]",8,2);
            flag = false;
        }
        String[] infos = TestUtils.combineArrays(infos1, infos3);
        TestUtils.insertKeysToAVLTree(avlTree, keys1);
        //TEST 8.3
        if (!Arrays.equals(avlTree.infoToArray(), infos)) {
            addError("inserted 400-499 then 0-99. info array wasn't [\"0 # 0\",\"1 # 1\",..,\"99 # 99\",\"401 # 401\",..,\"499 # 499\"]",8,3);
            flag = false;
        }
        infos = TestUtils.combineArrays(infos1, infos2);
        infos = TestUtils.combineArrays(infos, infos3);

        TestUtils.insertKeysToAVLTree(avlTree, keys2);
        //TEST 8.4
        if (!Arrays.equals(avlTree.infoToArray(), infos)){
            addError("inserted 400-499, then 0-99, then 200-299. keys array wasn't [\"0 # 0\",\"1 # 1\",..,\"99 # 99\",\"200 # 200\",\n\t\t\"201 # 201\",..,\"299 # 299\",\"400 # 400\",\n\t\t\"401 # 401\",..,\"499 # 499\"]",8,4);
            flag = false;
        }
        return flag;
    }


    public static void main(String[] args) {
        TestStage1 tester = new TestStage1();
        Exception exception = null;
        String msg="empty()";
        try {
            if (tester.empty()) {
                TestUtils.printSuccess("empty()");
            } else {
                TestUtils.printFail("empty()");
            }
            msg="search()";
            if (tester.search()) {
                TestUtils.printSuccess("search()");
            } else {
                TestUtils.printFail("search()");
            }
            msg="insert()";
            if (tester.insert()) {
                TestUtils.printSuccess("insert()");
            } else {
                TestUtils.printFail("insert()");
            }
            msg="delete()";
            if (tester.delete()) {
                TestUtils.printSuccess("delete()");
            } else {
                TestUtils.printFail("delete()");
            }
            msg="size()";
            if (tester.size()) {
                TestUtils.printSuccess("size()");
            } else {
                TestUtils.printFail("size()");
            }
            msg="minAndMax()";
            if (tester.minAndMax()) {
                TestUtils.printSuccess("minAndMax()");
            } else {
                TestUtils.printFail("minAndMax()");
            }
            msg="keysToArray()";
            if (tester.keysToArray()) {
                TestUtils.printSuccess("keysToArray()");
            } else {
                TestUtils.printFail("keysToArray()");
            }
            msg="infoToArray()";
            if (tester.infoToArray()) {
                TestUtils.printSuccess("infoToArray()");
            } else {
                TestUtils.printFail("infoToArray()");
            }
        }
        catch (Exception e) {
            exception = e;
            System.out.println("\nthere was an exception(details below)\n");
        }
        finally {
            if (tester.getError()!="") {
                System.out.println("============================================");
                System.out.println("errors details:\n");
                System.out.println(tester.getError());
            }

            if (exception!=null){
                System.out.println("============================================");
                System.out.printf("%nException at %s%n%nException details :%n%n", msg);
                exception.printStackTrace(System.out);
            }
            System.out.println("\n\n===============TEST ENDED===============");
        }

    }



}

class TestUtils {

    //printing methods
    public static void printProblem(String msg, int i){
        System.out.printf("problem at %s() test #%d%n",msg,i);
    }
    public static void printFail(int main, int sub){
        System.out.printf("Error %d.%d%n",main,sub);
    }
    public static void printFail(int main){
        System.out.printf("Error %d%n",main);
    }
    public static void printFail(String main){
        System.out.printf("Error %s%n",main);
    }
    public static void printSuccess(int main, int sub){
        System.out.printf("Passed %d.%d%n",main,sub);
    }
    public static void printSuccess(int main){
        System.out.printf("Passed %d%n",main);
    }
    public static void printSuccess(String main){
        System.out.printf("Passed %s%n",main);
    }


    public static AVLTree treefromKeys(int[] keys){
        return treeFromItems(keysToItems(keys));
    }

    public static int[] createKeysArray(int start, int end) {
        int[] res = new int[end - start];
        for (int i = start; i < end; i++) {
            res[i - start] = i;
        }
        return res;
    }

    public static int[] createKeysArray(int end){
        return createKeysArray(0, end);
    }

    public static void insertItemsToAVLTree(AVLTree avlTree, Item[] items){
        for (Item item :
                items) {
            avlTree.insert(item.getKey(),item.getInfo());
        }
    }

    public static void insertKeysToAVLTree(AVLTree avlTree,int[] keys){
        insertItemsToAVLTree(avlTree, keysToItems(keys));
    }
    public static AVLTree treeFromItems(Item[] array){
        AVLTree tree = new AVLTree();
        for (Item item :
                array) {
            tree.insert(item.getKey(), item.getInfo());
        }
        return tree;
    }

    public static Item[] keysToItems(int[] array){
        Item[] res = new Item[array.length];
        for (int i = 0; i < res.length; i++) {
            res[i]= new Item(array[i],String.format("%d # %d",array[i],array[i]));
        }
        return res;
    }

    public static int[] combineArrays(int[] arr1, int[] arr2){
        int size1 = arr1.length;
        int size2 = arr2.length;
        int size = size1+size2;
        int[] res = new int[size];
        for (int i = 0; i < size; i++) {
            if (i<size1){
                res[i]=arr1[i];
            }
            else{
                res[i]=arr2[i-size1];
            }
        }
        return res;
    }

    public static String[] combineArrays(String[] arr1, String[] arr2){
        int size1 = arr1.length;
        int size2 = arr2.length;
        int size = size1+size2;
        String[] res = new String[size];
        for (int i = 0; i < size; i++) {
            if (i<size1){
                res[i]=arr1[i];
            }
            else{
                res[i]=arr2[i-size1];
            }
        }
        return res;
    }

    public static String[] keysToInfos(int[] keys){
        String[] res = new String[keys.length];
        for (int i = 0; i < res.length; i++) {
            res[i]=keys[i]+" # "+keys[i];
        }
        return res;
    }

    public static String[] createInfosArray(int start, int end) {
        int[] keys = createKeysArray(start, end);
        return keysToInfos(keys);
    }

    public static String[] createInfosArray(int end) {
        int[] keys = createKeysArray(end);
        return keysToInfos(keys);
    }
}
