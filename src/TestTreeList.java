//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class TestTreeList {
//
//
//    int k,insertIndex,delIndex;
//    String s;
//    int count=0;
//    boolean check=true;
//    Random rd = new Random();
//		for(int j=0;j<200;j++) {
//        int length=rd.nextInt(10000);
//        //int length=6;
//        System.out.println("Test number: "+j);
//        System.out.println("length of array: "+length);
//        List<Integer> compare = new ArrayList<Integer>();
//        TreeList x = new TreeList();
//        for (int i = 0; i < length; i++) {
//            insertIndex= rd.nextInt(count+1); // storing random integers in an array
//            k=rd.nextInt(length);
//            s=String.valueOf(k);
//            compare.add(insertIndex,k);
//            x.insert(insertIndex, k, s);
//            count++;
//        }
//        if(x.tree.size()!=compare.size()) {
//            System.out.println("Error!!! insert, not same size");
//            check=false;
//        }
//        for (int i=0;i<x.tree.size();i++) {
//            if (x.retrieve(i).getKey()!=compare.get(i)) {
//                System.out.println("Error!!! insert, put breakpoint here");
//                check=false;
//            }
//            if (x.retrieve(i).getKey()!=compare.get(i)) {
//                System.out.println("check");
//                check=false;
//            }
//            // printing each array element
//        }
//        int[] checkk=x.list.keysToArray();
//        //System.out.println("AFter insert");
//        //System.out.println("Arr tree: "+Arrays.toString(checkk));
//        //System.out.println("Arr list: "+Arrays.toString(compare.toArray()));
//        //BTreePrinter.printNode(x.list.getRoot());
//        for (int i=0; i<(length/2);i++)
//        {
//            delIndex=rd.nextInt(x.len);
//            //delIndex=0;
//            System.out.println("remove index: "+delIndex);
//            //BTreePrinter.printNode(x.list.getRoot());
//            compare.remove(delIndex);
//            x.delete(delIndex);
//            checkk=x.list.keysToArray();
//            //System.out.println("Arr tree: "+Arrays.toString(checkk));
//            //System.out.println("Arr list: "+Arrays.toString(compare.toArray()));
//            //BTreePrinter.printNode(x.list.getRoot());
//        }
//        if(x.len!=compare.size())
//        {
//            System.out.println("Error!!! delete, not same size");
//            //BTreePrinter.printNode(x.list.getRoot());
//            check=false;
//        }
//        checkk=x.list.keysToArray();
//        //System.out.println("Arr tree: "+Arrays.toString(checkk));
//        //System.out.println("Arr list: "+Arrays.toString(compare.toArray()));
//        //BTreePrinter.printNode(x.list.getRoot());
//        for (int i=0;i<x.len;i++) {
//            if (x.retrieve(i).getKey()!=compare.get(i)) {
//                System.out.println("Error!!! delete, put breakpoint here");
//                int temptree=x.retrieve(i).getKey();
//                int templist=compare.get(i);
//                check=false;
//            }
//            if (x.retrieve(i).getKey()!=compare.get(i)) {
//                System.out.println("check");
//                check=false;
//            }
//            // printing each array element
//        }
//        count=0;
//        System.out.println("");
//    }
//		if (check) System.out.println("Done");
//		else System.out.println("there was an error! check where");
//}
//	}
//
//}
