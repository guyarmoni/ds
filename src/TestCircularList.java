import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * tester from WhatsApp group, sent by +972 52-844-3458 ~barh50
 * at 24/04/2020, 13:29pm
 *
 * if prints 'DONE' - all good.
 *
 */


public class TestCircularList {
    public static void main(String[] args) {
        int k,insertIndex,delIndex;
        String s;
        int count=0;
        boolean check=true;
        Random rd = new Random();
        for(int j=0;j<10000;j++) {
            int length=rd.nextInt(10000);
            System.out.println("Test number: "+j);
            System.out.println("length of array: "+length);
            List<Integer> compare = new ArrayList<Integer>();
            CircularList x = new CircularList(length);
            for (int i = 0; i < x.len; i++) {
                insertIndex= rd.nextInt(count); // storing random integers in an array
                k=rd.nextInt(length);
                s=String.valueOf(k);
                compare.add(insertIndex,k);
                x.insert(insertIndex, k, s);
                count++;
            }
            if(x.len!=compare.size()) {
                System.out.println("Error!!! insert, not same size");
                check=false;
            }
            for (int i=0;i<x.len;i++) {
                if (x.retrieve(i).getKey()!=compare.get(i)) {
                    System.out.println("Error!!! insert, put breakpoint here");
                    check=false;
                }
                if (x.retrieve(i).getKey()!=compare.get(i)) {
                    System.out.println("check");
                    check=false;
                }
                // printing each array element
            }
            for (int i=0; i<(x.len/2);i++)
            {
                delIndex=rd.nextInt(x.len);
                compare.remove(delIndex);
                x.delete(delIndex);
            }
            if(x.len!=compare.size())
            {
                System.out.println("Error!!! delete, not same size");
                check=false;
            }
            for (int i=0;i<x.len;i++) {
                if (x.retrieve(i).getKey()!=compare.get(i)) {
                    System.out.println("Error!!! delete, put breakpoint here");
                    check=false;
                }
                if (x.retrieve(i).getKey()!=compare.get(i)) {
                    System.out.println("check");
                    check=false;
                }
                // printing each array element
            }
            count=0;
            System.out.println("");
        }
        if (check) System.out.println("Done");
        else System.out.println("there was an error! check where");


    }
}
