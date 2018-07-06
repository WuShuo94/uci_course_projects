public class AVLTree extends Tree {
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
        updateNodeHeight(cur);

        for (Node g = cur.parent; g != null ; g = g.parent) {
            counter++;
            updateNodeHeight(g);
            if(!isAVLBalanced(g)) {
                g = rotateAt(tallerChild(tallerChild(g)));
            } else {
                updateNodeHeight(g);
                updateNodeHeight(g.lChild);
                updateNodeHeight(g.rChild);
            }
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

        for (Node g = sucParent; g != null ; g = g.parent) {
            counter++;
            updateNodeHeight(g);
            if(!isAVLBalanced(g)) {
                g = rotateAt(tallerChild(tallerChild(g)));
            }
        }
        updateNodeHeight(root);
        size--;
        return counter;
    }



    private boolean isAVLBalanced(Node node) {
        int l = 0, r = 0;

        if(node.lChild != null)
            l = node.lChild.height;
        if(node.rChild != null)
            r = node.rChild.height;

        if(Math.abs(l - r) < 2)
            return true;

        return false;
    }

    private Node tallerChild(Node node) {
        int l = 0, r = 0;
        if(node.lChild != null)
            l = node.lChild.height;
        if(node.rChild != null)
            r = node.rChild.height;

        if(l > r)
            return node.lChild;
        else
            return node.rChild;
    }

    private Node rotateAt(Node v) {
        Node p = v.parent;
        Node g = p.parent;
        if(p == g.lChild) { // zig
            if(v == p.lChild) { // zig-zig
                p.parent = g.parent;
                if(g.parent != null) {
                    if(g == g.parent.lChild)
                        g.parent.lChild = p;
                    else
                        g.parent.rChild = p;
                }
                g = trinodeReconstruction(v, p, g, v.lChild, v.rChild, p.rChild, g.rChild);
            } else {    //zig-zag
                v.parent = g.parent;
                if(g.parent != null) {
                    if(g == g.parent.lChild)
                        g.parent.lChild = v;
                    else
                        g.parent.rChild = v;
                }
                g = trinodeReconstruction(p, v, g, p.lChild, v.lChild, v.rChild, g.rChild);
            }
        } else {    //zag
            if(v == p.rChild) { // zag-zag
                p.parent = g.parent;
                if(g.parent != null) {
                    if(g == g.parent.lChild)
                        g.parent.lChild = p;
                    else
                        g.parent.rChild = p;
                }
                g = trinodeReconstruction(g, p, v, g.lChild, p.lChild, v.lChild, v.rChild);
            } else {    //zag-zig
                v.parent = g.parent;
                if(g.parent != null) {
                    if(g == g.parent.lChild)
                        g.parent.lChild = v;
                    else
                        g.parent.rChild = v;
                }
                g = trinodeReconstruction(g, v, p, g.lChild, v.lChild, v.rChild, p.rChild);
            }
        }
        return g;
    }

    private Node trinodeReconstruction(Node a, Node b, Node c, Node t0, Node t1, Node t2, Node t3) {
        a.lChild = t0;
        if(null != t0)
            t0.parent = a;
        a.rChild = t1;
        if(null != t1)
            t1.parent = a;
        updateNodeHeight(a);
        c.lChild = t2;
        if(null != t2)
            t2.parent = c;
        c.rChild = t3;
        if(null != t3)
            t3.parent = c;
        updateNodeHeight(c);
        b.lChild = a;
        b.rChild = c;
        a.parent = b;
        c.parent = b;
        updateNodeHeight(b);
        if(a == root || c == root)
            root = b;
        return b;
    }
}
