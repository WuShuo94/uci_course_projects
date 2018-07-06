import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class test {
    @Test
    public void testCuckoo() {
        double start, end, avgSet = 0, avgDelete = 0, avgSearch = 0;
        double loadFactor = 0;
        double[] loadFactors = new double[100];
        double[] setCosts = new double[100];
        double[] deleteCosts = new double[100];
        double[] searchCosts = new double[100];

        Random random = new Random();

        int stepSize = 500;
        int avgTime = 2000;

        for (int i = 0; i < 100; i++) {
            avgSet = 0;
            avgDelete = 0;
            avgSearch = 0;
            loadFactor = 0;
            for (int j = 0; j < avgTime; j++) {
                CuckooHashing h = new CuckooHashing(100*stepSize);

                // generate random keys
                List<Integer> list = new ArrayList<>(i*stepSize);
                for (int k = 0; k < i*stepSize; k++) {
                    list.add(random.nextInt(1000000));
                }

                start = System.currentTimeMillis();
                for (int k = 0; k < i*stepSize; k++) {
                    h.set(list.get(k), list.get(k));
                }
                end = System.currentTimeMillis();
                avgSet += (end - start);
                loadFactor += h.getLoadFactor();

                Collections.shuffle(list);
                start = System.currentTimeMillis();
                for (int k = 0; k < stepSize && k < i*stepSize; k++) {
                    h.search(list.get(k));
                }
                end = System.currentTimeMillis();
                avgSearch += (end - start);

                Collections.shuffle(list);
                start = System.currentTimeMillis();
                for (int k = 0; k < stepSize && k < i*stepSize; k++) {
                    h.delete(list.get(k));
                }
                end = System.currentTimeMillis();
                avgDelete += (end - start);
            }

            loadFactors[i] = loadFactor/avgTime;

            setCosts[i] = avgSet / avgTime;
            System.out.println("i = " + i + ", set: " + setCosts[i]);

            deleteCosts[i] = avgDelete / avgTime;
            System.out.println("i = " + i + ", delete: " + deleteCosts[i]);

            searchCosts[i] = avgSearch / avgTime;
            System.out.println("i = " + i + ", search: " + searchCosts[i]);

        }

        System.out.println("");

        System.out.println("Load Factors: [");
        for(double i : loadFactors) {
            System.out.print(i + ", ");
        }
        System.out.println("]");

        System.out.println("");

        System.out.println("Sets Sequence: [");
        for(double i : setCosts) {
            System.out.print(i + ", ");
        }
        System.out.println("]");

        System.out.println("");

        System.out.println("Delete: [");
        for(double i : deleteCosts) {
            System.out.print(i + ", ");
        }
        System.out.println("]");

        System.out.println("");

        System.out.println("Search: [");
        for(double i : searchCosts) {
            System.out.print(i + ", ");
        }
        System.out.println("]");
    }

    // Warning: comment throw exception code of delete before using this test
    @Test
    public void countTestProbingAndChaining() {
        double avgSet = 0, avgDelete = 0, avgSearch = 0;;
        double loadFactor = 0;
        double[] loadFactors = new double[100];
        double[] setCosts = new double[100];
        double[] deleteCosts = new double[100];
        double[] searchCosts = new double[100];

        Random random = new Random();

        int stepSize = 1000;
        int avgTime = 100;

        for (int i = 0; i < 100; i++) {
            avgSet = 0;
            avgDelete = 0;
            avgSearch = 0;
            loadFactor = 0;
            for (int j = 0; j < avgTime; j++) {
//                LinearHashing h = new LinearHashing(100*stepSize);
//                QuadraticHashing h = new QuadraticHashing(100*stepSize);
                ChainedHashing h = new ChainedHashing(100*stepSize);

                // generate random keys
                List<Integer> list = new ArrayList<>((i+1)*stepSize);
                for (int k = 0; k < (i+1)*stepSize; k++) {
                    list.add(random.nextInt(1000000));
                }

                for (int k = 0; k < (i+1)*stepSize; k++) {
                    h.set(list.get(k), list.get(k));
                }

                for (int k = i*stepSize; k < (i+1)*stepSize; k++) {
                    avgSet += h.setAndCount(list.get(k), list.get(k));
                }

                loadFactor += h.getLoadFactor();

                Collections.shuffle(list);
                for (int k = 0; k < stepSize && k < i*stepSize; k++) {
                    avgSearch += h.searchAndCount(list.get(k))[1];
                }

                Collections.shuffle(list);
                for (int k = 0; k < stepSize && k < i*stepSize; k++) {
                    avgDelete += h.deleteAndCount(list.get(k));
                }
            }

            loadFactors[i] = loadFactor/avgTime;

            setCosts[i] = avgSet / avgTime;
            System.out.println("i = " + i + ", set: " + setCosts[i]);

            deleteCosts[i] = avgDelete / avgTime;
            System.out.println("i = " + i + ", delete: " + deleteCosts[i]);

            searchCosts[i] = avgSearch / avgTime;
            System.out.println("i = " + i + ", search: " + searchCosts[i]);

        }

        System.out.println("");

        System.out.print("[");
        for(double i : loadFactors) {
            System.out.print(i + ", ");
        }
        System.out.println("];");

        System.out.println("");

        System.out.print("[");
        for(double i : setCosts) {
            System.out.print(i + ", ");
        }
        System.out.println("];");

        System.out.println("");

        System.out.print("[");
        for(double i : deleteCosts) {
            System.out.print(i + ", ");
        }
        System.out.println("];");

        System.out.println("");

        System.out.print("[");
        for(double i : searchCosts) {
            System.out.print(i + ", ");
        }
        System.out.println("];");
    }

    @Test
    public void testResizeOfProbing() {
        int length = 10000;
        int avgTime = 1000;

        double[] sizes = new double[length];
        double[] setCosts = new double[length];

        Random random = new Random();

        for (int i = 0; i < avgTime; i++) {
//            LinearHashing h = new LinearHashing(512);
            QuadraticHashing h = new QuadraticHashing(512);

            // generate random keys
            List<Integer> list = new ArrayList<>(length);
            for (int k = 0; k < length; k++) {
                list.add(random.nextInt(1000000));
            }

            for (int k = 0; k < length; k++) {
                setCosts[k] += h.setAndCount(list.get(k), list.get(k));
                sizes[k] += h.size;
            }

        }

        for (int i = 0; i < setCosts.length; i++) {
            setCosts[i] /= avgTime;
            sizes[i] /= avgTime;
        }

        System.out.println("");

        System.out.print("[");
        for(double i : sizes) {
            System.out.print(i + ", ");
        }
        System.out.println("];");

        System.out.println("");

        System.out.print("[");
        for(double i : setCosts) {
            System.out.print(i + ", ");
        }
        System.out.println("];");

    }


}
