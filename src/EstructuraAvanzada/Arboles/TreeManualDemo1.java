package EstructuraAvanzada.Arboles;

import java.util.*;
import java.util.function.Predicate;

public class TreeManualDemo1 {
    // ===== Implementación de Árbol N-ario =====
    static class Tree<T> {
        static final class Node<E> {
            E value;
            Node<E> parent;
            List<Node<E>> children = new ArrayList<>();

            Node(E value) { this.value = value; }

            public E getValue() { return value; }
            public List<Node<E>> getChildren() { return Collections.unmodifiableList(children); }
            public Node<E> getParent() { return parent; }

            @Override public String toString() { return String.valueOf(value); }
        }

        private Node<T> root;
        private int size;

        public Tree() {}
        public Tree(T rootValue) { this.root = new Node<>(rootValue); this.size = 1; }

        public Node<T> getRoot() { return root; }
        public boolean isEmpty() { return root == null; }
        public int size() { return size; }

        // Crear raíz si no existe
        public Node<T> ensureRoot(T value) {
            if (root == null) {
                root = new Node<>(value);
                size = 1;
            }
            return root;
        }

        // ---- Inserciones ----
        public Node<T> addChild(Node<T> parent, T value) {
            Objects.requireNonNull(parent, "parent is null");
            Node<T> child = new Node<>(value);
            child.parent = parent;
            parent.children.add(child);
            size++;
            return child;
        }

        public List<Node<T>> addChildren(Node<T> parent, Collection<T> values) {
            Objects.requireNonNull(parent, "parent is null");
            List<Node<T>> added = new ArrayList<>(values.size());
            for (T v : values) added.add(addChild(parent, v));
            return added;
        }

        // Mover un subárbol completo a otro padre (no permite crear ciclos)
        public void moveSubtree(Node<T> subtreeRoot, Node<T> newParent) {
            Objects.requireNonNull(subtreeRoot, "subtreeRoot is null");
            Objects.requireNonNull(newParent, "newParent is null");
            if (subtreeRoot == root && newParent == root)
                return;
            // Evitar mover un nodo dentro de sí mismo o descendientes
            if (isAncestor(subtreeRoot, newParent))
                throw new IllegalArgumentException("Cannot move a node into its own descendant");
            // quitar del padre actual
            if (subtreeRoot.parent != null) {
                subtreeRoot.parent.children.remove(subtreeRoot);
            } else {
                // era raíz: nueva raíz pasa a ser la antigua raíz? -> aquí no cambiamos root,
                // solo permitimos reanclar si hay otro padre; si tenía parent null, ahora tendrá
                // newParent como padre (el árbol dejará de ser estrictamente enraizado único si root!=newParent.root)
                // Para mantener un árbol simple enraizado, prohibimos mover la raíz.
                throw new IllegalStateException("Moving the root is not supported in this simple Tree");
            }
            // enlazar con el nuevo padre
            subtreeRoot.parent = newParent;
            newParent.children.add(subtreeRoot);
        }

        // ---- Eliminación ----
        // Elimina un subárbol completo; devuelve cuántos nodos se borraron
        public int removeSubtree(Node<T> node) {
            Objects.requireNonNull(node, "node is null");
            if (node == root) {
                int removed = size;
                root = null; size = 0;
                return removed;
            }
            int removed = countNodes(node);
            node.parent.children.remove(node);
            // ayuda GC
            unlinkRecursive(node);
            size -= removed;
            return removed;
        }

        private void unlinkRecursive(Node<T> n) {
            n.parent = null;
            for (Node<T> c : n.children) unlinkRecursive(c);
            n.children.clear();
        }

        // ---- Propiedades estructurales ----
        public int height() { return height(root); }
        private int height(Node<T> n) {
            if (n == null) return -1; // árbol vacío
            if (n.children.isEmpty()) return 0;
            int h = 0;
            for (Node<T> c : n.children) h = Math.max(h, 1 + height(c));
            return h;
        }

        public int depth(Node<T> n) {
            int d = 0;
            while (n != null && n != root) { d++; n = n.parent; }
            return (n == null) ? -1 : d;
        }

        public boolean isAncestor(Node<T> a, Node<T> b) {
            if (a == null || b == null) return false;
            for (Node<T> x = b.parent; x != null; x = x.parent)
                if (x == a) return true;
            return false;
        }

        public List<Node<T>> pathTo(Node<T> n) {
            LinkedList<Node<T>> path = new LinkedList<>();
            while (n != null) { path.addFirst(n); n = n.parent; }
            return path;
        }

        // ---- Recorridos (devuelven lista de valores) ----
        public List<T> preOrder()  { List<T> r = new ArrayList<>(); preOrder(root, r);  return r; }
        public List<T> postOrder() { List<T> r = new ArrayList<>(); postOrder(root, r); return r; }
        public List<T> levelOrder(){
            List<T> out = new ArrayList<>();
            if (root == null) return out;
            ArrayDeque<Node<T>> q = new ArrayDeque<>();
            q.add(root);
            while (!q.isEmpty()) {
                Node<T> n = q.poll();
                out.add(n.value);
                for (Node<T> c : n.children) q.add(c);
            }
            return out;
        }

        private void preOrder(Node<T> n, List<T> out) {
            if (n == null) return;
            out.add(n.value);
            for (Node<T> c : n.children) preOrder(c, out);
        }
        private void postOrder(Node<T> n, List<T> out) {
            if (n == null) return;
            for (Node<T> c : n.children) postOrder(c, out);
            out.add(n.value);
        }

        // ---- Búsquedas ----
        public Node<T> findDFS(Predicate<T> pred) {
            return findDFS(root, pred);
        }
        private Node<T> findDFS(Node<T> n, Predicate<T> pred) {
            if (n == null) return null;
            if (pred.test(n.value)) return n;
            for (Node<T> c : n.children) {
                Node<T> r = findDFS(c, pred);
                if (r != null) return r;
            }
            return null;
        }

        public Node<T> findBFS(Predicate<T> pred) {
            if (root == null) return null;
            ArrayDeque<Node<T>> q = new ArrayDeque<>();
            q.add(root);
            while (!q.isEmpty()) {
                Node<T> n = q.poll();
                if (pred.test(n.value)) return n;
                for (Node<T> c : n.children) q.add(c);
            }
            return null;
        }

        // ---- Utilidades ----
        private int countNodes(Node<T> n) {
            if (n == null) return 0;
            int total = 1;
            for (Node<T> c : n.children) total += countNodes(c);
            return total;
        }

        public String toPrettyString() {
            if (root == null) return "(empty)";
            StringBuilder sb = new StringBuilder();
            toPretty(root, 0, sb);
            return sb.toString();
        }
        private void toPretty(Node<T> n, int depth, StringBuilder sb) {
            sb.append("  ".repeat(depth)).append("- ").append(n.value).append("\n");
            for (Node<T> c : n.children) toPretty(c, depth + 1, sb);
        }
    }

    // ===== Demo en main =====
    public static void main(String[] args) {
        // Creamos árbol de ejemplo:
        //        A
        //     /  |   \
        //    B   C    D
        //       / \    \
        //      E   F    G
        //          |
        //          H
        Tree<String> tree = new Tree<>("A");
        Tree.Node<String> A = tree.getRoot();

        Tree.Node<String> B = tree.addChild(A, "B");
        Tree.Node<String> C = tree.addChild(A, "C");
        Tree.Node<String> D = tree.addChild(A, "D");

        Tree.Node<String> E = tree.addChild(C, "E");
        Tree.Node<String> F = tree.addChild(C, "F");
        Tree.Node<String> G = tree.addChild(D, "G");
        Tree.Node<String> H = tree.addChild(F, "H");

        System.out.println("Pretty:\n" + tree.toPrettyString());
        System.out.println("size=" + tree.size() + " height=" + tree.height());

        System.out.println("\nRecorridos:");
        System.out.println("preOrder   : " + tree.preOrder());
        System.out.println("postOrder  : " + tree.postOrder());
        System.out.println("levelOrder : " + tree.levelOrder());

        System.out.println("\nBúsquedas:");
        var foundDFS = tree.findDFS(v -> v.equals("F"));
        var foundBFS = tree.findBFS(v -> v.equals("G"));
        System.out.println("findDFS(F) -> " + (foundDFS != null ? foundDFS.value : null));
        System.out.println("findBFS(G) -> " + (foundBFS != null ? foundBFS.value : null));

        System.out.println("\nRuta a H:");
        List<Tree.Node<String>> path = tree.pathTo(H);
        System.out.println(path);

        System.out.println("\nProfundidad y ancestros:");
        System.out.println("depth(H) = " + tree.depth(H));
        System.out.println("isAncestor(C, H) = " + tree.isAncestor(C, H));
        System.out.println("isAncestor(B, H) = " + tree.isAncestor(B, H));

        System.out.println("\nEliminar subárbol en C (C, E, F, H):");
        int removed = tree.removeSubtree(C);
        System.out.println("Eliminados: " + removed);
        System.out.println("size=" + tree.size() + " height=" + tree.height());
        System.out.println(tree.toPrettyString());
    }
}
