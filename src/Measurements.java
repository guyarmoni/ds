import java.util.Random;

public class Measurements {

    public void measure(int i, Scenario scenario) {

    }


    /**
     * public enum Scenario
     * containing the 3 possible scenarios of insertion,
     * each has a different insertion index to the list
     *
     **/
    public enum Scenario {
        LAST, MIDDLE, UNIFORM;

        /** Returns the suitable index for current insertion to the list **/
        public int insertionIndex(int i) {
            switch(this) {
                case LAST:
                    return i;
                case MIDDLE:
                    return i/2;
                case UNIFORM:
                    return new Random().nextInt(i+1);
                default: throw new AssertionError(this);
            }
        }
    }
}
