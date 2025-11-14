package EstructuraAvanzada.Arboles;

import java.util.*;

/**
 * Árbol Binario de Búsqueda (BST) genérico - implementación manual.
 * - insert(e): O(h)    (h = altura)
 * - contains(e): O(h)
 * - remove(e): O(h)
 * - Recorridos: inOrder, preOrder, postOrder, levelOrder
 *
 * NOTA: Este BST NO está balanceado (para balanceo: AVL / Red-Black).
 */
public class BSTManualDemo3 {

    // ===== Implementación =====
    static class BST<T extends Comparable<? super T>> implements Iterable<T> {
        private static final class Node<E> {
            E key;
            Node<E> left, right;
            Node(E key) { this.key = key; }
        }

        private Node<T> root;
        private int size;

        // --- Métricas ---
        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
        public int height() { return height(root); }

        private int height(Node<T> x) {
            if (x == null) return -1;                 // altura de árbol vacío = -1
            return 1 + Math.max(height(x.left), height(x.right));
        }

        // --- Inserción ---
        public void insert(T key) {
            if (key == null) throw new NullPointerException("key == null");
            root = insertRec(root, key);
        }

        private Node<T> insertRec(Node<T> x, T key) {
            if (x == null) { size++; return new Node<>(key); }
            int cmp = key.compareTo(x.key);
            if (cmp < 0)      x.left  = insertRec(x.left, key);
            else if (cmp > 0) x.right = insertRec(x.right, key);
            // si cmp==0, ignoramos duplicados (o podrías contar frecuencia)
            return x;
        }

        // --- Búsqueda ---
        public boolean contains(T key) {
            if (key == null) return false;
            Node<T> x = root;
            while (x != null) {
                int cmp = key.compareTo(x.key);
                if (cmp == 0) return true;
                x = (cmp < 0) ? x.left : x.right;
            }
            return false;
        }

        // --- Mínimo / Máximo ---
        public T min() {
            Node<T> m = minNode(root);
            return m == null ? null : m.key;
        }
        public T max() {
            Node<T> m = maxNode(root);
            return m == null ? null : m.key;
        }
        private Node<T> minNode(Node<T> x) {
            if (x == null) return null;
            while (x.left != null) x = x.left;
            return x;
        }
        private Node<T> maxNode(Node<T> x) {
            if (x == null) return null;
            while (x.right != null) x = x.right;
            return x;
        }

        // --- Eliminación ---
        public boolean remove(T key) {
            if (key == null || root == null) return false;
            int before = size;
            root = removeRec(root, key);
            return size < before;
        }

        private Node<T> removeRec(Node<T> x, T key) {
            if (x == null) return null;
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                x.left = removeRec(x.left, key);
            } else if (cmp > 0) {
                x.right = removeRec(x.right, key);
            } else {
                // Caso 1: sin hijos
                if (x.left == null && x.right == null) {
                    size--;
                    return null;
                }
                // Caso 2: un hijo
                if (x.left == null) {
                    size--;
                    return x.right;
                }
                if (x.right == null) {
                    size--;
                    return x.left;
                }
                // Caso 3: dos hijos -> reemplazar por sucesor (mínimo del subárbol derecho)
                Node<T> succ = minNode(x.right);
                x.key = succ.key;                      // copiar clave
                x.right = removeRec(x.right, succ.key); // eliminar sucesor
            }
            return x;
        }

        // --- Recorridos (devuelven listas para inspección) ---
        public List<T> inOrder()  { List<T> r = new ArrayList<>(); inOrder(root, r);  return r; }
        public List<T> preOrder() { List<T> r = new ArrayList<>(); preOrder(root, r); return r; }
        public List<T> postOrder(){ List<T> r = new ArrayList<>(); postOrder(root, r);return r; }
        public List<T> levelOrder(){
            List<T> r = new ArrayList<>();
            if (root == null) return r;
            ArrayDeque<Node<T>> q = new ArrayDeque<>();
            q.add(root);
            while (!q.isEmpty()) {
                Node<T> n = q.poll();
                r.add(n.key);
                if (n.left  != null) q.add(n.left);
                if (n.right != null) q.add(n.right);
            }
            return r;
        }

        private void inOrder(Node<T> x, List<T> r) {
            if (x == null) return;
            inOrder(x.left, r);
            r.add(x.key);
            inOrder(x.right, r);
        }
        private void preOrder(Node<T> x, List<T> r) {
            if (x == null) return;
            r.add(x.key);
            preOrder(x.left, r);
            preOrder(x.right, r);
        }
        private void postOrder(Node<T> x, List<T> r) {
            if (x == null) return;
            postOrder(x.left, r);
            postOrder(x.right, r);
            r.add(x.key);
        }

        // --- Utilidades ---
        public void clear() { root = null; size = 0; }

        @Override
        public String toString() { return inOrder().toString(); }

        // Iterador in-order (de menor a mayor)
        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private final Deque<Node<T>> stack = new ArrayDeque<>();
                { pushLeft(root); }
                private void pushLeft(Node<T> x) { while (x != null) { stack.push(x); x = x.left; } }
                @Override public boolean hasNext() { return !stack.isEmpty(); }
                @Override public T next() {
                    if (stack.isEmpty()) throw new NoSuchElementException();
                    Node<T> n = stack.pop();
                    if (n.right != null) pushLeft(n.right);
                    return n.key;
                }
            };
        }
    }

    // ===== Demo en main =====
    public static void main(String[] args) {
        BST<Integer> bst = new BST<>();

        System.out.println("=== INSERT ===");
        int[] vals = { 8, 3, 10, 1, 6, 14, 4, 7, 13 };
        for (int v : vals) bst.insert(v);
        System.out.println("InOrder  (sorted): " + bst.inOrder());   // [1,3,4,6,7,8,10,13,14]
        System.out.println("PreOrder          : " + bst.preOrder()); // [8,3,1,6,4,7,10,14,13]
        System.out.println("PostOrder         : " + bst.postOrder());// [1,4,7,6,3,13,14,10,8]
        System.out.println("LevelOrder        : " + bst.levelOrder());// [8,3,10,1,6,14,4,7,13]
        System.out.println("size=" + bst.size() + " height=" + bst.height());
        System.out.println("min=" + bst.min() + " max=" + bst.max());

        System.out.println("\n=== CONTAINS ===");
        System.out.println("contains(7) -> " + bst.contains(7));
        System.out.println("contains(99) -> " + bst.contains(99));

        System.out.println("\n=== REMOVE (hoja) 1 ===");
        bst.remove(1);
        System.out.println("InOrder: " + bst.inOrder());

        System.out.println("\n=== REMOVE (un hijo) 14 ===");
        bst.remove(14);
        System.out.println("InOrder: " + bst.inOrder());

        System.out.println("\n=== REMOVE (dos hijos) 3 ===");
        bst.remove(3);
        System.out.println("InOrder: " + bst.inOrder());

        System.out.println("\n=== ITERADOR IN-ORDER ===");
        for (int x : bst) System.out.print(x + " ");
        System.out.println();

        System.out.println("\n=== CLEAR ===");
        bst.clear();
        System.out.println("empty? " + bst.isEmpty());
    }
}
