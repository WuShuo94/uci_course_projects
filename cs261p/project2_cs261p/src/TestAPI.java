import org.junit.Test;

import java.util.*;

public class TestAPI {
    @Test
    public void test1() {
        BinaryTree tree = new BinaryTree();
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        tree.insert(25);
        tree.insert(50);
        tree.insert(5);
        tree.insert(15);

        System.out.println(tree);
        System.out.println(" ");

        tree.delete(15);
        System.out.println(tree);
        System.out.println(" ");

        tree.delete(10);
        System.out.println(tree);
        System.out.println(" ");

        tree.insert(45);
        tree.insert(46);
        tree.insert(60);
        tree.delete(30);
        System.out.println(tree);
        System.out.println(" ");

        tree.insert(70);
        tree.delete(60);
        System.out.println(tree);
        System.out.println(" ");

        tree.delete(5);
        System.out.println(tree);
        System.out.println(" ");

        tree.delete(20);
        System.out.println(tree);
        System.out.println(" ");

        tree.insert(5);
        tree.insert(6);
        tree.insert(4);
        tree.delete(5);
        System.out.println(tree);
        System.out.println(" ");
    }

    @Test
    public void test2() {
        AVLTree tree = new AVLTree();
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        tree.insert(25);
        tree.insert(50);

        System.out.println(tree);
        System.out.println(" ");

        tree.insert(45);
        System.out.println(tree);
        System.out.println(" ");

        tree.insert(40);
        System.out.println(tree);
        System.out.println(" ");

        tree.insert(5);
        tree.insert(7);
        System.out.println(tree);
        System.out.println(" ");

        tree.delete(20);
        System.out.println(tree);
        System.out.println(" ");

        tree.insert(9);
        tree.insert(20);
        tree.insert(28);
        tree.insert(38);
        tree.insert(42);
        tree.insert(48);
        tree.insert(55);

        tree.insert(3);
        tree.insert(15);
        tree.insert(23);
        tree.insert(27);
        tree.insert(36);
        tree.insert(47);
        tree.insert(49);
        tree.insert(60);
        tree.insert(46);

        System.out.println(tree);
        System.out.println(" ");


        tree.delete(40);
        System.out.println(tree);
        System.out.println(" ");
    }

    @Test
    public void test3() {
        SplayTree t = new SplayTree();
        t.insert(100);
        t.insert(50);
        System.out.println(t);
        System.out.println(" ");

        t.insert(200);
        System.out.println(t);
        System.out.println(" ");

        t.insert(25);
        System.out.println(t);
        System.out.println(" ");

        t.insert(35);
        System.out.println(t);
        System.out.println(" ");

        t.insert(40);
        System.out.println(t);
        System.out.println(" ");

        t.insert(20);
        System.out.println(t);
        System.out.println(" ");

        t.delete(200);
        System.out.println(t);
        System.out.println(" ");
    }

    @Test
    public void test4() {
        Treap t = new Treap();

        t.insert(100);
        t.insert(50);
        System.out.println(t);
        System.out.println(" ");

        t.insert(200);
        System.out.println(t);
        System.out.println(" ");

        t.insert(25);
        System.out.println(t);
        System.out.println(" ");

        t.insert(35);
        System.out.println(t);
        System.out.println(" ");

        t.insert(40);
        System.out.println(t);
        System.out.println(" ");

        t.insert(20);
        System.out.println(t);
        System.out.println(" ");

        t.delete(40);
        System.out.println(t);
        System.out.println(" ");
    }

    @Test
    public void testBinaryTree0() {
        int iteration = 100;
        int maxHeight = 20;
        int[] searchWorstCosts = new int[maxHeight+1];
        int[] insertWorstCosts = new int[maxHeight+1];
        int[] deleteWorstCosts = new int[maxHeight+1];
        double[] searchAvgCosts = new double[maxHeight+1];
        double[] insertAvgCosts = new double[maxHeight+1];
        double[] deleteAvgCosts = new double[maxHeight+1];

        HashSet<Integer> set = new HashSet<>(1<<(maxHeight+1));
        ArrayList<Integer> list = new ArrayList<>(1<<maxHeight);

        int tmp = 0;
        Random random = new Random();
        while(list.size() < 1<<maxHeight) {
            tmp = random.nextInt(Integer.MAX_VALUE);
            if(set.contains(tmp))
                continue;
            set.add(tmp);
            list.add(tmp);
        }

        searchWorstCosts[0] = 1;
        insertWorstCosts[0] = 1;
        deleteWorstCosts[0] = 1;

        searchAvgCosts[0] = 1.0;
        insertAvgCosts[0] = 1.0;
        deleteAvgCosts[0] = 1.0;

        for (int i = 1; i < maxHeight+1; i++) {
            searchWorstCosts[i] = 1;
            insertWorstCosts[i] = 1;
            deleteWorstCosts[i] = 1;

            searchAvgCosts[i] = 0.0;
            insertAvgCosts[i] = 0.0;
            deleteAvgCosts[i] = 0.0;

            int k = 0, counter1 = 0, counter2 = 0;

            for (int j = 0; j < iteration; j++) {
                k = 0;
                BinaryTree t = new BinaryTree();
                ArrayList<Integer> sublist = new ArrayList<>();
                Collections.shuffle(list);
                while(true) {
                    tmp = t.insertAndCount(list.get(k));
                    sublist.add(list.get(k));
                    if(t.root.height > i)
                        break;

                    if(t.root.height == i) {
                        insertWorstCosts[i] = Math.max(insertWorstCosts[i], tmp);
                        insertAvgCosts[i] += tmp;

                        tmp = (int)t.searchAndCount((int)list.get(k))[1];
                        searchWorstCosts[i] = Math.max(searchWorstCosts[i], tmp);
                        searchAvgCosts[i] += tmp;

                        counter1++;
                    }
                    k++;
                }
                Collections.shuffle(sublist);
                k = 0;
                while(t.root != null && t.root.height >= i && k < sublist.size()) {
                    tmp = t.deleteAndCount(sublist.get(k++));
                    deleteWorstCosts[i] = Math.max(deleteWorstCosts[i], tmp);
                    if(t.root != null && t.root.height == i) {
                        deleteAvgCosts[i] += tmp;
                        counter2++;
                    }
                }

            }
            searchAvgCosts[i] /= (double)counter1;
            insertAvgCosts[i] /= (double)counter1;
            deleteAvgCosts[i] /= (double)counter2;
        }

        StringBuffer s1 = new StringBuffer("[");
        StringBuffer s2 = new StringBuffer("[");
        StringBuffer s3 = new StringBuffer("[");
        StringBuffer s4 = new StringBuffer("[");
        StringBuffer s5 = new StringBuffer("[");
        StringBuffer s6 = new StringBuffer("[");
        String tmpS;


        for (int i = 0; i < maxHeight+1; i++) {
            s1.append(searchWorstCosts[i]);
            s1.append(",");
            s2.append(insertWorstCosts[i]);
            s2.append(",");
            s3.append(deleteWorstCosts[i]);
            s3.append(",");

            tmpS = String.valueOf(searchAvgCosts[i]);
            if(tmpS.length() > 4)
                tmpS = tmpS.substring(0, 4);
            s4.append(tmpS);
            s4.append(",");

            tmpS = String.valueOf(insertAvgCosts[i]);
            if(tmpS.length() > 4)
                tmpS = tmpS.substring(0, 4);
            s5.append(tmpS);
            s5.append(",");

            tmpS = String.valueOf(deleteAvgCosts[i]);
            if(tmpS.length() > 4)
                tmpS = tmpS.substring(0, 4);
            s6.append(tmpS);
            s6.append(",");
        }
        s1.deleteCharAt(s1.length()-1);
        s2.deleteCharAt(s2.length()-1);
        s3.deleteCharAt(s3.length()-1);
        s1.append("]");
        s2.append("]");
        s3.append("]");
        s4.deleteCharAt(s4.length()-1);
        s5.deleteCharAt(s5.length()-1);
        s6.deleteCharAt(s6.length()-1);
        s4.append("]");
        s5.append("]");
        s6.append("]");

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);
        System.out.println(s5);
        System.out.println(s6);
    }
}
