package EstructuraAvanzada.Arboles;

import java.util.*;

/**
 * Árbol AVL genérico (balanceado por alturas).
 * - Inserción / Eliminación / Búsqueda: O(log n)
 * - Rebalanceo mediante rotaciones LL, RR, LR, RL.
 */
public class AVLManualDemo4 {

    // ===== Implementación =====
    static class AVL<T extends Comparable<? super T>> {

        static final class Node<E> {
            E key;
            Node<E> left, right;
            int height; // altura del nodo (hoja = 0)
            Node(E key) { this.key = key; }
            @Override public String toString() { return String.valueOf(key); }
        }

        private Node<T> root;
        private int size;

        public int size() { return size; }
        public boolean isEmpty() { return root == null; }
        public int height() { return height(root); }

        // ===== API =====
        public boolean contains(T key) {
            Node<T> x = root;
            while (x != null) {
                int c = key.compareTo(x.key);
                if (c == 0) return true;
                x = c < 0 ? x.left : x.right;
            }
            return false;
        }

        public void insert(T key) {
            if (key == null) throw new NullPointerException("key == null");
            root = insert(root, key);
        }

        public boolean remove(T key) {
            int before = size;
            root = remove(root, key);
            return size < before;
        }

        public List<T> inOrder() { List<T> r = new ArrayList<>(); inOrder(root, r); return r; }
        public List<T> preOrder(){ List<T> r = new ArrayList<>(); preOrder(root, r); return r; }
        public List<T> postOrder(){ List<T> r = new ArrayList<>(); postOrder(root, r); return r; }
        public List<T> levelOrder(){
            List<T> r = new ArrayList<>();
            if (root == null) return r;
            ArrayDeque<Node<T>> q = new ArrayDeque<>();
            q.add(root);
            while (!q.isEmpty()) {
                Node<T> n = q.poll();
                r.add(n.key);
                if (n.left != null) q.add(n.left);
                if (n.right != null) q.add(n.right);
            }
            return r;
        }

        public String toPrettyString() {
            if (root == null) return "(empty)";
            StringBuilder sb = new StringBuilder();
            toPretty(root, 0, sb);
            return sb.toString();
        }

        // ===== Internos =====
        private Node<T> insert(Node<T> n, T key) {
            if (n == null) { size++; return leaf(key); }
            int c = key.compareTo(n.key);
            if (c < 0) n.left = insert(n.left, key);
            else if (c > 0) n.right = insert(n.right, key);
            else return n; // ignorar duplicados
            return rebalance(update(n));
        }

        private Node<T> remove(Node<T> n, T key) {
            if (n == null) return null;
            int c = key.compareTo(n.key);
            if (c < 0) n.left = remove(n.left, key);
            else if (c > 0) n.right = remove(n.right, key);
            else {
                // encontrado
                if (n.left == null || n.right == null) {
                    Node<T> child = (n.left != null) ? n.left : n.right;
                    size--;
                    return child; // puede ser null (caso hoja)
                } else {
                    // dos hijos: reemplazar por sucesor (mínimo del subárbol derecho)
                    Node<T> succ = minNode(n.right);
                    n.key = succ.key;
                    n.right = remove(n.right, succ.key);
                }
            }
            return rebalance(update(n));
        }

        private Node<T> minNode(Node<T> x) {
            while (x.left != null) x = x.left;
            return x;
        }

        // --- utilidades de altura/balance ---
        private Node<T> leaf(T key) { Node<T> n = new Node<>(key); n.height = 0; return n; }
        private int height(Node<T> n) { return n == null ? -1 : n.height; }
        private int balance(Node<T> n) { return height(n.left) - height(n.right); }
        private Node<T> update(Node<T> n) {
            n.height = 1 + Math.max(height(n.left), height(n.right));
            return n;
        }

        // --- rotaciones ---
        private Node<T> rotateRight(Node<T> y) {
            Node<T> x = y.left;
            Node<T> B = x.right;
            x.right = y; y.left = B;
            update(y); update(x);
            return x;
        }

        private Node<T> rotateLeft(Node<T> y) {
            Node<T> x = y.right;
            Node<T> B = x.left;
            x.left = y; y.right = B;
            update(y); update(x);
            return x;
        }

        private Node<T> rebalance(Node<T> n) {
            int bf = balance(n);
            // LL o LR
            if (bf > 1) {
                if (balance(n.left) < 0) // LR
                    n.left = rotateLeft(n.left);
                return rotateRight(n);    // LL
            }
            // RR o RL
            if (bf < -1) {
                if (balance(n.right) > 0) // RL
                    n.right = rotateRight(n.right);
                return rotateLeft(n);      // RR
            }
            return n; // ya balanceado
        }

        // --- recorridos recursivos ---
        private void inOrder(Node<T> n, List<T> r) {
            if (n == null) return;
            inOrder(n.left, r); r.add(n.key); inOrder(n.right, r);
        }
        private void preOrder(Node<T> n, List<T> r) {
            if (n == null) return;
            r.add(n.key); preOrder(n.left, r); preOrder(n.right, r);
        }
        private void postOrder(Node<T> n, List<T> r) {
            if (n == null) return;
            postOrder(n.left, r); postOrder(n.right, r); r.add(n.key);
        }

        private void toPretty(Node<T> n, int depth, StringBuilder sb) {
            if (n == null) return;
            sb.append("  ".repeat(depth))
                    .append("- ").append(n.key)
                    .append(" (h=").append(n.height)
                    .append(", bf=").append(balance(n)).append(")\n");
            toPretty(n.left, depth + 1, sb);
            toPretty(n.right, depth + 1, sb);
        }
    }

    // ===== DEMO MAIN =====
    public static void main(String[] args) {
        AVL<Integer> avl = new AVL<>();

        // Probar rotaciones: LL
        avl.insert(30); avl.insert(20); avl.insert(10);
        System.out.println("Después de LL (30,20,10):");
        System.out.println(avl.toPrettyString());
        System.out.println("InOrder: " + avl.inOrder());

        // RR
        avl = new AVL<>();
        avl.insert(10); avl.insert(20); avl.insert(30);
        System.out.println("\nDespués de RR (10,20,30):");
        System.out.println(avl.toPrettyString());
        System.out.println("InOrder: " + avl.inOrder());

        // LR
        avl = new AVL<>();
        avl.insert(30); avl.insert(10); avl.insert(20);
        System.out.println("\nDespués de LR (30,10,20):");
        System.out.println(avl.toPrettyString());
        System.out.println("InOrder: " + avl.inOrder());

        // RL
        avl = new AVL<>();
        avl.insert(10); avl.insert(30); avl.insert(20);
        System.out.println("\nDespués de RL (10,30,20):");
        System.out.println(avl.toPrettyString());
        System.out.println("InOrder: " + avl.inOrder());

        // Inserciones y eliminaciones mixtas
        avl = new AVL<>();
        int[] vals = { 50, 30, 70, 20, 40, 60, 80, 10, 35, 65, 85, 5 };
        for (int v : vals) avl.insert(v);
        System.out.println("\nÁrbol completo:");
        System.out.println(avl.toPrettyString());
        System.out.println("size=" + avl.size() + " height=" + avl.height());
        System.out.println("LevelOrder: " + avl.levelOrder());

        System.out.println("\nEliminaciones (manteniendo balance):");
        avl.remove(70);
        avl.remove(30);
        avl.remove(10);
        System.out.println(avl.toPrettyString());
        System.out.println("InOrder: " + avl.inOrder());
        System.out.println("size=" + avl.size() + " height=" + avl.height());
    }
}
