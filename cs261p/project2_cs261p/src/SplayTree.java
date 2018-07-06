public class SplayTree extends Tree {
    public Object[] searchAndSplay(int key) {
        Object[] res = super.searchAndCount(key);
        int counter = Math.abs((int)res[1]);
        Node cur = (Node)res[0];
        while(cur != root && cur != null) {
            splay(cur);
            counter++;
        }
        if(cur != null && cur.value != key)
            counter = -counter;
        return new Object[]{cur, counter};
    }

    @Override
    public Node search(int key) {
        Object[] res = searchAndSplay(key);

        if((int)res[1] < 0)
            return null;

        return (Node)res[0];
    }

    @Override
    public int insertAndCount(int key) {
        if(null == this.root) {
            this.root = new Node(1, key, null, null, null);
            size++;
            return 1;
        }

        Object[] searchRes = searchAndCount(key);
        Node cur = (Node)searchRes[0];
        int counter = Math.abs((int)searchRes[1]) + 1;
        // the key exists in the tree, splay the node directly.
        if((int)searchRes[1] > 0)
            return (int)searchRes[1];
        else {
            // insert
            if(key < cur.value) {
                cur.lChild = new Node(1, key, null, null, cur);
                cur = cur.lChild;
                updateNodeHeight(cur);
            } else {
                cur.rChild = new Node(1, key, null, null, cur);
                cur = cur.rChild;
                updateNodeHeight(cur);
            }
        }

        // update height of ancestors
        while(cur != root && cur != null) {
            splay(cur);
            counter++;
        }
        updateNodeHeight(cur);
        size++;
        return counter;
    }

    @Override
    public int deleteAndCount(int key) {
        if(null == this.root) {
            return -1;
        }

        Object[] searchRes = searchAndCount(key);

        // the key may not exist in the tree
        int counter = Math.abs((int)searchRes[1]);
        Node cur = (Node)searchRes[0];
        Node sucParent = cur;

        // found key and delete key, splay at parent node of the deleted key
        // else splay the last node of search path
        if((int)searchRes[1] > 0) {
            Object[] removeRes = removeAt(cur);
            counter += (int)removeRes[1];
            sucParent = (Node)removeRes[0];
            size--;
        }

        if(cur != null)
            cur = cur.parent;

        //update height
        while(!(sucParent == null || sucParent == cur)) {
            updateNodeHeight(sucParent);
            sucParent = sucParent.parent;
            counter++;
        }

        // splay
        while(cur != root && cur != null) {
            splay(cur);
            counter++;
        }

        return counter;
    }

    private void splay(Node x) {
        if(x == null) {
            return;
        } else if(x == root) {
            updateNodeHeight(root);
        } else if(x.parent == root) {
            // zig
            if(x == root.lChild)
                rightRotate(root);
            // zag
            else
                leftRotate(root);
            this.root = x;
            this.root.parent = null;
        } else {
            Node p = x.parent;
            Node g = p.parent;
            // zig-
            if(p == g.lChild) {
                // zig-zig
                if(x == p.lChild) {
                    rightRotate(g);
                    rightRotate(p);
                }
                // zig-zag
                else {
                    leftRotate(p);
                    rightRotate(g);
                }
            }
            // zag-
            else {
                // zag-zig
                if(x == p.lChild) {
                    rightRotate(p);
                    leftRotate(g);
                }
                // zag-zag
                if(x == p.rChild) {
                    leftRotate(g);
                    leftRotate(p);
                }
            }
        }
    }
}
