public class BinaryTree extends Tree {
    @Override
    public int insertAndCount(int key) {
        if(null == this.root) {
            this.root = new Node(1, key, null, null, null);
            size++;
            return 1;
        }

        // the key exists in the tree
        Object[] searchRes = searchAndCount(key);
        if((int)searchRes[1] > 0)
            return (int)searchRes[1];

        // insert
        Node cur = (Node)searchRes[0];
        int counter = -(int)searchRes[1] + 1;
        if(key < cur.value)
            cur.lChild = new Node(1, key, null, null, cur);
        else
            cur.rChild = new Node(1, key, null, null, cur);

        // update height of ancestors
        while(null != cur) {
            updateNodeHeight(cur);
            cur = cur.parent;
            counter++;
        }
        size++;

        return counter;
    }

    @Override
    public int deleteAndCount(int key) {
        if(null == this.root) {
            return -1;
        }

        Object[] searchRes = searchAndCount(key);

        // the key does not exist in the tree
        if((int)searchRes[1] < 0)
            return (int)searchRes[1];

        Node cur = (Node)searchRes[0];
        Node sucParent = null;

        Object[] removeRes = removeAt(cur);

        int counter = (int)searchRes[1] + (int)removeRes[1];
        sucParent = (Node)removeRes[0];

        // found and deleted. then update height
        while(sucParent!= null) {
            updateNodeHeight(sucParent);
            sucParent = sucParent.parent;
            counter++;
        }
        size--;

        return counter;
    }

}
