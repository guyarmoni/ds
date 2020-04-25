import java.util.Random;

public class Measurements {

    public static void main(String[] args) {
        measure(Scenario.LAST, 1);
    }

    public static void measure(Scenario scenario, int i) {
        int listSize;
        int key = 1;
        String value = "a";

        for (int j = 1; j <= i; j++) {
            listSize = j * 10;
            System.out.println("~~~~~ List Size: " + listSize + " ~~~~~" + "\n");
            int k = 0;

            /* measuring TreeList */
            TreeList treeList = new TreeList();
            long startTime = System.nanoTime();
            while (k < listSize) {
                treeList.insert(scenario.insertionIndex(k), key, value);
                k++;
            }
            long endTime = System.nanoTime();
            System.out.println("Tree List -");
            System.out.println("Average Num of Right Rotations: " + treeList.tree.getRightRotations()/listSize);
            System.out.println("Average Num of Left Rotations: " + treeList.tree.getLeftRotations()/listSize);
            System.out.println("Average Insertion Time (in milliseconds): "
                    + ( (endTime - startTime)/1000000 )/listSize + "\n");

            /* measuring CircularList */
            k = 0;
            CircularList circularList = new CircularList(listSize+1);
            startTime = System.nanoTime();
            while (k < listSize) {
                circularList.insert(scenario.insertionIndex(k), key, value);
                k++;
            }
            endTime = System.nanoTime();
            System.out.println("Circular List -");
            System.out.println("Average Insertion Time (in milliseconds): "
                    + ( (endTime - startTime)/1000000 )/listSize + "\n");
        }
    }


    /**
     *
     * public enum Scenario
     *
     * containing the 3 possible scenarios of insertion,
     * each has a different insertion index to the list
     *
     **/
    public enum Scenario {
        LAST, MIDDLE, UNIFORM;

        /** Returns the suitable index for current insertion to the list **/
        public int insertionIndex(int k) {
            switch(this) {
                case LAST: // index is end of list
                    return k;
                case MIDDLE: // index is middle of the list
                    return k/2;
                case UNIFORM: // index is randomly chosen
                    return new Random().nextInt(k+1);
                default: throw new AssertionError(this);
            }
        }
    }
}
