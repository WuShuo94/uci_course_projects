public class ChainedHashing {
    class Entry {
        int key, value;
        Entry next;

        Entry(int key, int value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public String toString() {
            return key + ":" + value;
        }
    }


    private Entry[] table;
    int size;
    int capacity;

    public ChainedHashing() {
        table = new Entry[16];
        capacity = 16;
        size = 0;
    }

    public ChainedHashing(int initialCapacity) {
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
        int index = indexFor(hash(key), capacity);
        int counter = 1;
        Entry entry = table[index];
        while(entry != null) {
            if (entry.key == key) {
                return new int[]{entry.value, counter};
            }
            entry = entry.next;
            counter++;
        }
        return new int[]{Integer.MIN_VALUE, counter};
    }

    public void set(int key, int value) {
        setAndCount(key, value);
    }

    public int setAndCount(int key, int value) {
        int counter = 0;
        counter += putEntry(new Entry(key, value));
        return counter;
    }

    public void delete(int key) {
        deleteAndCount(key);
    }

    public int deleteAndCount(int key) {
        int index = indexFor(hash(key), capacity);
        int counter = 1;
        Entry entry = table[index];

        if(entry == null)
//            throw new IllegalArgumentException("Element is Not Found ");
            return counter;

        if(entry.key == key) {
            table[index] = entry.next;
            size--;
            return counter;
        }

        Entry pre = entry;
        entry = entry.next;
        counter++;
        while(entry != null) {
            if(entry.key == key) {
                pre.next = entry.next;
                size--;
                return counter;
            }
            pre = pre.next;
            entry = entry.next;
            counter++;
        }
//        throw new IllegalArgumentException("Element is Not Found ");
        return counter;
    }

    public double getLoadFactor() {
        return (double)size / (double)capacity;
    }

    private int putEntry(Entry entry) {
        int index = indexFor(hash(entry.key), capacity);
        int counter = 1;
        if(table[index] == null) {
            table[index] = entry;
        } else if(table[index].key == entry.key) {
            table[index] = entry;
            return counter;
        } else {
            Entry pre = table[index];
            while(pre.next != null) {
                if(pre.key == entry.key) {
                    pre.value = entry.value;
                    return counter;
                }
                pre = pre.next;
                counter++;
            }
            pre.next = entry;
            counter++;
        }
        size++;
        return counter;
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
            while(e != null) {
                sb.append(e.toString() + " ");
                e = e.next;
            }
        }
        sb.append(']');
        return sb.toString();
    }
}
