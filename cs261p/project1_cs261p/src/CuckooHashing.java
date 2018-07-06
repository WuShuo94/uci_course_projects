import java.util.Random;

public class CuckooHashing {

    class Entry {
        int key, value;

        Entry(int key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + ":" + value;
        }
    }


    private Entry[] table1;
    private Entry[] table2;
    private int a = 37452, b = 43807;
    int size;
    int capacity;

    public CuckooHashing() {
        table1 = new Entry[16];
        table2 = new Entry[16];
        capacity = 16;
        size = 0;
    }

    public CuckooHashing(int initialCapacity) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        table1 = new Entry[initialCapacity];
        table2 = new Entry[initialCapacity];
        capacity = initialCapacity;
        size = 0;
    }

    public int search(int key) {
        int index = indexFor(hash1(key), capacity);
        if(table1[index].key == key)
            return table1[index].value;

        index = indexFor(hash2(key), capacity);
        if(table2[index].key == key)
            return table2[index].value;

        throw new IllegalArgumentException("Element is Not Found ");
    }

    public void set(int key, int value) {
        if(table1[indexFor(hash1(key), capacity)] != null && table1[indexFor(hash1(key), capacity)].key == key) {
            table1[indexFor(hash1(key), capacity)].value = value;
            return;
        }

        if(table2[indexFor(hash2(key), capacity)] != null && table2[indexFor(hash2(key), capacity)].key == key) {
            table2[indexFor(hash2(key), capacity)].value = value;
            return;
        }

        int t = 0, counter = 0, index = 0;
        Entry entry = new Entry(key, value);
        Entry tmp;

        index = indexFor(hash1(entry.key), capacity);
        tmp = table1[index];
        table1[index] = entry;
        entry = tmp;
        t = 1;
        counter++;

        while(entry != null && counter <= (size+1)) {
            if(t == 0) {
                index = indexFor(hash1(entry.key), capacity);
                tmp = table1[index];
                table1[index] = entry;
                entry = tmp;
            } else {
                index = indexFor(hash2(entry.key), capacity);
                tmp = table2[index];
                table2[index] = entry;
                entry = tmp;
            }
            t = 1 - t;
            counter++;
        }

        if(counter > (size+1)) {
            rehash();
            set(entry.key, entry.value);
        }
        size++;
    }

    public void delete(int key) {
        int index1 = indexFor(hash1(key), capacity);
        int index2 = indexFor(hash2(key), capacity);
        if(table1[index1] != null && table1[index1].key == key) {
            table1[index1] = null;
            size--;
        } else if(table2[index2] != null && table2[index2].key == key) {
            table2[index2] = null;
            size--;
        } else
            throw new IllegalArgumentException("Element is Not Found ");
//            return;
    }

    private void rehash() {
        Entry[] oldTable1 = table1;
        Entry[] oldTable2 = table2;

        Random gen = new Random();
        a = gen.nextInt(372965);
        b = gen.nextInt(162378);

        if(size >= capacity*2) {
            capacity *= 2;
        }
        size = 0;
        table1 = new Entry[capacity];
        table2 = new Entry[capacity];

        for(Entry e : oldTable1){
            if(e == null)
                continue;
            set(e.key, e.value);
        }
        for(Entry e : oldTable2){
            if(e == null)
                continue;
            set(e.key, e.value);
        }
//        System.out.println("resized!");
    }

    public double getLoadFactor() {
        return (double)size / (double)capacity;
    }

    int hash1(int h) {
        int result = 2135423121;
        String s = h + "";
        for (int i = 0; i < s.length(); i++) {
            result ^= (result << 5) +(s.charAt(i) - 'a' + a + b) + (result >> 2);
        }
        return result;
    }

    int hash2(int h) {
        int result = 0, c = a;
        String s = h + "";
        for (int i = 0; i < s.length(); i++) {
            result = result * c + (s.charAt(i) - 'a');
            c *= b;
        }
        return result;
    }

    static int indexFor(int h, int length) {
        return Math.abs(h) % length;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[ ");
        for(Entry e : table1){
            if(e == null)
                continue;
            sb.append(e.toString() + " ");
        }
        for(Entry e : table2){
            if(e == null)
                continue;
            sb.append(e.toString() + " ");
        }
        sb.append(']');
        return sb.toString();
    }
}
