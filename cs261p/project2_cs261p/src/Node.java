import java.util.Random;

public class Node {
    int height;
    int value;
    Node lChild;
    Node rChild;
    Node parent;

    float priority;
    Random random = new Random();

    public Node() {
        this.height = 1;
        this.value = 0;
        this.lChild = null;
        this.rChild = null;
        this.parent = null;
        this.priority = random.nextFloat();
    }

    public Node(int height, int value, Node lChild, Node rChild, Node parent) {
        this.height = height;
        this.value = value;
        this.lChild = lChild;
        this.rChild = rChild;
        this.parent = parent;
        this.priority = random.nextFloat();
    }

    public Node(int height, int value, Node lChild, Node rChild, Node parent, float priority) {
        this.height = height;
        this.value = value;
        this.lChild = lChild;
        this.rChild = rChild;
        this.parent = parent;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Node{v=" + value + ", height=" + height + "} ";
    }

    public String toStringWithPriority() {
        return "Node{v=" + value + ", height=" + height + ", priority=" + priority + "} ";
    }
}
