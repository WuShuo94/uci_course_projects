import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class TestCosts {
    @Test
    public void test() {
        int iterationSeq = 1;
        int iteration = 2000;
        int maxSize = 1<<12;
        int[] searchSeqCosts = new int[maxSize+1];
        int[] insertSeqCosts = new int[maxSize+1];
        int[] deleteSeqCosts = new int[maxSize+1];
        double[] searchAvgCosts = new double[maxSize+1];
        double[] insertAvgCosts = new double[maxSize+1];
        double[] deleteAvgCosts = new double[maxSize+1];

        HashSet<Integer> set = new HashSet<>(2*maxSize);
        ArrayList<Integer> listRandom = new ArrayList<>(maxSize);
        ArrayList<Integer> listSeq = new ArrayList<>(maxSize);

        int tmp = 0;
        while(tmp < maxSize) {
            listSeq.add(tmp++);
        }
        tmp = 0;
        Random random = new Random();
        while(listRandom.size() < maxSize) {
            tmp = random.nextInt(Integer.MAX_VALUE);
            if(set.contains(tmp))
                continue;
            set.add(tmp);
            listRandom.add(tmp);
        }

        searchSeqCosts[0] = 1;
        insertSeqCosts[0] = 1;
        deleteSeqCosts[0] = 1;

        searchAvgCosts[0] = 1.0;
        insertAvgCosts[0] = 1.0;
        deleteAvgCosts[0] = 1.0;

        // test sequential data set
        for (int i = 0; i < iterationSeq; i++) {
//            BinaryTree t = new BinaryTree();
//            AVLTree t= new AVLTree();
            SplayTree t= new SplayTree();
//            Treap t = new Treap();

            for (int j = 0; j < maxSize; j++) {
                tmp = t.insertAndCount(listSeq.get(j));
                insertSeqCosts[j+1] += tmp;

                tmp = (int)t.searchAndCount(listSeq.get(j))[1];
                searchSeqCosts[j+1] += tmp;
            }
            for (int j = maxSize-1; j > -1; j--) {
                tmp = t.deleteAndCount(listSeq.get(j));
                deleteSeqCosts[t.size+1] += tmp;
            }
        }
        for (int i = 0; i < maxSize; i++) {
            insertSeqCosts[i+1] /= (double)iterationSeq;
            searchSeqCosts[i+1] /= (double)iterationSeq;
            deleteSeqCosts[i+1] /= (double)iterationSeq;
        }

        // test random data set
        for (int i = 0; i < iteration; i++) {
//            BinaryTree t = new BinaryTree();
//            AVLTree t= new AVLTree();
            SplayTree t= new SplayTree();
//            Treap t = new Treap();

            Collections.shuffle(listRandom);
            ArrayList<Integer> tmpList = new ArrayList<>(maxSize);
            for (int j = 0; j < maxSize; j++) {
                tmp = t.insertAndCount(listRandom.get(j));
                tmpList.add(listRandom.get(j));
                insertAvgCosts[j+1] += tmp;

                tmp = (int)t.searchAndCount(tmpList.get(random.nextInt(tmpList.size())))[1];
                searchAvgCosts[j+1] += tmp;
            }

            Collections.shuffle(listRandom);
            for (int j = 0; j < maxSize; j++) {
                tmp = t.deleteAndCount(listRandom.get(j));
                deleteAvgCosts[t.size+1] += tmp;
            }
        }
        for (int i = 0; i < maxSize; i++) {
            searchAvgCosts[i+1] /= (double)iteration;
            insertAvgCosts[i+1] /= (double)iteration;
            deleteAvgCosts[i+1] /= (double)iteration;
        }

        StringBuffer s1 = new StringBuffer("searchSeqCosts = [");
        StringBuffer s2 = new StringBuffer("insertSeqCosts = [");
        StringBuffer s3 = new StringBuffer("deleteSeqCosts = [");
        StringBuffer s4 = new StringBuffer("searchAvgCosts = [");
        StringBuffer s5 = new StringBuffer("insertAvgCosts = [");
        StringBuffer s6 = new StringBuffer("deleteAvgCosts = [");
        String tmpS;

        for (int i = 0; i < maxSize+1; i++) {
            s1.append(searchSeqCosts[i]);
            s1.append(",");
            s2.append(insertSeqCosts[i]);
            s2.append(",");
            s3.append(deleteSeqCosts[i]);
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
        s1.append("];");
        s2.append("];");
        s3.append("];");
        s4.deleteCharAt(s4.length()-1);
        s5.deleteCharAt(s5.length()-1);
        s6.deleteCharAt(s6.length()-1);
        s4.append("];");
        s5.append("];");
        s6.append("];");

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(" ");
        System.out.println(s4);
        System.out.println(s5);
        System.out.println(s6);
    }

    @Test
    public void testSplayWorst() {
        int maxSize = 1<<12;
        int[] searchSeqCosts = new int[maxSize+1];
        int[] insertSeqCosts = new int[maxSize+1];
        int[] deleteSeqCosts = new int[maxSize+1];
        ArrayList<Integer> listSeq = new ArrayList<>(maxSize);

        int tmp = 0;
        while(tmp < maxSize) {
            listSeq.add(tmp++);
        }

        searchSeqCosts[0] = 1;
        insertSeqCosts[0] = 1;
        deleteSeqCosts[0] = 1;

        // test sequential data set
        SplayTree t= new SplayTree();

        for (int j = 0; j < maxSize; j++) {
            insertSeqCosts[j+1] += t.insertAndCount(listSeq.get(j));
            //worst case for search
            searchSeqCosts[j+1] += (int)t.searchAndCount(listSeq.get(0))[1];
        }

        //when j = 0, worst case for delete
        for (int j = 0; j < maxSize; j++) {
            tmp = t.deleteAndCount(listSeq.get(j));
            deleteSeqCosts[t.size+1] += tmp;
        }

        StringBuffer s1 = new StringBuffer("searchSeqCosts = [");
        StringBuffer s2 = new StringBuffer("insertSeqCosts = [");
        StringBuffer s3 = new StringBuffer("deleteSeqCosts = [");

        for (int i = 0; i < maxSize+1; i++) {
            s1.append(searchSeqCosts[i]);
            s1.append(",");
            s2.append(insertSeqCosts[i]);
            s2.append(",");
            s3.append(deleteSeqCosts[i]);
            s3.append(",");
        }
        s1.deleteCharAt(s1.length()-1);
        s2.deleteCharAt(s2.length()-1);
        s3.deleteCharAt(s3.length()-1);
        s1.append("];");
        s2.append("];");
        s3.append("];");

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
    }
}
