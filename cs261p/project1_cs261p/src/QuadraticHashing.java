public class QuadraticHashing {

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

    public QuadraticHashing() {
        table = new Entry[16];
        capacity = 16;
        size = 0;
    }

    public QuadraticHashing(int initialCapacity) {
        if (initialCapacity < 0)
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
        for (int i = 0; i < capacity  && table[index] != null; i++, counter++) {
            index = indexFor(hash(key)+i*i, capacity);
            if (table[index].key == key && table[index].value != Integer.MIN_VALUE) {
                return new int[]{table[index].value, counter};
            }
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
        int index = indexFor(hash(key), capacity);
        int counter = 1;
        for (int i = 0; i < capacity && table[index] != null; i++, counter++) {
            if (table[index].key == key && table[index].value != Integer.MIN_VALUE) {
                table[index].value = Integer.MIN_VALUE;
                size--;
                return counter;
            }
            index = indexFor(hash(key)+i*i, capacity);
        }
        throw new IllegalArgumentException("Element is Not Found ");
//        return counter;
    }

    private int putEntry(Entry entry) {
        int index, counter = 1;

        for (int i = 0; i < capacity; i++, counter++) {
            index = indexFor(hash(entry.key)+i*i, capacity);
            if(null == table[index] || table[index].value == Integer.MIN_VALUE) {
                table[index] = entry;
                size++;
                return counter;
            }
            if(entry.key == table[index].key) {
                table[index] = entry;
                return counter;
            }
        }
        counter += resize();
        counter += putEntry(entry);
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
