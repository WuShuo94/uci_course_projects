import java.util.LinkedList;
import java.util.Queue;

public class Treap extends Tree {


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
        Node v = null;
        int counter = -(int)searchRes[1] + 1;
        if(key < cur.value) {
            v = new Node(1, key, null, null, cur);
            cur.lChild = v;
        } else {
            v = new Node(1, key, null, null, cur);
            cur.rChild = v;
        }
        updateNodeHeight(cur);

        while(v != root && v.priority > v.parent.priority) {
            counter++;
            if(v == v.parent.lChild)
                rightRotate(v.parent);
            else
                leftRotate(v.parent);
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

        // the key does not exist in the trees
        if((int)searchRes[1] < 0)
            return (int)searchRes[1];

        // exists. make it leaf node
        Node v = (Node)searchRes[0];
        int counter = (int)searchRes[1];
        while(!isLeafNode(v)) {
            ++counter;
            if(v.lChild == null)
                leftRotate(v);
            else if(v.rChild == null || v.lChild.priority > v.rChild.priority)
                rightRotate(v);
            else
                leftRotate(v);
            updateNodeHeight(v);
        }

        // v is the only node in the treap
        if(v == root) {
            root = null;
        } else {
            Node p = v.parent;
            if(v == p.lChild) {
                p.lChild = null;
            } else {
                p.rChild = null;
            }
            while(p != null) {
                updateNodeHeight(p);
                p = p.parent;
                counter++;
            }
        }
        ++counter;
        size--;

        return counter;
    }

    private boolean isLeafNode(Node node) {
        return (node.lChild == null) && (node.rChild == null);
    }

    @Override
    public String toString() {
        Queue<Node> frontier = new LinkedList<>();
        Queue<Node> nextFrontier = new LinkedList<>();
        Node cur = null;
        StringBuffer res = new StringBuffer();

        frontier.add(root);
        while(!(frontier.isEmpty() && nextFrontier.isEmpty())) {
            if(frontier.isEmpty()) {
                frontier = nextFrontier;
                nextFrontier = new LinkedList<>();
                res.append("\n");
            }

            cur = frontier.poll();

            if(cur == null) {
                res.append("null ");
            } else {
                res.append(cur.toStringWithPriority());
                nextFrontier.add(cur.lChild);
                nextFrontier.add(cur.rChild);
            }
        }

        return res.toString();
    }
}
