import java.util.LinkedList;
import java.util.Queue;

public abstract class Tree {

    public Node root;
    public int size;

    public Tree() {
        this.root = null;
        this.size = 0;
    }

    public Tree(Node root) {
        this.root = root;
        if(root != null)
            this.size = 1;
        else
            this.size = 0;
    }

    public void insert(int key) {
        insertAndCount(key);
    }

    public void delete(int key) {
        deleteAndCount(key);
    }

    public Node search(int key) {
        Object[] res = searchAndCount(key);

        if((int)res[1] < 0)
            return null;

        return (Node)res[0];
    }

    protected Object[] removeAt(Node cur) {
        int counter = 1;
        Node sucParent = null;

        if(cur.rChild != null) {
            Object[] res = goAlongLeftBranch(cur.rChild);
            counter += (int)res[1];
            Node successor = (Node)res[0];

            // successor is right child of deleted node (cur).
            // it means cur.rChild == successor & cur.rChild.lChild == null
            if(successor.parent == cur) {
                sucParent = successor;
            } else {
                sucParent = successor.parent;   // successor must be the left child of sucParent
                sucParent.lChild = successor.rChild;
                if(successor.rChild != null)
                    successor.rChild.parent = sucParent;
                successor.rChild = cur.rChild;
                cur.rChild.parent = successor;
            }
            successor.lChild = cur.lChild;
            if(cur.lChild != null)
                cur.lChild.parent = successor;
            successor.parent = cur.parent;

            if(cur == root) {
                root = successor;
            } else {
                if(cur.parent.lChild == cur)
                    cur.parent.lChild = successor;
                if(cur.parent.rChild == cur)
                    cur.parent.rChild = successor;
            }

        }
        // cur.rChild == null
        else {
            sucParent = cur.parent;

            // cur is root node
            if(sucParent == null) {
                this.root = cur.lChild;
                if(root != null) {
                    root.parent = null;
                    updateNodeHeight(root);
                }
            }
            // delete other node
            else {
                if(sucParent.lChild == cur) {
                    sucParent.lChild = cur.lChild;
                } else {
                    sucParent.rChild = cur.lChild;
                }
                if(cur.lChild != null)
                    cur.lChild.parent = sucParent;
            }
        }

        updateNodeHeight(sucParent);

        return new Object[]{sucParent, counter};
    }


    // pivot is parent node. from left to right (child)  --> pivot becomes child.
    public boolean leftRotate(Node pivot) {
        Node rightNode = pivot.rChild;

        if(rightNode == null)
            return false;

        pivot.rChild = rightNode.lChild;
        if(pivot.rChild != null)
            pivot.rChild.parent = pivot;

        rightNode.lChild = pivot;
        rightNode.parent = pivot.parent;
        if(pivot.parent != null) {
            if(pivot == pivot.parent.lChild)
                pivot.parent.lChild = rightNode;
            else
                pivot.parent.rChild = rightNode;
        }
        pivot.parent = rightNode;

        updateNodeHeight(pivot);
        updateNodeHeight(rightNode);
        if(pivot == root) {
            root = rightNode;
            root.parent = null;
        }
        return true;
    }

    // pivot is parent node. from right to left (child)  --> pivot becomes child.
    public boolean rightRotate(Node pivot) {
        Node leftNode = pivot.lChild;

        if(leftNode == null)
            return false;

        pivot.lChild = leftNode.rChild;
        if(pivot.lChild != null)
            pivot.lChild.parent = pivot;

        leftNode.rChild = pivot;
        leftNode.parent = pivot.parent;
        if(pivot.parent != null) {
            if(pivot == pivot.parent.lChild)
                pivot.parent.lChild = leftNode;
            else
                pivot.parent.rChild = leftNode;
        }
        pivot.parent = leftNode;

        updateNodeHeight(pivot);
        updateNodeHeight(leftNode);
        if(pivot == root) {
            root = leftNode;
            root.parent = null;
        }
        return true;
    }

    public Object[] goAlongLeftBranch(Node node) {
        int counter = 1;
        while(null != node.lChild) {
            node = node.lChild;
            counter++;
        }
        return new Object[]{node, counter};
    }

    protected void updateNodeHeight(Node node) {
        if(node == null)
            return;
        int l = 0, r = 0;
        if(node.lChild != null)
            l = node.lChild.height;
        if(node.rChild != null)
            r = node.rChild.height;
        node.height = Math.max(l, r) + 1;
    }

    public abstract int insertAndCount(int key);

    public abstract int deleteAndCount(int key);

    public Object[] searchAndCount(int key) {
        if(null == this.root)
            return new Object[]{null, -1};

        int counter = 0;
        Node cur = this.root;

        while(true) {
            counter++;
            if(key == cur.value) {
                break;
            } else if(key < cur.value) {
                if(null == cur.lChild) {
                    counter = -counter;
                    break;
                }
                cur = cur.lChild;
            } else {
                if(null == cur.rChild) {
                    counter = -counter;
                    break;
                }
                cur = cur.rChild;
            }
        }

        return new Object[]{cur, counter};
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
                res.append(cur.toString());
                nextFrontier.add(cur.lChild);
                nextFrontier.add(cur.rChild);
            }
        }

        return res.toString();
    }
}
