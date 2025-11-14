package EstructuraAvanzada.Arboles;

import java.util.*;

/**
 * Árbol Binario (Binary Tree) genérico - sin reglas de ordenamiento.
 * Cada nodo tiene como máximo dos hijos: left y right.
 */
public class BinaryTreeDemo2 {

    static class BinaryTree<T> {
        static final class Node<E> {
            E value;
            Node<E> left, right;
            Node(E value) { this.value = value; }
            @Override public String toString() { return String.valueOf(value); }
        }

        private Node<T> root;
        private int size;

        public Node<T> getRoot() { return root; }
        public boolean isEmpty() { return root == null; }
        public int size() { return size; }

        // Crear raíz
        public Node<T> createRoot(T value) {
            if (root != null) throw new IllegalStateException("Root already exists");
            root = new Node<>(value);
            size = 1;
            return root;
        }

        // Agregar hijos manualmente
        public Node<T> addLeft(Node<T> parent, T value) {
            if (parent.left != null) throw new IllegalStateException("Left child already exists");
            Node<T> n = new Node<>(value);
            parent.left = n;
            size++;
            return n;
        }

        public Node<T> addRight(Node<T> parent, T value) {
            if (parent.right != null) throw new IllegalStateException("Right child already exists");
            Node<T> n = new Node<>(value);
            parent.right = n;
            size++;
            return n;
        }

        // Recorridos básicos
        public List<T> preOrder()  { List<T> r = new ArrayList<>(); preOrder(root, r);  return r; }
        public List<T> inOrder()   { List<T> r = new ArrayList<>(); inOrder(root, r);   return r; }
        public List<T> postOrder() { List<T> r = new ArrayList<>(); postOrder(root, r); return r; }
        public List<T> levelOrder(){
            List<T> r = new ArrayList<>();
            if (root == null) return r;
            Queue<Node<T>> q = new ArrayDeque<>();
            q.add(root);
            while (!q.isEmpty()) {
                Node<T> n = q.poll();
                r.add(n.value);
                if (n.left != null) q.add(n.left);
                if (n.right != null) q.add(n.right);
            }
            return r;
        }

        private void preOrder(Node<T> n, List<T> r) {
            if (n == null) return;
            r.add(n.value);
            preOrder(n.left, r);
            preOrder(n.right, r);
        }
        private void inOrder(Node<T> n, List<T> r) {
            if (n == null) return;
            inOrder(n.left, r);
            r.add(n.value);
            inOrder(n.right, r);
        }
        private void postOrder(Node<T> n, List<T> r) {
            if (n == null) return;
            postOrder(n.left, r);
            postOrder(n.right, r);
            r.add(n.value);
        }

        // Altura
        public int height() { return height(root); }
        private int height(Node<T> n) {
            if (n == null) return -1;
            return 1 + Math.max(height(n.left), height(n.right));
        }

        // Impresión jerárquica
        public String toPrettyString() {
            if (root == null) return "(empty)";
            StringBuilder sb = new StringBuilder();
            toPretty(root, 0, sb);
            return sb.toString();
        }
        private void toPretty(Node<T> n, int depth, StringBuilder sb) {
            if (n == null) return;
            sb.append("  ".repeat(depth)).append("- ").append(n.value).append("\n");
            toPretty(n.left, depth + 1, sb);
            toPretty(n.right, depth + 1, sb);
        }
    }

    // ==== DEMO MAIN ====
    public static void main(String[] args) {
        BinaryTree<String> tree = new BinaryTree<>();

        // Estructura:
        //        A
        //      /   \
        //     B     C
        //    / \   / \
        //   D  E  F   G
        var A = tree.createRoot("A");
        var B = tree.addLeft(A, "B");
        var C = tree.addRight(A, "C");
        var D = tree.addLeft(B, "D");
        var E = tree.addRight(B, "E");
        var F = tree.addLeft(C, "F");
        var G = tree.addRight(C, "G");

        System.out.println("Árbol Binario:");
        System.out.println(tree.toPrettyString());
        System.out.println("size = " + tree.size() + " height = " + tree.height());

        System.out.println("\nRecorridos:");
        System.out.println("PreOrder  : " + tree.preOrder());
        System.out.println("InOrder   : " + tree.inOrder());
        System.out.println("PostOrder : " + tree.postOrder());
        System.out.println("LevelOrder: " + tree.levelOrder());
    }
}
