import java.util.Arrays;

public class LinearHashing {

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


    private Entry[] table;
    int size;
    int capacity;

    public LinearHashing() {
        table = new Entry[16];
        capacity = 16;
        size = 0;
    }

    public LinearHashing(int initialCapacity) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        table = new Entry[initialCapacity];
        capacity = initialCapacity;
        size = 0;
    }

    public int search(int key) {
        int[] res = searchAndCount(key);
        if(res[0] == Integer.MIN_VALUE)
            throw new IllegalArgumentException("Element is Not Found ");
        return res[0];
    }

    public int[] searchAndCount(int key) {
        int counter = 1;
        int index = indexFor(hash(key), capacity);
        for (int i = 0; i < capacity && table[index] != null; i++, counter++) {
            if (table[index].key == key && table[index].value != Integer.MIN_VALUE) {
                return new int[]{table[index].value, counter};
            }
            index = (++index) % capacity;
        }
        return new int[]{Integer.MIN_VALUE, counter};
    }

    public void set(int key, int value) {
        setAndCount(key, value);
    }

    public int setAndCount(int key, int value) {
        int counter = 0;
        if(size >= capacity) {
            counter += resize();
        }
        counter += putEntry(new Entry(key, value));
        return counter;
    }

    public void delete(int key) {
        deleteAndCount(key);
    }

    public int deleteAndCount(int key) {
        int counter = 1;
        int index = indexFor(hash(key), capacity);
        for (int i = 0; i < capacity && table[index] != null; i++) {
            if (table[index].key == key && table[index].value != Integer.MIN_VALUE) {
                break;
            }
            index = (++index) % capacity;
            counter++;
        }

        if(table[index] == null || table[index].value == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Element is Not Found ");
//            return counter;
        }
        // lazy delete. use Integer.MIN_VALUE as 'nonempty but unused mark"
        table[index].value = Integer.MIN_VALUE;
        size--;
        return counter;
    }

    private int putEntry(Entry entry) {
        int counter = 1;
        int index = indexFor(hash(entry.key), capacity);
        while(true) {
            if(null == table[index] || table[index].value == Integer.MIN_VALUE) {
                table[index] = entry;
                size++;
                break;
            }
            if(entry.key == table[index].key) {
                table[index] = entry;
                break;
            }
            index = (++index) % capacity;
            counter++;
        }
        return counter;
    }

    private int resize() {
        Entry[] oldTable = table;

        capacity *= 2;
        size = 0;
        table = new Entry[capacity];

        int counter = 0;

        for(Entry e : oldTable){
            if(e == null || e.value == Integer.MIN_VALUE) {
                counter++;
                continue;
            }

            counter += putEntry(e);
        }
        return counter;
//        System.out.println("resized!");
    }

    public double getLoadFactor() {
        return (double)size / (double)capacity;
    }

    static int hash(int h) {
        h ^= (h >>> 16);
        return h;
    }

    static int indexFor(int h, int length) {
        return h % length;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[ ");
        for(Entry e : table){
            if(e == null || e.value == Integer.MIN_VALUE)
                continue;
            sb.append(e.toString() + " ");
        }
        sb.append(']');
        return sb.toString();
    }
}
