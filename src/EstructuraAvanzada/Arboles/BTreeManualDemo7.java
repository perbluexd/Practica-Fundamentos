package EstructuraAvanzada.Arboles;

import java.util.*;

/**
 * B-Tree genérico (grado mínimo t >= 2).
 * - Múltiples hijos por nodo (hasta 2t).
 * - Cada nodo (salvo la raíz) mantiene entre t-1 y 2t-1 claves.
 * - Búsqueda / Inserción / Eliminación: O(log n) en número de claves.
 *
 * Notas:
 *  - Implementación basada en CLRS (con arreglos dinámicos / ArrayList).
 *  - Orden natural (Comparable). Para Comparator podría ampliarse.
 */
public class BTreeManualDemo7 {

    /** Implementación de B-Tree (t = grado mínimo). */
    static class BTree<T extends Comparable<? super T>> {

        final int t;             // grado mínimo (t >= 2)
        Node<T> root;            // raíz

        /** Nodo del B-Tree. */
        static final class Node<E extends Comparable<? super E>> {
            boolean leaf;                 // ¿hoja?
            int n;                        // número actual de claves usadas
            ArrayList<E> keys;            // hasta 2t-1 claves
            ArrayList<Node<E>> children;  // hasta 2t hijos

            Node(boolean leaf, int t) {
                this.leaf = leaf;
                this.n = 0;
                this.keys = new ArrayList<>(2 * t - 1);
                this.children = new ArrayList<>(2 * t);
                // pre-rellenar con nulls para set(i, ..) seguro
                for (int i = 0; i < 2 * t - 1; i++) keys.add(null);
                for (int i = 0; i < 2 * t; i++) children.add(null);
            }

            @Override public String toString() {
                List<E> inUse = new ArrayList<>(n);
                for (int i = 0; i < n; i++) inUse.add(keys.get(i));
                return inUse.toString();
            }
        }

        public BTree(int t) {
            if (t < 2) throw new IllegalArgumentException("t must be >= 2");
            this.t = t;
            this.root = new Node<>(true, t);
        }

        // ===== BÚSQUEDA =====
        /** Devuelve true si la clave está en el árbol. */
        public boolean contains(T k) { return search(root, k) != null; }

        /** Busca y devuelve el nodo que contiene k; null si no existe. */
        private Node<T> search(Node<T> x, T k) {
            int i = 0;
            while (i < x.n && k.compareTo(x.keys.get(i)) > 0) i++;
            if (i < x.n && k.compareTo(x.keys.get(i)) == 0) return x;
            if (x.leaf) return null;
            return search(x.children.get(i), k);
        }

        // ===== INSERCIÓN =====
        public void insert(T k) {
            Node<T> r = root;
            if (r.n == 2 * t - 1) {
                Node<T> s = new Node<>(false, t);
                root = s;
                s.children.set(0, r);
                splitChild(s, 0, r);
                insertNonFull(s, k);
            } else {
                insertNonFull(r, k);
            }
        }

        /** Parte al hijo y sube la mediana. */
        private void splitChild(Node<T> parent, int i, Node<T> y) {
            Node<T> z = new Node<>(y.leaf, t);
            z.n = t - 1;
            // copiar la mitad derecha de claves de y -> z
            for (int j = 0; j < t - 1; j++) {
                z.keys.set(j, y.keys.get(j + t));
                z.children.set(j, z.children.get(j)); // no-op para mantener tamaño
            }
            if (!y.leaf) {
                for (int j = 0; j < t; j++) {
                    z.children.set(j, y.children.get(j + t));
                }
            }
            y.n = t - 1;

            // desplazar hijos del padre para insertar z
            for (int j = parent.n; j >= i + 1; j--) {
                parent.children.set(j + 1, parent.children.get(j));
            }
            parent.children.set(i + 1, z);

            // desplazar claves del padre e insertar la mediana de y
            for (int j = parent.n - 1; j >= i; j--) {
                parent.keys.set(j + 1, parent.keys.get(j));
            }
            parent.keys.set(i, y.keys.get(t - 1));
            parent.n++;
            // limpiar referencias que ya no se usan (ayuda a GC, opcional)
            for (int j = t - 1; j < 2 * t - 1; j++) {
                y.keys.set(j, null);
                if (!y.leaf) y.children.set(j + 1, null);
            }
        }

        /** Inserta en un nodo no lleno (descendiendo hasta hoja). */
        private void insertNonFull(Node<T> x, T k) {
            int i = x.n - 1;
            if (x.leaf) {
                // correr claves para hacer hueco
                while (i >= 0 && k.compareTo(x.keys.get(i)) < 0) {
                    x.keys.set(i + 1, x.keys.get(i));
                    i--;
                }
                // evitar duplicados (opcional: aquí los ignoramos)
                if (i >= 0 && k.compareTo(x.keys.get(i)) == 0) return;
                x.keys.set(i + 1, k);
                x.n++;
            } else {
                while (i >= 0 && k.compareTo(x.keys.get(i)) < 0) i--;
                i++;
                Node<T> child = x.children.get(i);
                if (child.n == 2 * t - 1) {
                    splitChild(x, i, child);
                    if (k.compareTo(x.keys.get(i)) > 0) i++;
                }
                insertNonFull(x.children.get(i), k);
            }
        }

        // ===== ELIMINACIÓN =====
        public boolean remove(T k) {
            int before = countKeys();
            delete(root, k);
            // Si la raíz queda con 0 claves y no es hoja, subir primer hijo
            if (root.n == 0 && !root.leaf) root = root.children.get(0);
            int after = countKeys();
            return after < before;
        }

        private void delete(Node<T> x, T k) {
            int idx = findKeyIndex(x, k);
            if (idx < x.n && k.compareTo(x.keys.get(idx)) == 0) {
                if (x.leaf) deleteFromLeaf(x, idx);
                else deleteFromNonLeaf(x, idx);
            } else {
                if (x.leaf) return; // no existe
                boolean atLast = (idx == x.n);
                Node<T> child = x.children.get(idx);
                if (child.n < t) fill(x, idx); // asegurar >= t-1 antes de descender
                if (atLast && idx > x.n) delete(x.children.get(idx - 1), k);
                else delete(x.children.get(idx), k);
            }
        }

        private int findKeyIndex(Node<T> x, T k) {
            int idx = 0;
            while (idx < x.n && k.compareTo(x.keys.get(idx)) > 0) idx++;
            return idx;
        }

        private void deleteFromLeaf(Node<T> x, int idx) {
            for (int i = idx + 1; i < x.n; i++) x.keys.set(i - 1, x.keys.get(i));
            x.keys.set(x.n - 1, null);
            x.n--;
        }

        private void deleteFromNonLeaf(Node<T> x, int idx) {
            T k = x.keys.get(idx);
            Node<T> y = x.children.get(idx);     // hijo izquierdo
            Node<T> z = x.children.get(idx + 1); // hijo derecho

            if (y.n >= t) {
                T pred = getPred(y);
                x.keys.set(idx, pred);
                delete(y, pred);
            } else if (z.n >= t) {
                T succ = getSucc(z);
                x.keys.set(idx, succ);
                delete(z, succ);
            } else {
                merge(x, idx);
                delete(y, k);
            }
        }

        private T getPred(Node<T> x) {
            while (!x.leaf) x = x.children.get(x.n);
            return x.keys.get(x.n - 1);
        }

        private T getSucc(Node<T> x) {
            while (!x.leaf) x = x.children.get(0);
            return x.keys.get(0);
        }

        /** Asegura que el hijo x.children[idx] tenga >= t-1 claves. */
        private void fill(Node<T> x, int idx) {
            if (idx > 0 && x.children.get(idx - 1).n >= t) borrowFromPrev(x, idx);
            else if (idx < x.n && x.children.get(idx + 1).n >= t) borrowFromNext(x, idx);
            else {
                if (idx < x.n) merge(x, idx);
                else merge(x, idx - 1);
            }
        }

        private void borrowFromPrev(Node<T> x, int idx) {
            Node<T> child = x.children.get(idx);
            Node<T> sibling = x.children.get(idx - 1);

            // correr child hacia la derecha
            for (int i = child.n - 1; i >= 0; i--) child.keys.set(i + 1, child.keys.get(i));
            if (!child.leaf) {
                for (int i = child.n; i >= 0; i--) child.children.set(i + 1, child.children.get(i));
            }
            // bajar la clave del padre
            child.keys.set(0, x.keys.get(idx - 1));
            if (!child.leaf) child.children.set(0, sibling.children.get(sibling.n));

            // subir la última clave del hermano al padre
            x.keys.set(idx - 1, sibling.keys.get(sibling.n - 1));

            child.n++;
            sibling.keys.set(sibling.n - 1, null);
            if (!sibling.leaf) sibling.children.set(sibling.n, null);
            sibling.n--;
        }

        private void borrowFromNext(Node<T> x, int idx) {
            Node<T> child = x.children.get(idx);
            Node<T> sibling = x.children.get(idx + 1);

            // bajar clave del padre a child
            child.keys.set(child.n, x.keys.get(idx));
            if (!child.leaf) child.children.set(child.n + 1, sibling.children.get(0));

            // subir primera clave del hermano al padre
            x.keys.set(idx, sibling.keys.get(0));

            // correr el hermano a la izquierda
            for (int i = 1; i < sibling.n; i++) sibling.keys.set(i - 1, sibling.keys.get(i));
            if (!sibling.leaf) {
                for (int i = 1; i <= sibling.n; i++) sibling.children.set(i - 1, sibling.children.get(i));
            }

            child.n++;
            sibling.n--;
            sibling.keys.set(sibling.n, null);
            if (!sibling.leaf) sibling.children.set(sibling.n + 1, null);
        }

        /** Fusiona child[idx] con child[idx+1]; baja la clave x.keys[idx] como mediana. */
        private void merge(Node<T> x, int idx) {
            Node<T> y = x.children.get(idx);
            Node<T> z = x.children.get(idx + 1);

            // insertar mediana
            y.keys.set(t - 1, x.keys.get(idx));
            // copiar claves de z
            for (int i = 0; i < z.n; i++) y.keys.set(i + t, z.keys.get(i));
            if (!y.leaf) {
                for (int i = 0; i <= z.n; i++) y.children.set(i + t, z.children.get(i));
            }
            y.n += 1 + z.n;

            // correr claves e hijos del padre para cerrar hueco
            for (int i = idx + 1; i < x.n; i++) x.keys.set(i - 1, x.keys.get(i));
            for (int i = idx + 2; i <= x.n; i++) x.children.set(i - 1, x.children.get(i));

            x.n--;
            // limpiar z (opcional)
            for (int i = 0; i < z.n; i++) z.keys.set(i, null);
            for (int i = 0; i <= z.n; i++) z.children.set(i, null);
        }

        // ===== UTILIDADES / INSPECCIÓN =====
        /** Cuenta todas las claves (para tests). */
        public int countKeys() { return countKeys(root); }
        private int countKeys(Node<T> x) {
            int sum = x.n;
            if (!x.leaf) for (int i = 0; i <= x.n; i++) sum += countKeys(x.children.get(i));
            return sum;
        }

        /** Recorrido in-order (claves en orden). */
        public List<T> inOrder() { List<T> r = new ArrayList<>(); inOrder(root, r); return r; }
        private void inOrder(Node<T> x, List<T> out) {
            if (x.leaf) {
                for (int i = 0; i < x.n; i++) out.add(x.keys.get(i));
            } else {
                for (int i = 0; i < x.n; i++) {
                    inOrder(x.children.get(i), out);
                    out.add(x.keys.get(i));
                }
                inOrder(x.children.get(x.n), out);
            }
        }

        /** Impresión nivel por nivel (claves por nodo). */
        public String toLevels() {
            StringBuilder sb = new StringBuilder();
            ArrayDeque<Node<T>> q = new ArrayDeque<>();
            q.add(root);
            while (!q.isEmpty()) {
                int sz = q.size();
                for (int i = 0; i < sz; i++) {
                    Node<T> n = q.poll();
                    sb.append(n).append("  ");
                    if (!n.leaf) for (int c = 0; c <= n.n; c++) q.add(n.children.get(c));
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    // ===== DEMO MAIN =====
    public static void main(String[] args) {
        // Grado mínimo t=3 => cada nodo tiene 2..5 claves (salvo raíz).
        BTree<Integer> bt = new BTree<>(3);

        int[] values = { 10, 20, 5, 6, 12, 30, 7, 17, 3, 4, 2, 25, 26, 27, 28, 29, 50, 60, 1, 8, 9, 11, 13, 14, 15, 16, 18, 19 };
        for (int v : values) bt.insert(v);

        System.out.println("B-Tree (niveles) después de inserciones:");
        System.out.println(bt.toLevels());
        System.out.println("InOrder (ordenado): " + bt.inOrder());
        System.out.println("contains(17) = " + bt.contains(17));
        System.out.println("contains(99) = " + bt.contains(99));

        // Eliminaciones variadas
        int[] del = { 6, 7, 4, 2, 16, 25, 1, 12, 20, 30, 29, 28, 27, 26, 50, 60, 3, 5, 10 };
        for (int d : del) {
            System.out.println("\nremove(" + d + ")");
            bt.remove(d);
            System.out.println(bt.toLevels());
            System.out.println("InOrder: " + bt.inOrder());
        }

        System.out.println("\nClaves totales: " + bt.countKeys());
    }
}
