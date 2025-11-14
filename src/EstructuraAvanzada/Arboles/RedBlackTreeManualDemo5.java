package EstructuraAvanzada.Arboles;

import java.util.*;

/**
 * Árbol Rojo-Negro (Red-Black Tree) genérico.
 * - Balanceo lógico por colores (propiedades RB)
 * - Búsqueda/Inserción/Eliminación: O(log n)
 * - Basado en CLRS con nodo NIL centinela.
 *
 * Notas:
 *  - No se permiten claves null.
 *  - Orden natural (Comparable). Si quieres Comparator, se puede extender luego.
 */
public class RedBlackTreeManualDemo5 {

    // ===== Implementación =====
    static class RedBlackTree<T extends Comparable<? super T>> {

        private static final boolean RED   = true;
        private static final boolean BLACK = false;

        static final class Node<E> {
            E key;
            Node<E> left, right, parent;
            boolean color; // RED=true, BLACK=false
            Node(E key, boolean color) { this.key = key; this.color = color; }
            @Override public String toString() { return String.valueOf(key) + (color==RED?"(R)":"(B)"); }
        }

        private final Node<T> NIL = new Node<>(null, BLACK); // centinela
        private Node<T> root = NIL;
        private int size = 0;

        public int size() { return size; }
        public boolean isEmpty() { return root == NIL; }

        // ---------- BÚSQUEDA ----------
        public boolean contains(T key) {
            if (key == null) return false;
            Node<T> x = root;
            while (x != NIL) {
                int c = key.compareTo(x.key);
                if (c == 0) return true;
                x = (c < 0) ? x.left : x.right;
            }
            return false;
        }

        // ---------- INSERCIÓN ----------
        public void insert(T key) {
            if (key == null) throw new NullPointerException("key == null");
            Node<T> z = new Node<>(key, RED);
            z.left = z.right = z.parent = NIL;

            Node<T> y = NIL;
            Node<T> x = root;
            while (x != NIL) {
                y = x;
                int c = key.compareTo(x.key);
                if (c < 0) x = x.left;
                else if (c > 0) x = x.right;
                else return; // ignorar duplicados
            }
            z.parent = y;
            if (y == NIL) root = z;
            else if (key.compareTo(y.key) < 0) y.left = z;
            else y.right = z;

            size++;
            insertFixup(z);
        }

        private void insertFixup(Node<T> z) {
            while (z.parent.color == RED) {
                if (z.parent == z.parent.parent.left) {
                    Node<T> y = z.parent.parent.right; // tío
                    if (y.color == RED) {
                        // Caso 1: padre y tío rojos
                        z.parent.color = BLACK;
                        y.color = BLACK;
                        z.parent.parent.color = RED;
                        z = z.parent.parent;
                    } else {
                        if (z == z.parent.right) {
                            // Caso 2: triángulo -> rotación izquierda
                            z = z.parent;
                            rotateLeft(z);
                        }
                        // Caso 3: línea -> rotación derecha
                        z.parent.color = BLACK;
                        z.parent.parent.color = RED;
                        rotateRight(z.parent.parent);
                    }
                } else {
                    // simétrico (padre es hijo derecho del abuelo)
                    Node<T> y = z.parent.parent.left;
                    if (y.color == RED) {
                        z.parent.color = BLACK;
                        y.color = BLACK;
                        z.parent.parent.color = RED;
                        z = z.parent.parent;
                    } else {
                        if (z == z.parent.left) {
                            z = z.parent;
                            rotateRight(z);
                        }
                        z.parent.color = BLACK;
                        z.parent.parent.color = RED;
                        rotateLeft(z.parent.parent);
                    }
                }
            }
            root.color = BLACK;
        }

        // ---------- ELIMINACIÓN ----------
        public boolean remove(T key) {
            if (key == null) return false;
            Node<T> z = root;
            while (z != NIL) {
                int c = key.compareTo(z.key);
                if (c == 0) break;
                z = (c < 0) ? z.left : z.right;
            }
            if (z == NIL) return false;

            Node<T> y = z;
            boolean yOriginalColor = y.color;
            Node<T> x;

            if (z.left == NIL) {
                x = z.right;
                transplant(z, z.right);
            } else if (z.right == NIL) {
                x = z.left;
                transplant(z, z.left);
            } else {
                y = minimum(z.right);
                yOriginalColor = y.color;
                x = y.right;
                if (y.parent == z) {
                    x.parent = y;
                } else {
                    transplant(y, y.right);
                    y.right = z.right; y.right.parent = y;
                }
                transplant(z, y);
                y.left = z.left; y.left.parent = y;
                y.color = z.color;
            }
            size--;

            if (yOriginalColor == BLACK) deleteFixup(x);
            // z queda elegible para GC
            return true;
        }

        private void deleteFixup(Node<T> x) {
            while (x != root && x.color == BLACK) {
                if (x == x.parent.left) {
                    Node<T> w = x.parent.right;
                    if (w.color == RED) {
                        // Caso 1
                        w.color = BLACK;
                        x.parent.color = RED;
                        rotateLeft(x.parent);
                        w = x.parent.right;
                    }
                    if (w.left.color == BLACK && w.right.color == BLACK) {
                        // Caso 2
                        w.color = RED;
                        x = x.parent;
                    } else {
                        if (w.right.color == BLACK) {
                            // Caso 3
                            w.left.color = BLACK;
                            w.color = RED;
                            rotateRight(w);
                            w = x.parent.right;
                        }
                        // Caso 4
                        w.color = x.parent.color;
                        x.parent.color = BLACK;
                        w.right.color = BLACK;
                        rotateLeft(x.parent);
                        x = root;
                    }
                } else {
                    // simétrico
                    Node<T> w = x.parent.left;
                    if (w.color == RED) {
                        w.color = BLACK;
                        x.parent.color = RED;
                        rotateRight(x.parent);
                        w = x.parent.left;
                    }
                    if (w.right.color == BLACK && w.left.color == BLACK) {
                        w.color = RED;
                        x = x.parent;
                    } else {
                        if (w.left.color == BLACK) {
                            w.right.color = BLACK;
                            w.color = RED;
                            rotateLeft(w);
                            w = x.parent.left;
                        }
                        w.color = x.parent.color;
                        x.parent.color = BLACK;
                        w.left.color = BLACK;
                        rotateRight(x.parent);
                        x = root;
                    }
                }
            }
            x.color = BLACK;
        }

        // ---------- UTILIDADES ----------
        private void rotateLeft(Node<T> x) {
            Node<T> y = x.right;
            x.right = y.left;
            if (y.left != NIL) y.left.parent = x;
            y.parent = x.parent;
            if (x.parent == NIL) root = y;
            else if (x == x.parent.left) x.parent.left = y;
            else x.parent.right = y;
            y.left = x;
            x.parent = y;
        }

        private void rotateRight(Node<T> x) {
            Node<T> y = x.left;
            x.left = y.right;
            if (y.right != NIL) y.right.parent = x;
            y.parent = x.parent;
            if (x.parent == NIL) root = y;
            else if (x == x.parent.right) x.parent.right = y;
            else x.parent.left = y;
            y.right = x;
            x.parent = y;
        }

        private void transplant(Node<T> u, Node<T> v) {
            if (u.parent == NIL) root = v;
            else if (u == u.parent.left) u.parent.left = v;
            else u.parent.right = v;
            v.parent = u.parent;
        }

        private Node<T> minimum(Node<T> x) {
            while (x.left != NIL) x = x.left;
            return x;
        }

        public void clear() { root = NIL; size = 0; }

        // ---------- Recorridos / representación ----------
        public List<T> inOrder()  { List<T> r = new ArrayList<>(); inOrder(root, r);  return r; }
        public List<T> levelOrder(){
            List<T> r = new ArrayList<>();
            if (root == NIL) return r;
            ArrayDeque<Node<T>> q = new ArrayDeque<>();
            q.add(root);
            while (!q.isEmpty()) {
                Node<T> n = q.poll();
                r.add(n.key);
                if (n.left  != NIL) q.add(n.left);
                if (n.right != NIL) q.add(n.right);
            }
            return r;
        }
        private void inOrder(Node<T> n, List<T> r) {
            if (n == NIL) return;
            inOrder(n.left, r);
            r.add(n.key);
            inOrder(n.right, r);
        }

        public String toPrettyString() {
            if (root == NIL) return "(empty)";
            StringBuilder sb = new StringBuilder();
            toPretty(root, 0, sb);
            return sb.toString();
        }
        private void toPretty(Node<T> n, int depth, StringBuilder sb) {
            if (n == NIL) return;
            sb.append("  ".repeat(depth))
                    .append("- ").append(n.key)
                    .append(n.color==RED ? " (R)" : " (B)")
                    .append("\n");
            toPretty(n.left, depth + 1, sb);
            toPretty(n.right, depth + 1, sb);
        }

        // (Opcional) Verificación simple de propiedades RB para pruebas rápidas
        public boolean checkInvariants() {
            if (root.color != BLACK) return false;        // raíz negra
            return blackHeight(root) >= 0 && noRedRed(root);
        }
        private int blackHeight(Node<T> n) {
            if (n == NIL) return 1;
            int lh = blackHeight(n.left);
            int rh = blackHeight(n.right);
            if (lh < 0 || rh < 0 || lh != rh) return -1;  // debe coincidir
            return lh + (n.color == BLACK ? 1 : 0);
        }
        private boolean noRedRed(Node<T> n) {
            if (n == NIL) return true;
            if (n.color == RED && (n.left.color == RED || n.right.color == RED)) return false;
            return noRedRed(n.left) && noRedRed(n.right);
        }
    }

    // ===== DEMO MAIN =====
    public static void main(String[] args) {
        RedBlackTree<Integer> rbt = new RedBlackTree<>();

        // Inserciones típicas
        int[] a = { 41, 38, 31, 12, 19, 8, 25, 50, 60, 55, 1, 5, 7 };
        for (int v : a) rbt.insert(v);

        System.out.println("Árbol Red-Black (después de inserciones):");
        System.out.println(rbt.toPrettyString());
        System.out.println("InOrder    : " + rbt.inOrder());
        System.out.println("LevelOrder : " + rbt.levelOrder());
        System.out.println("size=" + rbt.size() + " invariantsOK=" + rbt.checkInvariants());

        // Búsquedas
        System.out.println("\ncontains(25) -> " + rbt.contains(25));
        System.out.println("contains(99) -> " + rbt.contains(99));

        // Eliminaciones (ejercitan casos de fixup)
        int[] del = { 38, 41, 8, 12, 60, 7 };
        for (int d : del) {
            System.out.println("\nremove(" + d + ")");
            rbt.remove(d);
            System.out.println(rbt.toPrettyString());
            System.out.println("invariantsOK=" + rbt.checkInvariants() + " size=" + rbt.size());
        }
    }
}
